package com.suji.domain

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.DocumentSnapshot
import com.pagination.FirestorePaging
import com.repository.Repository
import com.suji.domain.mappers.toAthlete
import com.suji.model.ConnectionStatus
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import timber.log.Timber
import javax.inject.Inject

interface AthletesPagingInterface{
    val isLoading : StateFlow<Boolean>
    val isRefreshing : StateFlow<Boolean>
    val endReached : StateFlow<Boolean>
    val error : StateFlow<String>
    suspend fun requestNextPage()
    suspend fun reset()

}

data class AthletesPagingState(
    var page: DocumentSnapshot? = null,
    val _isLoading : MutableStateFlow<Boolean> = MutableStateFlow(false),
    val _isRefreshing : MutableStateFlow<Boolean> = MutableStateFlow(false),
    val _endReached : MutableStateFlow<Boolean> = MutableStateFlow(false),
    val _error : MutableStateFlow<String> = MutableStateFlow("")
)

class AthletesPaging @Inject constructor(
    repository: Repository
) : AthletesPagingInterface {

    // Paging State
    private val state by mutableStateOf(AthletesPagingState())

    //Data Sources
    private val firestore = repository.readServerInterface
    private val connectedSujiDevices = repository.connectedSujiDevicesInterface

    //Public State
    override val isLoading : StateFlow<Boolean> = state._isLoading
    override val isRefreshing : StateFlow<Boolean> = state._isRefreshing
    override val endReached : StateFlow<Boolean> = state._endReached
    override val error : StateFlow<String> = state._error


    private val pager = FirestorePaging(
        initialKey = state.page,
        onRequest = { nextPage ->
            firestore.getAllDocuments(
                "athletes",
                page = nextPage,
                pageSize = 10
            )
        },
        onLoadUpdated = {
            state._isLoading.value = it
        },
        onSuccess = { list, newKey ->
            state._isRefreshing.value = false
            state.page = newKey
            state._endReached.value = list.documents.size < 10
            for (document in list.documents) {
                val athlete = document.toAthlete()
                connectedSujiDevices.addAthlete(athlete)
            }
        },
        onError = { error ->
            if (error != null) {
                state._error.value = error.message!!
            }
        },
        getNextKey = { list ->
            list.lastOrNull()
        },
    )

    override suspend fun requestNextPage(){
        pager.loadNextItems()
    }

    override suspend fun reset(){
        state._isRefreshing.value = true
        state.page = null
        state._endReached.value = false
        connectedSujiDevices.sujiAthleteMapFlow.value.forEach { athleteDevice ->
            if (athleteDevice.value?.connectionStatus != ConnectionStatus.CONNECTED) {
                connectedSujiDevices.removeAthlete(athleteDevice.key)
            }
        }
        pager.reset()
        pager.loadNextItems()
    }
}