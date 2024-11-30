package net.iessochoa.pmdm.t11_ejemplointents.ui.screens

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.provider.CallLog
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import net.iessochoa.pmdm.t11_ejemplointents.ui.DialogoDeConfirmacion

/**
 * Función composable para gestionar permisos de acceso al registro de llamadas.
 *
 * @param onVolver Función opcional que se ejecuta al presionar un botón para volver (no implementado en este caso).
 */
@Composable
fun PermisosScreen(
    onVolver: () -> Unit = {}
) {
    // Contexto de la actividad actual.
    val context = LocalContext.current


    // Variable que indica si el permiso está concedido.
    var granted by remember {
        mutableStateOf(
            ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.WRITE_CALL_LOG
            ) == PackageManager.PERMISSION_GRANTED
        )
    }

    // Estado del permiso para mostrar al usuario.
    var estadoPermiso by remember {
        mutableStateOf(
            if (granted) "Permiso concedido" else "Permiso denegado"
        )
    }

    // Estado para manejar la visualización del Snackbar.
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()
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

    // Lanza la solicitud del permiso y actualiza el estado según la respuesta.
    val permissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
        //si el permiso está concedido borramos la llamada, en otro caso mostramos un mensaje
    ) { granted ->
        estadoPermiso = if (granted) "Permiso concedido" else "Permiso denegado"
        if (granted)
            borrarLlamada(context)

    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        modifier = Modifier
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            horizontalAlignment = Alignment.CenterHorizontally,

            ) {

            Text(
                text = "Permisos en Android",
                fontSize = 30.sp,
            )
            Spacer(modifier = Modifier.padding(100.dp))
            // Muestra el estado del permiso.
            Text(text = estadoPermiso)

            // Botón para intentar borrar el registro de llamadas.
            Button(onClick = {
                granted = ContextCompat.checkSelfPermission(
                    context,
                    Manifest.permission.WRITE_CALL_LOG
                ) == PackageManager.PERMISSION_GRANTED
                //si el permiso está concedido borramos la llamada, en otro caso mostramos un mensaje
                if (granted) {
                    borrarLlamada(context)
                    muestraSnackBar( "Llamadas borradas", SnackbarDuration.Short)

                } else {
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
                        permissionLauncher.launch(Manifest.permission.WRITE_CALL_LOG)
                    },
                    dialogTitle = "Atención",
                    dialogText = "Para poder borrar un registro de llamadas necesitas conceder el permiso.",
                    icon = Icons.Default.Info
                )
            }
        }
    }

}

/**
 * Borra un registro de llamadas específico con el número '555555'.
 *
 * @param context Contexto de la actividad desde donde se accede al registro de llamadas.
 */
fun borrarLlamada(context: Context) {
    context.contentResolver.delete(
        CallLog.Calls.CONTENT_URI,
        "number = 555555",
        null
    )
}

/**
 * muestra el snackBar
 */
/*fun mostrarSnackBar2(
    scope: CoroutineScope,
    snackbarHostState: SnackbarHostState,
    mensaje: String,
    duration: SnackbarDuration = SnackbarDuration.Short
) {
    scope.launch {
        snackbarHostState.showSnackbar(
            message = mensaje,
            duration = duration
        )
    }
}*/

