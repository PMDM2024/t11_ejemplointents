package net.iessochoa.pmdm.t11_ejemplointents

import android.content.Intent
import android.net.Uri
import android.provider.MediaStore
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.platform.LocalContext

@Composable
fun IntentsAndPermissionsScreen(modifier: Modifier) {
    val context = LocalContext.current

    // Launcher para permisos
    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        val message = if (isGranted) "Permiso concedido" else "Permiso denegado"
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Intents y Permisos",
            fontSize = 30.sp,
            modifier = Modifier.padding(16.dp))

        // Botón 1: Abrir URL
        Button(
            onClick = {
            val url = "https://portal.edu.gva.es/03013224/"
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
            context.startActivity(intent)
            //debemos comprobar si existe una actividad que pueda manejar la acción
            //pero desde es necesario incluir el permiso de internet en el manifest
            //puedes verlo en el manifest
           /* if (intent.resolveActivity(context.packageManager) != null) {
                context.startActivity(intent)
            } else {
                Toast.makeText(context, "No se puede abrir la URL", Toast.LENGTH_SHORT).show()
            }*/
        }, modifier = Modifier.fillMaxWidth().padding(8.dp)) {
            Text("Abrir URL")
        }

        // Botón 2: Llamar por teléfono
        Button(
            onClick = {
            val phoneNumber = "966912260"
            val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:$phoneNumber"))
            context.startActivity(intent)
            /*if (intent.resolveActivity(context.packageManager) != null) {
                context.startActivity(intent)
            } else {
                Toast.makeText(context, "No se puede realizar la llamada", Toast.LENGTH_SHORT).show()
            }*/
        }, modifier = Modifier.fillMaxWidth().padding(8.dp)) {
            Text("Llamar por teléfono")
        }

        // Botón 3: Abrir Google Maps
        Button(
            onClick = {
            val geoUri = "geo:38.278855,-0.716400?22"
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(geoUri))
            context.startActivity(intent)
            /*if (intent.resolveActivity(context.packageManager) != null) {
                context.startActivity(intent)
            } else {
                Toast.makeText(context, "No se puede abrir Google Maps", Toast.LENGTH_SHORT).show()
            }*/
        }, modifier = Modifier.fillMaxWidth().padding(8.dp)) {
            Text("Google Maps")
        }

        // Botón 4: Abrir cámara
        Button(
            onClick = {
            val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            context.startActivity(intent)
            /*if (intent.resolveActivity(context.packageManager) != null) {
                context.startActivity(intent)
            } else {
                Toast.makeText(context, "No se puede abrir la cámara", Toast.LENGTH_SHORT).show()
            }*/
        }, modifier = Modifier.fillMaxWidth().padding(8.dp)) {
            Text("Abrir cámara")
        }

        // Botón 5: Mandar correo
        Button(
            onClick = {
            val intent = Intent(Intent.ACTION_SENDTO).apply {
                data = Uri.parse("mailto:")
                putExtra(Intent.EXTRA_EMAIL, arrayOf("pepe@edu.gva.es"))
                putExtra(Intent.EXTRA_TEXT, "texto del correo")
                putExtra(Intent.EXTRA_SUBJECT, "Asunto del correo")
            }
            context.startActivity(intent)
            /*if (intent.resolveActivity(context.packageManager) != null) {
                context.startActivity(intent)
            } else {
                Toast.makeText(context, "No se puede mandar el correo", Toast.LENGTH_SHORT).show()
            }*/
        }, modifier = Modifier.fillMaxWidth().padding(8.dp)) {
            Text("Mandar correo")
        }

        // Botón 6: Solicitar permisos
        Button(
            onClick = {
            permissionLauncher.launch(android.Manifest.permission.CAMERA)
        }, modifier = Modifier.fillMaxWidth().padding(8.dp)) {
            Text("Permisos")
        }
    }
}
