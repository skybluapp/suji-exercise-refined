package com.repository

import com.connectedSujiDevices.ConnectedSujiDevices
import com.connectedSujiDevices.ConnectedSujiDevicesInterface
import com.firestore.ReadServerInterface
import javax.inject.Inject

/**
 * Provides interfaces to the application to access data from a variety of sources
 * @param readServerInterface interface to read data from the server
 */
data class Repository @Inject constructor(
    val readServerInterface: ReadServerInterface,
    val connectedSujiDevicesInterface: ConnectedSujiDevicesInterface
)