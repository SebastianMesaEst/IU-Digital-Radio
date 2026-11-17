package com.example.iudigitalradio

import android.Manifest
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.os.Build
import android.os.Bundle
import android.os.VibrationEffect
import android.os.Vibrator
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.media3.common.MediaItem
import androidx.media3.common.PlaybackException
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import com.example.iudigitalradio.ui.theme.IUDigitalRadioTheme

private val AzulIU = Color(0xFF09234D)
private val RojoIU = Color(0xFFD91C3B)
private val AmarilloIU = Color(0xFFFFD21C)
private val FondoIU = Color(0xFFF3F6FC)

data class Emisora(
    val nombre: String,
    val url: String?
)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            IUDigitalRadioTheme {
                RadioScreen()
            }
        }
    }
}

@Composable
fun RadioScreen() {
    val context = LocalContext.current
    val player = remember(context) {
        ExoPlayer.Builder(context).build()
    }

    val emisoras = remember {
        listOf(
            Emisora(
                nombre = "Radio Paradise - Rock Mix",
                url = "https://stream.radioparadise.com/rock-192"
            ),
            Emisora(
                nombre = "Radio Paradise - Main Mix",
                url = "https://stream.radioparadise.com/mp3-192"
            ),
            Emisora(
                nombre = "Radio Paradise - Mellow Mix",
                url = "https://stream.radioparadise.com/mellow-192"
            ),
            Emisora(nombre = "La Ochentera 91.9 FM", url = null),
            Emisora(nombre = "98.9 Clásicos", url = null)
        )
    }

    var emisoraSeleccionada by rememberSaveable {
        mutableStateOf(emisoras[0].nombre)
    }
    var isPlaying by remember { mutableStateOf(false) }
    var isMuted by remember { mutableStateOf(false) }
    var estadoReproductor by remember { mutableStateOf("Pausado") }

    DisposableEffect(player) {
        val listener = object : Player.Listener {
            override fun onPlaybackStateChanged(playbackState: Int) {
                estadoReproductor = when (playbackState) {
                    Player.STATE_IDLE -> {
                        if (player.playerError != null) "Error de conexión" else "Pausado"
                    }
                    Player.STATE_BUFFERING -> "Conectando..."
                    Player.STATE_READY -> {
                        if (player.isPlaying) "Reproduciendo" else "Pausado"
                    }
                    Player.STATE_ENDED -> "Finalizado"
                    else -> estadoReproductor
                }
            }

            override fun onIsPlayingChanged(reproduciendo: Boolean) {
                isPlaying = reproduciendo
                if (player.playbackState == Player.STATE_READY) {
                    estadoReproductor =
                        if (reproduciendo) "Reproduciendo" else "Pausado"
                }
            }

            override fun onPlayerError(error: PlaybackException) {
                isPlaying = false
                estadoReproductor = when (error.errorCode) {
                    PlaybackException.ERROR_CODE_IO_NETWORK_CONNECTION_FAILED,
                    PlaybackException.ERROR_CODE_IO_NETWORK_CONNECTION_TIMEOUT ->
                        "Error: revisa tu conexión a Internet"
                    PlaybackException.ERROR_CODE_IO_BAD_HTTP_STATUS ->
                        "Error: transmisión no disponible"
                    else -> "Error al reproducir la emisora"
                }
            }
        }
        player.addListener(listener)
        onDispose {
            player.removeListener(listener)
            player.release()
        }
    }

    val vibrator = remember(context) {
        context.getSystemService(Vibrator::class.java)
    }

    fun vibrar() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            vibrator?.vibrate(
                VibrationEffect.createOneShot(100, VibrationEffect.DEFAULT_AMPLITUDE)
            )
        } else {
            @Suppress("DEPRECATION")
            vibrator?.vibrate(100)
        }
    }

    var imagenPerfil by remember { mutableStateOf<Bitmap?>(null) }
    val cameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicturePreview()
    ) { bitmap ->
        imagenPerfil = bitmap
    }
    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) cameraLauncher.launch(null)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(FondoIU)
            .padding(16.dp),
        verticalArrangement = Arrangement.Top
    ) {
        Text(
            text = "IU Digital Radio",
            color = AzulIU,
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = "Tu música, tus clásicos, tu emisora",
            color = RojoIU,
            fontSize = 14.sp
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(text = emisoraSeleccionada, color = AzulIU)
        Spacer(modifier = Modifier.height(16.dp))

        // Fotografía de perfil
        Card(modifier = Modifier.fillMaxWidth()) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                val foto = imagenPerfil
                if (foto != null) {
                    Image(
                        bitmap = foto.asImageBitmap(),
                        contentDescription = "Foto de perfil",
                        modifier = Modifier
                            .size(72.dp)
                            .clip(CircleShape),
                        contentScale = ContentScale.Crop
                    )
                } else {
                    Text("Sin foto de perfil")
                }

                Button(onClick = {
                    val permiso = ContextCompat.checkSelfPermission(
                        context, Manifest.permission.CAMERA
                    )
                    if (permiso == PackageManager.PERMISSION_GRANTED) {
                        cameraLauncher.launch(null)
                    } else {
                        permissionLauncher.launch(Manifest.permission.CAMERA)
                    }
                }) {
                    Text("Tomar foto")
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Reproductor azul institucional
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = AzulIU,
                contentColor = Color.White
            )
        ) {
            Column(modifier = Modifier.padding(20.dp)) {
                Text(
                    text = "♫ IU Digital Radio",
                    color = AmarilloIU,
                    fontSize = 21.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text("Emisora actual:", color = Color.White)
                Text(
                    text = emisoraSeleccionada,
                    color = Color.White,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "● $estadoReproductor",
                    color = if (isPlaying) AmarilloIU else Color.White,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Medium
                )
                Text(
                    text = if (isMuted) "Sonido silenciado" else "Sonido activado",
                    color = Color.White
                )
                Spacer(modifier = Modifier.height(12.dp))

                Row(
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Button(
                        onClick = {
                            val emisoraActual = emisoras.firstOrNull {
                                it.nombre == emisoraSeleccionada
                            }
                            val url = emisoraActual?.url
                            if (url != null) {
                                if (player.mediaItemCount == 0 || player.playerError != null) {
                                    player.setMediaItem(MediaItem.fromUri(url))
                                    player.prepare()
                                }
                                player.play()
                            } else {
                                player.stop()
                                player.clearMediaItems()
                                estadoReproductor = "Emisora no disponible"
                            }
                            vibrar()
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = AmarilloIU,
                            contentColor = AzulIU
                        )
                    ) {
                        Text("Play")
                    }

                    Button(
                        onClick = {
                            player.pause()
                            estadoReproductor = "Pausado"
                            vibrar()
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color.White,
                            contentColor = AzulIU
                        )
                    ) {
                        Text("Pause")
                    }

                    Button(
                        onClick = {
                            isMuted = !isMuted
                            player.volume = if (isMuted) 0f else 1f
                            vibrar()
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color.White,
                            contentColor = AzulIU
                        )
                    ) {
                        Text(if (isMuted) "Unmute" else "Mute")
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "Emisoras disponibles",
            color = AzulIU,
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(8.dp))

        LazyColumn(modifier = Modifier.weight(1f)) {
            items(emisoras) { emisora ->
                Button(
                    enabled = emisora.url != null,
                    onClick = {
                        val url = emisora.url
                        if (url != null && (
                                    emisoraSeleccionada != emisora.nombre ||
                                            player.mediaItemCount == 0 ||
                                            player.playerError != null
                                    )
                        ) {
                            player.stop()
                            player.clearMediaItems()
                            emisoraSeleccionada = emisora.nombre
                            player.setMediaItem(MediaItem.fromUri(url))
                            player.prepare()
                            player.play()
                            estadoReproductor = "Conectando..."
                        }
                        vibrar()
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp)
                ) {
                    Text(
                        when {
                            emisora.url == null -> "${emisora.nombre} (pendiente)"
                            emisora.nombre == emisoraSeleccionada ->
                                "${emisora.nombre} ✓ ACTIVA"
                            else -> emisora.nombre
                        }
                    )
                }
            }
        }
    }
}
