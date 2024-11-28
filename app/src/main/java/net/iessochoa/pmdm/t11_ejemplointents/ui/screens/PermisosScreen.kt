package net.iessochoa.pmdm.t11_ejemplointents.ui.screens

import android.Manifest
import android.provider.CallLog
import android.widget.Toast
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


            // Botón debajo del texto
            Button(onClick = {
                if (permissionState.status.isGranted){
                    context.getContentResolver().delete(
                        CallLog.Calls.CONTENT_URI,
                        "number = 555555",null
                    )
                    Toast.makeText(context, "Borrado", Toast.LENGTH_SHORT).show()

                }else{
                        permissionState.launchPermissionRequest()
                }

             }) {
                Text(text = "Borrar 555-555", fontSize = 18.sp)
            }
        }

    }
}