package dev.haqim.catapp.ui.screen.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import dev.haqim.catapp.data.mechanism.Resource
import dev.haqim.catapp.data.repository.CatRepository
import dev.haqim.catapp.domain.model.Cat
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch


class DetailCatViewModel(
    private val repository: CatRepository
) : ViewModel(){
    private val _uiState = MutableStateFlow(DetailCatUiState())
    val uiState = _uiState.stateIn(
        viewModelScope, SharingStarted.Eagerly, DetailCatUiState()
    )
    private val actionStateFlow = MutableSharedFlow<DetailCatUiAction>(
        extraBufferCapacity = 10,
        onBufferOverflow = BufferOverflow.DROP_OLDEST)
    init {
        actionStateFlow.updateStates().launchIn(viewModelScope)
    }

    /**
     * This is the only function exposed to the view to receive action
     */
    fun processAction(action: DetailCatUiAction) = actionStateFlow.tryEmit(action)

    private fun MutableSharedFlow<DetailCatUiAction>.updateStates() = onEach {
        when(it){
            is DetailCatUiAction.SetCatId -> {
                viewModelScope.launch {
                    _uiState.update { state -> state.copy(catId = it.value) }
                }
            }
            is DetailCatUiAction.OnLoad -> {
                viewModelScope.launch {
                    uiState.value.catId?.let { it ->
                        repository.getCatById(it).collect{ res ->
                            _uiState.update { state ->
                                state.copy(
                                    cat = res
                                )
                            }
                        }
                    } ?: _uiState.update { state -> state.copy(cat = Resource.Error("No selected ID")) }
                }
            }
            is DetailCatUiAction.SetToFavorite -> {
                viewModelScope.launch {
                    repository.setToFavorite(id = uiState.value.cat.data?.id!!, value = it.value)
                }
            }
        }
    }

    companion object{
        fun provideFactory(
            repository: CatRepository
        ): ViewModelProvider.Factory = object : ViewModelProvider.Factory{
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return DetailCatViewModel(repository) as T
            }
        }
    }

}

data class DetailCatUiState(
    val catId: String? = null,
    val cat: Resource<Cat?> = Resource.Loading(),
)

sealed class DetailCatUiAction{
    data class SetCatId(val value: String): DetailCatUiAction()
    object OnLoad: DetailCatUiAction()
    data class SetToFavorite(val value: Boolean): DetailCatUiAction()
}