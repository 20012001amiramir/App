package com.example.fu.ui.dashboard

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fu.data.network.response.GarbagesResponse
import com.example.fu.data.repository.GarbageRepository
import com.example.fu.util.ErrorHandler
import kotlinx.coroutines.launch
import javax.inject.Inject

@RequiresApi(Build.VERSION_CODES.N)
class ScanedGarbagesViewModel @Inject constructor(
    private val garbageRepository: GarbageRepository,
    private val errorHandler: ErrorHandler
): ViewModel() {

    private val _garbageScannedGarbagesState: MutableLiveData<GarbageViewState> = MutableLiveData(
        GarbageViewState.BlankViewState)
    val garbageScannedGarbagesState: LiveData<GarbageViewState> get() = _garbageScannedGarbagesState

    init {
        viewModelScope.launch {
            val gatGarbageFlow =
                garbageRepository
                    .getGarbagesScanedByAuthorizedUserFlow()

            gatGarbageFlow.collect{
                _garbageScannedGarbagesState.value = GarbageViewState.Data(it)
            }
        }
    }

    fun loadGarbagesInfo() {
        _garbageScannedGarbagesState.value = GarbageViewState.LoadingViewState
        viewModelScope.launch {
            try {
                garbageRepository.loadGarbagesScanedInfo()
            } catch (error: Throwable) {
                _garbageScannedGarbagesState.value = GarbageViewState.LoadingError(error)
                errorHandler.proceed(error)
            }
        }
    }

}
sealed class GarbageViewState {
    data class Data(val data: GarbagesResponse) : GarbageViewState()
    object LoadingViewState : GarbageViewState()
    object BlankViewState : GarbageViewState()
    data class LoadingError(val error: Throwable) : GarbageViewState()
}