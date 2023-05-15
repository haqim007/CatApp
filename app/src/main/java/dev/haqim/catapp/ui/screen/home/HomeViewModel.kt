package dev.haqim.catapp.ui.screen.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import dev.haqim.catapp.data.mechanism.Resource
import dev.haqim.catapp.data.repository.CatRepository
import dev.haqim.catapp.domain.model.Cat
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch


class HomeViewModel(
    private val repository: CatRepository
) : ViewModel(){
    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState = _uiState.stateIn(
        viewModelScope, SharingStarted.Eagerly, HomeUiState()
    )
    private val actionStateFlow = MutableSharedFlow<HomeUiAction>(
        extraBufferCapacity = 10,
        onBufferOverflow = BufferOverflow.DROP_OLDEST)
    init {
        actionStateFlow.updateStates().launchIn(viewModelScope)
        processAction(HomeUiAction.OnLoad)
    }

    /**
     * This is the only function exposed to the view to receive action
     */
    fun processAction(action: HomeUiAction) = actionStateFlow.tryEmit(action)

    private fun MutableSharedFlow<HomeUiAction>.updateStates() = onEach {
        when(it){
            is HomeUiAction.OnLoad -> {
                viewModelScope.launch {
                    repository.getCats(uiState.value.searchQuery).collect{
                        _uiState.update { state -> state.copy(cats = it) }
                    }
                }
            }
            is HomeUiAction.SetQuery -> {
                _uiState.update { state -> state.copy(searchQuery = it.query) }
            }
            is HomeUiAction.OpenCat -> {
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
                return HomeViewModel(repository) as T
            }
        }
    }

}

data class HomeUiState(
    val searchQuery: String = "",
    val cats: Resource<List<Cat>?> = Resource.Loading(),
    val openCatId: String? = null
)

sealed class HomeUiAction{
    object OnLoad: HomeUiAction()
    data class SetQuery(val query: String = ""): HomeUiAction()
    data class OpenCat(val id: String?): HomeUiAction()
}