package net.iessochoa.pmdm.t11_ejemplointents.ui.screens.fotoscreen

import android.Manifest
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.result.launch
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import kotlinx.coroutines.launch
import net.iessochoa.pmdm.t11_ejemplointents.utils.loadFromUri
import net.iessochoa.pmdm.t11_ejemplointents.utils.saveBitmapImage
import androidx.lifecycle.viewmodel.compose.viewModel
import coil3.compose.AsyncImage

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun FotoScreen(
    viewModel: FotoViewModel = viewModel(),
    onClickCameraX: () -> Unit={},
    onVolver: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    val uiState by viewModel.uiState.collectAsState()
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

    /*Configura el launcher para abrir la galería,
    Obtenemos una uri y realizamos una copia de la imagen
    */
    val launcherGaleria = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent(),
        onResult = { uri: Uri? ->
            //persistirPermisoUri(context, uri!!)
            //hacemos una copia de foto, ya que en las nuevas versiones solo nos deja acceso en esta sesión
            //lanzamos una corrutina para que no se bloquee el hilo principal
            scope.launch {
                val uriCopia = saveBitmapImage(context, loadFromUri(context, uri)!!)
                viewModel.setUri(uriCopia)

            }
        }
    )
    /*
    nos permita hacer una foto y recuperar una preview de la imagen
    En este caso realizamos una copia y la mostramos
     */
     val launcherPhoto = rememberLauncherForActivityResult(
         contract = ActivityResultContracts.TakePicturePreview(),
         onResult = { bitmap ->
             if (bitmap != null) {
                 scope.launch {
                     val uriCopia = saveBitmapImage(context, bitmap)
                     viewModel.setUri(uriCopia)

                 }
             }
         }
     )


    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        modifier = Modifier
    ) { innerPadding ->
        Column(
            modifier = modifier
                .padding(innerPadding)
                .fillMaxSize()
                .padding(16.dp),

            horizontalAlignment = Alignment.CenterHorizontally


        ) {
            Text(
                text = "Leer y hacer Foto",
                fontSize = 20.sp,
                modifier = Modifier.padding(4.dp)
            )
            Button(
                onClick = {
                    launcherGaleria.launch("image/*")
                }) {
                Text(text = "Seleccionar de la Galeria")
            }
            Button(
                onClick = {
                    launcherPhoto.launch()
                }) {
                Text(text = "Preview  foto")
            }
            Button(
                onClick = {
                    onClickCameraX()
                }) {
                Text(text = "CameraX")
            }

            // Mostrar la imagen seleccionada mediante Coil
            //esta librería nos carga la imagen en un hilo y está recomendada por
            //Google. Hay que incluir en el gradle
            //implementation("io.coil-kt.coil3:coil-compose:3.0.2")
            uiState.uri?.let { uri ->
                AsyncImage(
                    model = uri,
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp)
                        .clip(RoundedCornerShape(16.dp)) // Borde redondeado
                       // .border(2.dp, Color.Black, RoundedCornerShape(16.dp))
                )
            }
        }
    }
}