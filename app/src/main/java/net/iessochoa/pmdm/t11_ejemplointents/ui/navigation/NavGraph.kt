package net.iessochoa.pmdm.t11_ejemplointents.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import net.iessochoa.pmdm.t11_ejemplointents.ui.screens.IntentsAndPermissionsScreen
import net.iessochoa.pmdm.t11_ejemplointents.ui.screens.PermisosScreen

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    NavHost(
        navController = navController,
        //pantalla de inicio
        startDestination = IntentsAndPermissionsScreenDestination
    ) {
        //ruta a la pantalla ListaPalabrasScreen. Pantalla inicial de la app
        composable<IntentsAndPermissionsScreenDestination> {
            IntentsAndPermissionsScreen(
                onClickPermisos = {
                    // Navegamos a la pantalla PalabraScreen. Pasamos null porque es una nueva palabra
                    navController.navigate(PermisosScreenDestination)
                }
            )
        }
        //ruta a la pantalla
        composable<PermisosScreenDestination> {
            PermisosScreen()
        }
    }
}