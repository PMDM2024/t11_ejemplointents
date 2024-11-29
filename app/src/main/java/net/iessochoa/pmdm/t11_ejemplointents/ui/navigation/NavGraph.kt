package net.iessochoa.pmdm.t11_ejemplointents.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import net.iessochoa.pmdm.t11_ejemplointents.ui.screens.IntentsAndPermissionsScreen
import net.iessochoa.pmdm.t11_ejemplointents.ui.screens.PermisosLibreriaScreen
import net.iessochoa.pmdm.t11_ejemplointents.ui.screens.PermisosLibreriaSolicitudInmediataScreen
import net.iessochoa.pmdm.t11_ejemplointents.ui.screens.PermisosScreen

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
                    // Navegamos a la pantalla PalabraScreen. Pasamos null porque es una nueva palabra
                    navController.navigate(PermisosScreenDestination)
                },
                onClickPermisosLibreria = {
                    // Navegamos a la pantalla PalabraScreen. Pasamos null porque es una nueva palabra
                    navController.navigate(PermisosLibreriaScreenDestination)

                },
                onClickPermisosLibreriaSolicitudInmediata = {
                    // Navegamos a la pantalla PalabraScreen. Pasamos null porque es una nueva palabra
                    navController.navigate(PermisosLibreriaSolicitudInmediataScreenDestination)
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

    }
}