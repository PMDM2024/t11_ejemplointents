package net.iessochoa.joseantoniolopez.tareas.ui.components

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp

/**
 * Composable que define la barra de navegación superior de la app
 * @param tituloPantallaActual pantalla actual. Permite mostrar el titulo correspondiente.
 * @param puedeNavegarAtras indica si se puede navegar hacia atrás. La pantalla de inicio no puede tener navegación hacia atrás
 * @param navegaAtras acción de navegación hacia atrás. Lambda que se ejecuta al pulsar el botón de navegación hacia atrás
 * @param listaAcciones lista de acciones a mostrar en la barra de navegación.
 * @param listaAccionesOverflow lista de acciones a mostrar en el overflow menu.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppBar(
    tituloPantallaActual: String,
    puedeNavegarAtras: Boolean,
    navegaAtras: () -> Unit = {},
    listaAcciones: List<ActionItem> = listOf(),
    listaAccionesOverflow: List<ActionItem> = listOf(),
    modifier: Modifier = Modifier
) {
    TopAppBar(
        //Recuperamos el título del enum AppScreen
        title = { Text(text = tituloPantallaActual) },
        colors = TopAppBarDefaults.mediumTopAppBarColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer
        ),
        modifier = modifier,
        navigationIcon = {
            //si es la primera pantalla no se muestra el botón de navegación
            if (puedeNavegarAtras) {
                //lambda que iría a la pantalla anterior
                IconButton(onClick = navegaAtras) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        //recuerda que el texto tiene que ir en string.xml
                        contentDescription = "Ir atrás"
                    )
                }
            }
        },
        //Acciones de la barra de navegación
        actions = {
            if (listaAcciones.isNotEmpty())
                menuAction(listaAcciones)

            //overflow menu
            val (isExpanded, setExpanded) = remember { mutableStateOf(false) }
            if (listaAccionesOverflow.isNotEmpty())
                OverflowMenuAction(isExpanded, setExpanded, listaAccionesOverflow)
        }
    )
}

/**
 * Nos permite crear las acciones del menú de la TopAppBar
 */
data class ActionItem(
    val name: String,
    //los elementos del menú overflow pueden no tener icono
    val icon: ImageVector? = null,
    val action: () -> Unit,
    //val order: Int
)

/**
 * Acciones de la barra de navegación.
 */
@Composable
private fun menuAction(
    options: List<ActionItem>
) {
    options.forEach { // (2) Crear action items desde lista
        IconButton(onClick = it.action) {
            Icon(imageVector = it.icon!!, contentDescription = it.name)
        }
    }
}

/**
 * Acciones del overflow menu
 */
@Composable
private fun OverflowMenuAction(
    expanded: Boolean,
    setExpanded: (Boolean) -> Unit,
    options: List<ActionItem>
) {
    IconButton(onClick = { setExpanded(true) }) {
        Icon(imageVector = Icons.Filled.MoreVert, contentDescription = "Ver más")

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { setExpanded(false) },
            offset = DpOffset(x = 0.dp, y = 4.dp)
        ) {
            options.forEach { option ->
                DropdownMenuItem(
                    text = { //si tiene icono
                        if (option.icon != null) {
                            Row(
                                verticalAlignment = androidx.compose.ui.Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = option.icon,
                                    contentDescription = option.name,
                                    modifier = Modifier.scale(0.75f)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(text = option.name)
                            }
                        }else//sin icono
                            Text(text = option.name)
                           },
                    onClick = {
                        option.action()
                        setExpanded(false)
                    }
                )
            }
        }
    }
}

