package net.iessochoa.pmdm.t11_ejemplointents.ui.screens


import android.Manifest
import android.provider.CallLog
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState


/**
 * Función composable para gestionar permisos de acceso al registro de llamadas.

/Tenemos una librería en desarrollo de Google que nos permite simplificar el código
 *         //de petición de permisos
 *         //https://github.com/google/accompanist/tree/main/permissions
 *         //es necesario incluir en el gradle
 *         // implementation ("com.google.accompanist:accompanist-permissions:0.36.0")
 *
 *         En este caso, solicitamos el permiso cuando se abre la pantalla y si no se ha concedido,
 *         el permiso salimos
 * @param onVolver Función opcional que se ejecuta al presionar un botón para volver (no implementado en este caso).
 */
@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun PermisosLibreriaSolicitudInmediataScreen(
    modifier: Modifier = Modifier,
    onVolver: () -> Unit = {}
) {


    val context = LocalContext.current
    //nos permite pedir el permiso y manejar la lógica de permisos
    val permissionState = rememberPermissionState(Manifest.permission.WRITE_CALL_LOG)
    //mostramos en pantalla si tenemos el permiso
    var estadoPermiso =
        if (permissionState.status.isGranted) "Permiso concedido" else "Permiso denegado"
    //nos permite solicitar al sistema el permiso
    val requestPermissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (!isGranted) {
            onVolver()
        }
    }
    //iniciamos la petición de permiso
    LaunchedEffect(true) {
        if (!permissionState.status.isGranted)
            requestPermissionLauncher.launch(Manifest.permission.WRITE_CALL_LOG)

    }
    Scaffold(

        modifier = Modifier
    ) { innerPadding ->
        Column(
            modifier = modifier.padding(innerPadding),

            ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally, // Centra los elementos horizontalmente
                // verticalArrangement = Arrangement.Center // Centra verticalmente
            ) {
                Spacer(modifier = Modifier.padding(16.dp))
                Text(
                    text = "Permisos en Android con Librería: Petición inmediata",
                    fontSize = 20.sp,
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.padding(100.dp))

// Muestra el estado del permiso.
                Text(text = estadoPermiso)
                // Botón debajo del texto
                Button(onClick = {
                    //si el permiso está concedido borramos la llamada, en otro caso mostramos un mensaje
                    if (permissionState.status.isGranted) {

                        borrarLlamada(context)

                        //explicamos el uso del permiso
                    }


                }) {
                    Text(text = "Borrar 555-555", fontSize = 18.sp)
                }

            }

        }
    }
}
