package com.firestore

import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.QuerySnapshot

/**
 * An interface to read data from a remote backend
 */
interface ReadServerInterface {

    /**
     * Requests multiple users of a specified page size
     * @param page The page to start at
     * @param pageSize The number of users to return
     */
    suspend fun getAllDocuments(
        collection: String,
        page: DocumentSnapshot?,
        pageSize: Int,
    ): Result<QuerySnapshot>
}