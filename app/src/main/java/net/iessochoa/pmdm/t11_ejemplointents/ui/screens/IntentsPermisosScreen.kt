package net.iessochoa.pmdm.t11_ejemplointents.ui.screens

import android.content.Intent
import android.net.Uri
import android.provider.MediaStore
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.*

import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import kotlinx.coroutines.launch
import net.iessochoa.joseantoniolopez.tareas.ui.components.ActionItem
import net.iessochoa.joseantoniolopez.tareas.ui.components.AppBar
import net.iessochoa.pmdm.t11_ejemplointents.R

@Composable
fun IntentsAndPermissionsScreen(
    onClickPermisos: () -> Unit={},
    onClickPermisosLibreria: () -> Unit={},
    onClickPermisosLibreriaSolicitudInmediata: () -> Unit={},
    modifier: Modifier=Modifier
) {
    val context = LocalContext.current
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


    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        modifier = Modifier,
        topBar = {
            AppBar(
                tituloPantallaActual = "Intents y Permisos",
                //lista de acciones de la toolbar
                listaAcciones = ListaAccionesToolBar(muestraSnackBar),
                listaAccionesOverflow = ListaOverflowMenu(muestraSnackBar),
                puedeNavegarAtras = false
            )
        }

    ) { innerPadding ->

        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(innerPadding),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Intents y Permisos",
                fontSize = 30.sp,
                modifier = Modifier.padding(16.dp)
            )

            // Botón 1: Abrir URL
            Button(
                onClick = {
                    val url = "https://portal.edu.gva.es/03013224/"
                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                    context.startActivity(intent)
                    //debemos comprobar si existe una actividad que pueda manejar la acción
                    //pero  es necesario incluir el permiso de internet en el manifest
                    //puedes verlo en el manifest
                    /* if (intent.resolveActivity(context.packageManager) != null) {
                context.startActivity(intent)
            } else {
                Toast.makeText(context, "No se puede abrir la URL", Toast.LENGTH_SHORT).show()
            }*/
                }, modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
            ) {
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
                }, modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
            ) {
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
                }, modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
            ) {
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
                }, modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
            ) {
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
                }, modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
            ) {
                Text(stringResource(R.string.mandar_correo))
            }

            // Botón 6: Solicitar permisos
            Button(
                onClick = onClickPermisos,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
            )
            {
                Text("Permisos")
            }
            /* Botón 7: Solicitar permisos con libreria
            Tenemos una librería en desarrollo de Google que nos permite simplificar el código
            de petición de permisos
            https://github.com/google/accompanist/tree/main/permissions
            es necesario incluir en el gradle
             implementation ("com.google.accompanist:accompanist-permissions:0.36.0")*/
            Button(
                onClick = onClickPermisosLibreria,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
            )
            {
                Text("Permisos con libreria")
            }
            Button(
                onClick = onClickPermisosLibreriaSolicitudInmediata,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
            )
            {
                Text("Permisos:Petición Inmediata")
            }
        }
    }
}

/****
 * Crean las acciones de la toolbar
 *
 * puede que las acciones necesiten de los estados y estados  de la pantalla actual,
 * y sea necesario crear la lista en el mismo compose para crear las lambdas de las
 * acciones
 */
@Composable
private fun ListaAccionesToolBar(showSnackbar: (String, SnackbarDuration) -> Unit):List<ActionItem> {

        return listOf(
            ActionItem(
                "Buscar",
                Icons.Filled.Search,
                action = { showSnackbar("Buscar", SnackbarDuration.Short) }
            ),
            ActionItem(
                "Filtrar",
                //icono personalizado
                ImageVector.vectorResource(id = R.drawable.ic_filter),
                action = { showSnackbar("Filtrar", SnackbarDuration.Short) }
            )
        )
}

@Composable
private fun ListaOverflowMenu(showSnackbar: (String, SnackbarDuration) -> Unit):List<ActionItem> {

    return listOf(
        ActionItem(
            "Refrescar",
            icon= Icons.Filled.Refresh,
            action = { showSnackbar("Refrescar", SnackbarDuration.Short) }
        ),
        ActionItem(
            "Ajustes",
            icon=Icons.Default.Build,
            action = { showSnackbar("Ajustes",SnackbarDuration.Short) }
        ),
        ActionItem(
            "Enviar sugerencias",
            icon=Icons.Default.Send,
            action = { showSnackbar("Enviar sugerencias", SnackbarDuration.Short) }
        ),
        //accion sin icono
        ActionItem(
            "Ayuda",

            action = { showSnackbar("Ayuda", SnackbarDuration.Short) }
        ),
        ActionItem(
            "Cerrar sesión",
            action = { showSnackbar("Cerrar sesión", SnackbarDuration.Short) }
        )
    )


}
