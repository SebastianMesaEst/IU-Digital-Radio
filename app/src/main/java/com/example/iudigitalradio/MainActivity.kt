package com.example.iudigitalradio

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.iudigitalradio.ui.theme.IUDigitalRadioTheme
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.core.content.ContextCompat
import android.Manifest
import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.ui.platform.LocalContext
import androidx.compose.foundation.Image
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import android.os.Vibrator
import android.os.VibrationEffect
import android.os.Build

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
    val emisoras = listOf(
        "La Ochentera (91.9 FM)",
        "98.9 Clásicos (98.9 FM)",
        "Radio Retro",
        "Éxitos de los 80"
    )

    var emisoraSeleccionada by rememberSaveable {
        mutableStateOf(emisoras[0])
    }

    var isPlaying by rememberSaveable {
        mutableStateOf(false)
    }

    var isMuted by rememberSaveable {
        mutableStateOf(false)
    }

    val context = LocalContext.current

    val vibrator = context.getSystemService(Vibrator::class.java)
    fun vibrar() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            vibrator?.vibrate(
                VibrationEffect.createOneShot(
                    100,
                    VibrationEffect.DEFAULT_AMPLITUDE
                )
            )
        } else {
            @Suppress("DEPRECATION")
            vibrator?.vibrate(100)
        }
    }

    var imagenPerfil by remember {
        mutableStateOf<android.graphics.Bitmap?>(null)
    }
    val cameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicturePreview()
    ) { bitmap ->
        imagenPerfil = bitmap
    }
    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            cameraLauncher.launch(null)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Top
    ) {

        // Título
        Text(
            text = emisoraSeleccionada
        )

        Spacer(modifier = Modifier.height(20.dp))

        // -------------------------
        // SECCIÓN DE PERFIL
        // -------------------------

        Card(
            modifier = Modifier.fillMaxWidth()
        ) {

            Row(
                modifier = Modifier.padding(16.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {

                if (imagenPerfil != null) {
                    Image(
                        bitmap = imagenPerfil!!.asImageBitmap(),
                        contentDescription = "Foto de perfil",
                        modifier = Modifier
                            .clip(CircleShape),
                        contentScale = ContentScale.Crop
                    )
                } else {
                    Text(
                        text = "Sin foto de perfil"
                    )
                }

                Button(
                    onClick = {
                        val cameraPermission = ContextCompat.checkSelfPermission(
                            context,
                            Manifest.permission.CAMERA
                        )

                        if (cameraPermission == PackageManager.PERMISSION_GRANTED) {
                            cameraLauncher.launch(null)
                        } else {
                            permissionLauncher.launch(Manifest.permission.CAMERA)
                        }
                    }
                ) {
                    Text("Tomar foto")
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // -------------------------
        // REPRODUCTOR
        // -------------------------

        Card(
            modifier = Modifier.fillMaxWidth()
        ) {

            Column(
                modifier = Modifier.padding(16.dp)
            ) {

                Text(
                    text = "Reproductor"
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "Emisora actual"
                )

                Text(
                    text = emisoraSeleccionada
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = if (isPlaying) "▶ Reproduciendo" else "⏸ Pausado"
                )

                Text(
                    text = if (isMuted) " Silenciado" else " Sonido activado"
                )

                Spacer(modifier = Modifier.height(12.dp))

                Row(
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    modifier = Modifier.fillMaxWidth()
                ) {

                    Button(
                        onClick = {
                            isPlaying = true
                            vibrar()
                        }
                    ) {
                        Text("Play")
                    }

                    Button(
                        onClick = {
                            isPlaying = false
                            vibrar()
                        }
                    ) {
                        Text("Pause")
                    }

                    Button(
                        onClick = {
                            isMuted = !isMuted
                            vibrar()
                        }
                    ) {
                        Text(
                            if (isMuted) "Unmute" else "Mute"
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // -------------------------
        // CATÁLOGO
        // -------------------------

        Text(
            text = "Emisoras disponibles"
        )

        Spacer(modifier = Modifier.height(8.dp))

        LazyColumn {
            items(emisoras) { emisora ->

                Button(
                    onClick = {
                        emisoraSeleccionada = emisora
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp)
                ) {
                    Text(
                        if (emisora == emisoraSeleccionada) {
                            " $emisora ✓ ACTIVA"
                        } else {
                            " $emisora"
                        }
                    )
                }
            }
        }
    }
}