package net.iessochoa.pmdm.t11_ejemplointents.ui.screens.fotoscreen

import android.app.Application
import android.net.Uri
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import net.iessochoa.pmdm.t11_ejemplointents.R

class FotoViewModel() : ViewModel() {


    private val _uiState = MutableStateFlow(
        UiStateFoto(
            uri = null
        )
    )
    val uiState: StateFlow<UiStateFoto> = _uiState.asStateFlow()

    fun setUri(uri: Uri?){
        _uiState.value = _uiState.value.copy(uri = uri)

    }
}