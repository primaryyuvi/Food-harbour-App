package com.yuvarajcode.food_harbor.data

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.toObject
import com.yuvarajcode.food_harbor.domain.model.Donation
import com.yuvarajcode.food_harbor.domain.model.DonationStatus
import com.yuvarajcode.food_harbor.domain.model.OrgDonation
import com.yuvarajcode.food_harbor.domain.repository.OrganisationRepository
import com.yuvarajcode.food_harbor.utilities.ResponseState
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await

class OrganisationRepositoryImpl(
    private val firestore: FirebaseFirestore,
) : OrganisationRepository {
    override suspend fun getDonationsByOrgId(orgId: String): Flow<ResponseState<List<OrgDonation>>> =
        callbackFlow {
            ResponseState.Loading
            val snapshotListener = firestore.collection("organisations").document(orgId)
                .collection("donationsList")
                .whereEqualTo("rejected", false)
                .whereNotEqualTo("status", DonationStatus.TRANSACTIONCOMPLETE.status)
                .addSnapshotListener { snapshot, error ->
                    val response = if (snapshot != null) {
                        val donationList = snapshot.documents.map { doc ->
                            val donation = doc.toObject<OrgDonation>()
                            donation!!
                        }
                        ResponseState.Success(donationList)
                    } else {
                        ResponseState.Error(error?.message ?: "An unexpected error occurred")
                    }
                    trySend(response).isSuccess
                }
            awaitClose {
                snapshotListener.remove()
            }
        }.catch {
            emit(ResponseState.Error(it.message ?: "An unexpected error occurred"))
        }

    override suspend fun getDonationList(
        userId: String
    ): Flow<ResponseState<List<Donation>>> = callbackFlow {
        ResponseState.Loading
        Log.d("OrganisationImpl", "getDonationList: userId : ${userId}")
        var donateeList = emptyList<String>()

        val userListener = firestore.collection("organisations").document(userId)
            .collection("donationsList")
            .addSnapshotListener { snapshot, error ->
                if (snapshot != null) {
                    donateeList = snapshot.documents.mapNotNull { doc ->
                        doc.toObject<OrgDonation>()?.id
                    }
                }
                if (error != null) {
                    trySend(ResponseState.Error(error.message ?: "An unexpected error occurred"))
                    return@addSnapshotListener
                }
            }
        Log.d("OrganisationImpl", "getDonationsList: ${donateeList}")
        val snapshotListener = firestore.collection("donations")
            .whereNotEqualTo("status", DonationStatus.TRANSACTIONCOMPLETE.status)
            .addSnapshotListener { snapshot, error ->
                val response = if (snapshot != null) {
                    val donationList = snapshot.documents.mapNotNull { doc -> doc.toObject<Donation>() }
                        .filter { donation ->
                            Log.d("OrganisationImpl", "getDonationList: ${donation.id}")
                            donation.id !in donateeList
                        }
                    ResponseState.Success(donationList)
                } else {
                    ResponseState.Error(error?.message ?: "An unexpected error occurred")
                }
                trySend(response).isSuccess
            }
        awaitClose {
            snapshotListener.remove()
            userListener.remove()
        }
    }.catch {
        Log.e("OrganisationImpl", "getDonationList: ${it.message}")
        emit(ResponseState.Error(it.message ?: "An unexpected error occurred"))
    }

    override suspend fun getDonationDetails(donationId: String): Flow<ResponseState<Donation>> = callbackFlow {
            ResponseState.Loading
            val snapshotListener = firestore.collection("donations")
                .document(donationId)
                .addSnapshotListener { snapshot, error ->
                    val response = if (snapshot != null) {
                        val donation = snapshot.toObject<Donation>()
                        ResponseState.Success(donation ?: Donation())
                    } else {
                        ResponseState.Error(error?.message ?: "An unexpected error occurred")
                    }
                    trySend(response).isSuccess
                }
            awaitClose {
                snapshotListener.remove()
            }
        }.catch {
            emit(ResponseState.Error(it.message ?: "An unexpected error occurred"))
        }


    override suspend fun updateAcceptDonation(
        donationId: String,
        userId: String
    ): Flow<ResponseState<Boolean>> = flow {
        emit(ResponseState.Loading)
        firestore.collection("organisations")
            .document(userId)
            .collection("donationsList")
            .document(donationId)
            .update("accepted", true)
            .await()
        emit(ResponseState.Success(true))
    }.catch {
        emit(ResponseState.Error(it.message ?: "An unexpected error occurred"))
    }

    override suspend fun updateDonationStatus(
        donationId: String,
        status: String,
        userId: String,
    ): Flow<ResponseState<Boolean>> = flow {
        Log.d("DonationRepositoryImpl", "updateDonationStatus1: $status")
        Log.d("DonationRepositoryImpl", "updateDonationId: $donationId")
        Log.d("DonationRepositoryImpl", "updateUserId: $userId")
        emit(ResponseState.Loading)
        firestore.collection("donations")
            .document(donationId)
            .update("status", status)
            .await()
        firestore.collection("organisations")
            .document(userId)
            .collection("donationsList")
            .document(donationId)
            .update("status", status)
            .await()

        emit(ResponseState.Success(true))
    }.catch {
        emit(ResponseState.Error(it.message ?: "An unexpected error occurred"))
    }

    override suspend fun updateRejectDonation(
        donationId: String,
        userId: String
    ): Flow<ResponseState<Boolean>> = flow {
        emit(ResponseState.Loading)
        val orgDonationMap = hashMapOf(
            "id" to donationId,
            "status" to DonationStatus.PENDING.status,
            "rejected" to true,
            "accepted" to false
        )
        firestore.collection("organisations")
            .document(userId)
            .collection("donationsList")
            .document(donationId)
            .set(orgDonationMap)
            .await()
        firestore.runTransaction { transaction ->
            val donationRef = firestore.collection("donations").document(donationId)
            val donationSnapshot = transaction.get(donationRef)
            val donateeList =
                (donationSnapshot.get("donateeList") as? List<String>)?.toMutableList()
                    ?: mutableListOf()
            donateeList.remove(userId)
            transaction.update(donationRef, "donateeList", donateeList)
            if (donateeList.isEmpty()) {
                transaction.update(donationRef, "status", "Pending")
            }
        }.await()
        emit(ResponseState.Success(true))
    }.catch {
        emit(ResponseState.Error(it.message ?: "An unexpected error occurred"))
    }

    override suspend fun getOrgStatus(userId: String, orgId: String): Flow<ResponseState<String>> =
        flow {
            emit(ResponseState.Loading)
            val snapshot = firestore.collection("organisations")
                .document(userId)
                .collection("donationsList")
                .document(orgId)
                .get()
                .await()
            val status = snapshot.getString("status")!!
            emit(ResponseState.Success(status))
        }.catch {
            emit(ResponseState.Error(it.message ?: "An unexpected error occurred"))
        }

    override suspend fun addDonateeToDonation(
        donationId: String,
        userId: String
    ): Flow<ResponseState<Boolean>> = flow {
        emit(ResponseState.Loading)
        val orgDonationMap = hashMapOf(
            "id" to donationId,
            "status" to DonationStatus.ACCEPTEDBYORG.status,
            "rejected" to false,
            "accepted" to true
        )
        firestore.collection("donations")
            .document(donationId)
            .update("donateeList", FieldValue.arrayUnion(userId))
            .await()
        firestore.collection("donations")
            .document(donationId)
            .update("status", DonationStatus.ACCEPTEDBYORG.status)
            .await()
        firestore.collection("organisations")
            .document(userId)
            .collection("donationsList")
            .document(donationId)
            .set(orgDonationMap)
            .await()

        emit(ResponseState.Success(true))
    }.catch {
        emit(ResponseState.Error(it.message ?: "An unexpected error occurred"))
    }

    override suspend fun transactionCompleteStatus(
        donationId: String,
        userId: String,
        username : String
    ): Flow<ResponseState<Boolean>> = flow{
        emit(ResponseState.Loading)
        firestore.collection("organisations")
            .document(userId)
            .collection("donationsList")
            .document(donationId)
            .update("status", DonationStatus.TRANSACTIONCOMPLETE.status)
            .await()
        firestore.runTransaction {
            val donationRef = firestore.collection("donations").document(donationId)
            val donationSnapshot = it.get(donationRef)
            val quantity = donationSnapshot.getDouble("quantity")!!
            val donorId = donationSnapshot.getString("userId")!!
            val donorRef = firestore.collection("users").document(donorId)
            val donateeRef = firestore.collection("users").document(userId)
            it.update(donationRef,
                "status", DonationStatus.TRANSACTIONCOMPLETE.status,
                "acceptedBy", userId,
                "donateeName", username,
                "time", System.currentTimeMillis().toString(),
            )
            it.update(donorRef,
                "totalDonation", FieldValue.increment(quantity),
                "noOfDonations", FieldValue.increment(1),
                "ngoDonated", FieldValue.increment(1),
            )
            it.update(donateeRef,
                "totalDonation", FieldValue.increment(quantity),
                "noOfDonations", FieldValue.increment(1),
                "ngoDonated", FieldValue.increment(1),
            )
        }.await()
        emit(ResponseState.Success(true))
    }.catch {
        emit(ResponseState.Error(it.message ?: "An unexpected error occurred"))
    }

}