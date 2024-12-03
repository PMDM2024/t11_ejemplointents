package net.iessochoa.pmdm.t11_ejemplointents.ui.screens.fotoscreen


import android.content.ContentValues
import android.content.Context
import android.net.Uri
import android.os.Environment
import android.provider.MediaStore
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.result.launch
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import kotlinx.coroutines.launch
import net.iessochoa.pmdm.t11_ejemplointents.utils.loadFromUri
import net.iessochoa.pmdm.t11_ejemplointents.utils.saveBitmapImage
import androidx.lifecycle.viewmodel.compose.viewModel

import coil3.compose.AsyncImage
import com.google.accompanist.permissions.rememberMultiplePermissionsState

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun FotoScreen(
    viewModel: FotoViewModel = viewModel(),
    onClickCameraX: (onResult: (String) -> Unit) -> Unit = {},
    onVolver: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    //Permisos: petición de permisos múltiples
    val permissionState = rememberMultiplePermissionsState(
        permissions = mutableListOf(
            android.Manifest.permission.CAMERA
        ).apply {
            if (android.os.Build.VERSION.SDK_INT <= android.os.Build.VERSION_CODES.P) {
                add(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                add(android.Manifest.permission.READ_EXTERNAL_STORAGE)
            }
        }

    )
    //solicitamos los permisos al inicio
    LaunchedEffect(key1 = Unit) {
        if (!permissionState.allPermissionsGranted)
            permissionState.launchMultiplePermissionRequest()
    }
    //estado
    val uiState by viewModel.uiState.collectAsState()
    // Estado para manejar la visualización del Snackbar.
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()




    val context = LocalContext.current


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
    val launcherPhotoPreview = rememberLauncherForActivityResult(
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
    /*
    permite pedir una foto y guardarla en una uri
     */
    var uriTemp:Uri?=null
    val launcherPhoto = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicture(),
        onResult = { esCorrecto ->
            if (esCorrecto ) {
                scope.launch {
                    viewModel.setUri(uriTemp)
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
                    launcherPhotoPreview.launch()
                }) {
                Text(text = "Preview  foto")
            }
            Button(
                onClick = {
                    uriTemp=creaUri(context)
                    uriTemp?.let {launcherPhoto.launch(it)}
                }
            ){
                Text(text = "Pedir foto Camara")
            }

            Button(
                onClick = {
                    onClickCameraX{uri ->
                        viewModel.setUri(Uri.parse(uri))
                    }
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
/**
 * permite crear una uri para guardar la imagen
 * */
fun creaUri(context: Context): Uri? {
    val resolver = context.contentResolver
    val contentValues = ContentValues().apply {
        put(MediaStore.MediaColumns.DISPLAY_NAME, "photo_${System.currentTimeMillis()}.jpg")
        put(MediaStore.MediaColumns.MIME_TYPE, "image/jpeg")
        put(MediaStore.Images.ImageColumns.RELATIVE_PATH, Environment.DIRECTORY_PICTURES)
    }
    return resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)
}