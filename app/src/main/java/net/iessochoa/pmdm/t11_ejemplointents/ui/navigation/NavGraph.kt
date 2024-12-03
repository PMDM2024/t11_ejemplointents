package net.iessochoa.pmdm.t11_ejemplointents.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import net.iessochoa.pmdm.t11_ejemplointents.ui.screens.IntentsAndPermissionsScreen
import net.iessochoa.pmdm.t11_ejemplointents.ui.screens.NavigationDrawerExample
import net.iessochoa.pmdm.t11_ejemplointents.ui.screens.PermisosLibreriaScreen
import net.iessochoa.pmdm.t11_ejemplointents.ui.screens.PermisosLibreriaSolicitudInmediataScreen
import net.iessochoa.pmdm.t11_ejemplointents.ui.screens.PermisosScreen
import net.iessochoa.pmdm.t11_ejemplointents.ui.screens.fotoscreen.CameraXView
import net.iessochoa.pmdm.t11_ejemplointents.ui.screens.fotoscreen.FotoScreen

@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        //pantalla de inicio
        startDestination = IntentsAndPermissionsScreenDestination//IntentsAndPermissionsScreenDestination
    ) {
        //ruta a la pantalla ListaPalabrasScreen. Pantalla inicial de la app
        composable<IntentsAndPermissionsScreenDestination> {
            IntentsAndPermissionsScreen(
                onClickPermisos = {
                    // Navegamos a la pantalla
                    navController.navigate(PermisosScreenDestination)
                },
                onClickPermisosLibreria = {
                    // Navegamos a la pantalla .
                    navController.navigate(PermisosLibreriaScreenDestination)

                },
                onClickPermisosLibreriaSolicitudInmediata = {
                    // Navegamos a la pantalla . P
                    navController.navigate(PermisosLibreriaSolicitudInmediataScreenDestination)
                },
                onClickNavigationDrawer = {
                    // Navegamos a la pantalla .
                    navController.navigate(NavigationDrawerScreenDestination)
                },
                onClickFoto = {
                    // Navegamos a la pantalla .
                    navController.navigate(FotoScreenDestination)
                }

            )
        }
        //ruta a la pantalla
        composable<PermisosScreenDestination> {
            PermisosScreen()
        }
        composable<PermisosLibreriaScreenDestination> {
            PermisosLibreriaScreen()
        }
        composable<PermisosLibreriaSolicitudInmediataScreenDestination> {
            PermisosLibreriaSolicitudInmediataScreen(
                onVolver = {
                    navController.popBackStack()
                }
            )
        }
        //Navigation Drawer
        composable<NavigationDrawerScreenDestination> {
            NavigationDrawerExample()
        }
        //fotos
        composable<FotoScreenDestination> {
            FotoScreen(
                /*
                Esta lambda nos permite recuperar el resultado de la pantalla CameraX.
                El funcionamiento:
               - Proporcionas un lambda (onResult) que espera el resultado.
               - Este lambda se almacena en el savedStateHandle de la FotoScreen cuando se navega a la pantalla CameraX.
               - En CameraXScreen recuperamos la lambda desde el savedStateHandle(previosBackStackEntry)
               - Cuando el usuario interactúa con CameraX, invocas este lambda para devolver el resultado.
                 */
                onClickCameraX =
                //onResult es el lambda que nos permite recuperar el resultado de la pantalla CameraX.
                { onResult ->
                    //Guardamos la lambda en el savedStateHandle de la pantalla actual para recuperarla en CameraXScreen
                    navController.currentBackStackEntry
                        ?.savedStateHandle
                        ?.set("onUriResult", onResult)
                    //navegamos a la pantalla CameraX
                    navController.navigate(CameraXDestination)
                }
                ,
                onVolver = {
                    navController.popBackStack()
                }
            )
        }
        //CameraX
        composable<CameraXDestination> {
            /*
            - Recuperamos la pantalla anterior (FotoScreen)
            - Recuperamos la lambda que nos permite recuperar el resultado de la pantalla CameraX
            - Navegamos a la pantalla CameraX
      */
            val parentBackStackEntry = navController.previousBackStackEntry
            val onResult = parentBackStackEntry
                ?.savedStateHandle
                ?.get<(String) -> Unit>("onUriResult")
            CameraXView(

                onResult = { result ->
                    onResult?.invoke(result)
                    navController.popBackStack()
                }
            )
        }


    }
}