package com.firestore

import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.QuerySnapshot
import kotlinx.coroutines.delay

/**
 * Reads data from firebase firestore database
 * @property firestore The instance of Firestore database
 */
class FireStoreRead : ReadServerInterface {

    private val firestore = FirebaseFirestore.getInstance()

    /**
     * Retrieves a specified number of documents from a Firestore database
     * @param pageSize How many athletes to return at a time
     * @param page The athlete to start from in the list
     */
    override suspend fun getAllDocuments(
        collection : String,
        page: DocumentSnapshot?,
        pageSize: Int,
    ): Result<QuerySnapshot> {
        var result: Result<QuerySnapshot>? = null
        val startTime = System.currentTimeMillis()
        val firestoreCollection = firestore.collection(collection)

        val firestoreReference = if (page == null) {
            firestoreCollection
        } else {
            firestoreCollection.startAfter(page)
        }

        firestoreReference
            .limit(pageSize.toLong())
            .get()
            .addOnSuccessListener { userDocuments ->
                result = Result.success(userDocuments)
            }
            .addOnFailureListener {
                result = Result.failure(it)
            }

        while (result == null) {
            if (timeout(startTime)) {
                return Result.failure(Exception())
            }
            delay(1000)
        }

        return result as Result<QuerySnapshot>
    }


}


fun timeout(startTime: Long): Boolean {
    return System.currentTimeMillis() - 10000 > startTime
}
