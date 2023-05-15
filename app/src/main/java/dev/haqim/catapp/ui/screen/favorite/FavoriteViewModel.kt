package dev.haqim.catapp.ui.screen.favorite

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import dev.haqim.catapp.data.mechanism.Resource
import dev.haqim.catapp.data.repository.CatRepository
import dev.haqim.catapp.domain.model.Cat
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch


class FavoriteViewModel(
    private val repository: CatRepository
) : ViewModel(){
    private val _uiState = MutableStateFlow(FavoriteUiState())
    val uiState = _uiState.stateIn(
        viewModelScope, SharingStarted.Eagerly, FavoriteUiState()
    )
    private val actionStateFlow = MutableSharedFlow<FavoriteUiAction>(
        extraBufferCapacity = 10,
        onBufferOverflow = BufferOverflow.DROP_OLDEST)
    init {
        actionStateFlow.updateStates().launchIn(viewModelScope)
        processAction(FavoriteUiAction.OnLoad)
    }

    /**
     * This is the only function exposed to the view to receive action
     */
    fun processAction(action: FavoriteUiAction) = actionStateFlow.tryEmit(action)

    private fun MutableSharedFlow<FavoriteUiAction>.updateStates() = onEach {
        when(it){
            is FavoriteUiAction.OnLoad -> {
                viewModelScope.launch {
                    repository.getFavoriteCats(uiState.value.searchQuery).collect{
                        _uiState.update { state -> state.copy(cats = it) }
                    }
                }
            }
            is FavoriteUiAction.SetQuery -> {
                _uiState.update { state -> state.copy(searchQuery = it.query) }
            }
            is FavoriteUiAction.OpenCat -> {
                _uiState.update { state -> state.copy(openCatId = it.id) }
            }
        }
    }

    companion object{
        fun provideFactory(
            repository: CatRepository
        ): ViewModelProvider.Factory = object : ViewModelProvider.Factory{
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return FavoriteViewModel(repository) as T
            }
        }
    }

}

data class FavoriteUiState(
    val searchQuery: String = "",
    val cats: Resource<List<Cat>?> = Resource.Loading(),
    val openCatId: String? = null
)

sealed class FavoriteUiAction{
    object OnLoad: FavoriteUiAction()
    data class SetQuery(val query: String = ""): FavoriteUiAction()
    data class OpenCat(val id: String?): FavoriteUiAction()
}