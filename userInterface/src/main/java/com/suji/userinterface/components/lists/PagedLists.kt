package com.suji.userinterface.components.lists

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.SwipeRefreshState
import com.suji.userinterface.theme.dimensions

/**
 * A generic list of content that can request additional content when the list has been scrolled to the bottom
 * and refreshes when swiped down from the top
 * @param Heading composable content to display at the top of the scrollable ist
 * @param list A list of content to populate the list
 * @param endReached True if the end of the content has been reached
 * @param isLoading True if a request for more content is in progress
 * @param content Takes an item in the list and converts it in to a composable function
 * @param swipeState the current state of swipe-to-refresh
 * @param refresh A function to perform when the user swipes to refresh
 */
@Composable
fun <E> PagingList(
    Heading: @Composable () -> Unit,
    list: List<E>,
    endReached: Boolean,
    isLoading: Boolean,
    loadNextPage: () -> Unit,
    content: @Composable (E) -> Unit,
    swipeState: SwipeRefreshState,
    refresh: () -> Unit,
    placeholderContent : @Composable () -> Unit
) {
    val size = list.size
    SwipeRefresh(
        state = swipeState,
        onRefresh = { refresh() }) {
        LazyColumn(modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.spacedBy(dimensions.small)) {
            item {
                Heading()
            }
            if (list.isNotEmpty()) {
                items(list.size) { index ->
                    if (list.isNotEmpty()) {
                        val data: E = list[index]
                        if (index >= size - 1 && !endReached && !isLoading) {
                            loadNextPage()
                        }
                        content(data)
                    }
                }
            }
            item {
                if (isLoading) {
                    Row(
                        horizontalArrangement = Arrangement.Center,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(verticalArrangement = Arrangement.spacedBy(dimensions.small)) {
                            placeholderContent()
                        }
                    }
                }
            }
        }
    }
}