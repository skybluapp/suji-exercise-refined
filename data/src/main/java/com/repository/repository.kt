package com.repository

import com.firestore.ReadServerInterface
import javax.inject.Inject

/**
 * Provides interfaces to the application to access data from sources
 * @param readServerInterface interface to read data from the server
 */
data class Repository @Inject constructor(
    val readServerInterface: ReadServerInterface,
)