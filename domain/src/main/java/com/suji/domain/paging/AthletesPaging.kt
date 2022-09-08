package com.suji.domain

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import com.google.firebase.firestore.DocumentSnapshot
import com.pagination.FirestorePaging
import com.repository.Repository
import com.suji.domain.connectedSujiDevices.SujiDeviceManager
import com.suji.domain.mappers.toAthlete
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

/**
 * @property isLoading contains most recent loading state
 * @property isRefreshing contains most recent refreshing state
 * @property endReached contains true if the final page has been retrieved
 * @property error contains most recent error message
 * @property requestNextPage requests the next page of data
 * @property reset resets the paging
 */
interface AthletesPagingInterface{
    val isLoading : StateFlow<Boolean>
    val isRefreshing : StateFlow<Boolean>
    val endReached : StateFlow<Boolean>
    val error : StateFlow<String>
    suspend fun requestNextPage()
    suspend fun reset()
}

/**
 * Manages paging of data for athletes list
 * @param page The next page to collect from the athletes collection
 * @param _isLoading contains the most recent loading state
 * @param _isRefreshing contains the most recent refresh state
 * @param _endReached contains true if the final page  has been retrieved
 * @param _error contains most recent error message
 */
data class AthletesPagingState(
    var page: DocumentSnapshot? = null,
    val _isLoading : MutableStateFlow<Boolean> = MutableStateFlow(false),
    val _isRefreshing : MutableStateFlow<Boolean> = MutableStateFlow(false),
    val _endReached : MutableStateFlow<Boolean> = MutableStateFlow(false),
    val _error : MutableStateFlow<String> = MutableStateFlow("")
)

class AthletesPaging @Inject constructor(
    repository: Repository,
    private val connectedSujiDevices: SujiDeviceManager
) : AthletesPagingInterface {

    // Paging State
    private val state by mutableStateOf(AthletesPagingState())

    //Data Sources
    private val firestore = repository.readServerInterface

    //Public access to state
    override val isLoading : StateFlow<Boolean> = state._isLoading
    override val isRefreshing : StateFlow<Boolean> = state._isRefreshing
    override val endReached : StateFlow<Boolean> = state._endReached
    override val error : StateFlow<String> = state._error

    //Variables for collecting of data
    private val pageSize = 5
    private val collectionName = "athletes"


    private val pager = FirestorePaging(
        initialKey = state.page,
        onRequest = { nextPage ->
            firestore.getAllDocuments(
                collectionName,
                page = nextPage,
                pageSize = pageSize
            )
        },
        onLoadUpdated = {
            state._isLoading.value = it
        },
        onSuccess = { list, newKey ->
            state._isRefreshing.value = false
            state.page = newKey
            state._endReached.value = list.documents.size < pageSize
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
        connectedSujiDevices.clearAthletes()
        pager.reset()
        pager.loadNextItems()
    }
}