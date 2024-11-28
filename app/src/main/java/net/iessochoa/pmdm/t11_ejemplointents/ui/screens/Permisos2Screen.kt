package net.iessochoa.pmdm.t11_ejemplointents.ui.screens

import android.provider.CallLog
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import com.google.accompanist.permissions.isGranted

@Composable
fun Permisos2Screen(
    onVolver:()->Unit
) {
    val context = LocalContext.current
    var estadoPermiso by remember {mutableStateOf("No se ha pedido permiso")}
    val permissionLauncher =
        rememberLauncherForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { granted ->
            estadoPermiso = if (granted) "Permiso concedido" else "Permiso denegado"

        }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally, // Centra los elementos horizontalmente
    ){
        Button(onClick = {
            val granted= ContextCompat.checkSelfPermission(
                context,
                android.Manifest.permission.WRITE_CALL_LOG
            ) == android.content.pm.PackageManager.PERMISSION_GRANTED

            if (granted){
                context.getContentResolver().delete(
                    CallLog.Calls.CONTENT_URI,
                    "number = 555555",null
                )
                Toast.makeText(context, "Borrado", Toast.LENGTH_SHORT).show()

            }else{
                permissionLauncher.launch(android.Manifest.permission.WRITE_CALL_LOG)
            }

        }) {
            Text(text = "Borrar 555-555", fontSize = 18.sp)
        }
            }
    }
