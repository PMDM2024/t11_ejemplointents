package net.iessochoa.pmdm.t11_ejemplointents.ui.screens

import android.Manifest
import android.provider.CallLog
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.google.accompanist.permissions.shouldShowRationale

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun PermisosScreen(
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val permissionState = rememberPermissionState(Manifest.permission.WRITE_CALL_LOG)
    Column(
        modifier = modifier

    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally, // Centra los elementos horizontalmente
            verticalArrangement = Arrangement.Center // Centra verticalmente
        ) {
            if (permissionState.status.isGranted) {
            Text("Camera permission Granted")
        } else {
            Column {
                val textToShow = if (permissionState.status.shouldShowRationale) {
                    // If the user has denied the permission but the rationale can be shown,
                    // then gently explain why the app requires this permission
                    "The camera is important for this app. Please grant the permission."
                } else {
                    // If it's the first time the user lands on this feature, or the user
                    // doesn't want to be asked again for this permission, explain that the
                    // permission is required
                    "Camera permission required for this feature to be available. " +
                            "Please grant the permission"
                }
                Text(textToShow)
                Button(onClick = { permissionState.launchPermissionRequest() }) {
                    Text("Request permission")
                }
            }
        }
            // Texto superior
            Text(
                text = "Este es un ejemplo de Compose",
                fontSize = 20.sp,
                modifier = Modifier.padding(bottom = 16.dp) // Espaciado entre el texto y el botón
            )

            // Botón debajo del texto
            Button(onClick = {
                context.getContentResolver().delete(
                    CallLog.Calls.CONTENT_URI,
                    "number = 555555",null
                )
             }) {
                Text(text = "Borrar 555-555", fontSize = 18.sp)
            }
        }

    }
}