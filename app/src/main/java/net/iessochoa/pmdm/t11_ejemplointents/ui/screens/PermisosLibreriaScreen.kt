package net.iessochoa.pmdm.t11_ejemplointents.ui.screens

import android.Manifest
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
import com.google.accompanist.permissions.shouldShowRationale
import kotlinx.coroutines.launch
import net.iessochoa.joseantoniolopez.tareas.ui.components.DialogoDeConfirmacion


/**
* Función composable para gestionar permisos de acceso al registro de llamadas.

/Tenemos una librería en desarrollo de Google que nos permite simplificar el código
*         //de petición de permisos
*         //https://github.com/google/accompanist/tree/main/permissions
*         //es necesario incluir en el gradle
*         // implementation ("com.google.accompanist:accompanist-permissions:0.36.0")
* @param onVolver Función opcional que se ejecuta al presionar un botón para volver (no implementado en este caso).
*/
@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun PermisosLibreriaScreen(
    modifier: Modifier = Modifier
) {
    // Estado para manejar la visualización del Snackbar.
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()
    //permite mostrar el snackBar
    val muestraSnackBar: (String, SnackbarDuration) -> Unit = { mensaje, duration ->
        scope.launch {
            snackbarHostState.showSnackbar(
                message = mensaje,
                duration = duration
            )
        }
    }

    // Control de visibilidad del cuadro de diálogo de confirmación.
    var mostrarDialogo by remember { mutableStateOf(false) }

    val context = LocalContext.current
    //nos permite pedir el permiso y manejar la lógica de permisos
    val permissionState = rememberPermissionState(Manifest.permission.WRITE_CALL_LOG)
    var estadoPermiso =
            if (permissionState.status.isGranted) "Permiso concedido" else "Permiso denegado"



    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
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

                Text(
                    text = "Permisos en Android con Librería",
                    fontSize = 30.sp,
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
                        muestraSnackBar("Llamadas borradas",SnackbarDuration.Short )
                    //explicamos el uso del permiso
                    } else if(permissionState.status.shouldShowRationale){
                        mostrarDialogo = true
                    }
                    else{
                        mostrarDialogo = true
                    }


                }) {
                    Text(text = "Borrar 555-555", fontSize = 18.sp)
                }
                // Dialogo de confirmación si el permiso no está concedido.
                if (mostrarDialogo) {
                    DialogoDeConfirmacion(
                        onDismissRequest = {
                            mostrarDialogo = false
                            muestraSnackBar(
                                "Sin los permisos necesarios no se puede borrar el registro. Concede los permisos desde las opciones de la aplicación.",
                                SnackbarDuration.Long
                            )

                        },
                        onConfirmation = {
                            mostrarDialogo = false
                            //solicitamos el permiso
                           permissionState.launchPermissionRequest()
                        },
                        dialogTitle = "Atención",
                        dialogText = "Para poder borrar un registro de llamadas necesitas conceder el permiso.",
                        icon = Icons.Default.Info
                    )
                }
            }

        }
    }
}
