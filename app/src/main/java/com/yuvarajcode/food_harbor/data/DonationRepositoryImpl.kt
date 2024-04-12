package com.yuvarajcode.food_harbor.data

import com.google.firebase.firestore.FirebaseFirestore
import com.yuvarajcode.food_harbor.domain.model.Donation
import com.yuvarajcode.food_harbor.domain.repository.DonationRepository
import com.yuvarajcode.food_harbor.utilities.ResponseState
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class DonationRepositoryImpl @Inject constructor(
    private val firestore: FirebaseFirestore,
) : DonationRepository {
    private var operationSuccessful = false
    override suspend fun addDonation(
        name: String,
        description: String,
        quantity: Double,
        imageUrl: List<String>,
        location: String,
        contact: String,
        entryDate: String,
        expiryDate: String,
        time: String,
        status: Boolean?,
        userId: String
    ): Flow<ResponseState<Boolean>> = flow {
        operationSuccessful = false
        try {
            emit(ResponseState.Loading)
            val id = firestore.collection("donations").document().id
            val donation = Donation(
                name = name,
                description = description,
                quantity = quantity,
                imageUrl = imageUrl,
                location = location,
                contact = contact,
                entryDate = entryDate,
                expiryDate = expiryDate,
                time = time,
                status = status,
                id = id,
                userId = userId
            )
            firestore.collection("donations").document(id).set(donation).addOnSuccessListener {
                operationSuccessful = true
            }.await()
            if (operationSuccessful) {
                emit(ResponseState.Success(operationSuccessful))
            } else {
                emit(ResponseState.Error("Donation not added!!"))
            }
        } catch (e: Exception) {
            emit(ResponseState.Error(e.message ?: "An unexpected error occurred!"))
        }
    }

    override suspend fun getDonationList(): Flow<ResponseState<List<Donation>>> = callbackFlow {
        ResponseState.Loading
        val snapshotListener = firestore.collection("donations")
            .addSnapshotListener { snapshot, error ->
                val response = if (snapshot != null) {
                    val donationList = snapshot.toObjects(Donation::class.java)
                    ResponseState.Success(donationList)
                } else {
                    ResponseState.Error(error?.message ?: error.toString())
                }
                trySend(response).isSuccess
            }
        awaitClose {
            snapshotListener.remove()
        }
    }

    override suspend fun getDonationListByUserId(userId: String): Flow<ResponseState<List<Donation>>> =
        callbackFlow {
            ResponseState.Loading
            val snapshotListener = firestore.collection("donations")
                .whereEqualTo("userId", userId)
                .addSnapshotListener { snapshot, error ->
                    val response = if (snapshot != null) {
                        val donationList = snapshot.toObjects(Donation::class.java)
                        ResponseState.Success(donationList)
                    } else {
                        ResponseState.Error(error?.message ?: error.toString())
                    }
                    trySend(response).isSuccess
                }
            awaitClose {
                snapshotListener.remove()
            }
        }

    override suspend fun getDonationDetails(
        userid: String,
        donationId: String
    ): Flow<ResponseState<Donation>> = callbackFlow {
        ResponseState.Loading
        val snapshotListener = firestore.collection("donations")
            .whereEqualTo("userId", userid)
            .whereEqualTo("id", donationId)
            .addSnapshotListener { snapshot, error ->
                val response = if (snapshot != null) {
                    val donation = snapshot.toObjects(Donation::class.java)
                    ResponseState.Success(donation[0])
                } else {
                    ResponseState.Error(error?.message ?: error.toString())
                }
                trySend(response).isSuccess
            }
        awaitClose {
            snapshotListener.remove()
        }
    }

    override suspend fun deleteDonation(id: String): Flow<ResponseState<Boolean>> = flow {
        emit(ResponseState.Loading)
        operationSuccessful = false
        try {
            firestore.collection("donations")
                .document(id)
                .delete()
                .addOnSuccessListener {
                    operationSuccessful = true
                }.await()
            if (operationSuccessful) {
                emit(ResponseState.Success(operationSuccessful))
            } else {
                emit(ResponseState.Error("Deletion error!!"))
            }
        } catch (e: Exception) {
            emit(ResponseState.Error(e.message ?: "An unexpected error occurred!"))
        }
    }

    override suspend fun updateDonationStatus(
        id: String,
        status: Boolean?
    ): Flow<ResponseState<Boolean>> = flow {
        emit(ResponseState.Loading)
        operationSuccessful = false
        try {
            firestore.collection("donations")
                .document(id)
                .update("status", status)
                .addOnSuccessListener {
                    operationSuccessful = true
                }.await()
            if (operationSuccessful) {
                emit(ResponseState.Success(operationSuccessful))
            } else {
                emit(ResponseState.Error("Update error!!"))
            }
        } catch (e: Exception) {
            emit(ResponseState.Error(e.message ?: "An unexpected error occurred!"))
        }
    }
}

