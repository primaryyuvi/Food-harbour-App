package com.yuvarajcode.food_harbor.data

import android.net.Uri
import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore
import com.yuvarajcode.food_harbor.domain.model.Donation
import com.yuvarajcode.food_harbor.domain.model.DonationStatus
import com.yuvarajcode.food_harbor.domain.model.OrgDonation
import com.yuvarajcode.food_harbor.domain.model.User
import com.yuvarajcode.food_harbor.domain.repository.DonationRepository
import com.yuvarajcode.food_harbor.utilities.ResponseState
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.storage.storage
import io.github.jan.supabase.storage.upload
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class DonationRepositoryImpl @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val supabaseClient: SupabaseClient
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
        status: String,
        userId: String,
        id: String,
        additionalDetails: String,
        email: String,
        username: String
    ): Flow<ResponseState<Boolean>> = flow {
        emit(ResponseState.Loading)
        val images = mutableListOf<String>()
        for (image in imageUrl){
            val picture = uploadDonationImages(Uri.parse(image), id)
            images.add(picture)
        }
        try {
            val donation = Donation(
                name = name,
                description = description,
                quantity = quantity,
                imageUrl = images,
                location = location,
                contact = contact,
                entryDate = entryDate,
                expiryDate = expiryDate,
                time = time,
                status = "Pending",
                id = id,
                userId = userId,
                donateeList = emptyList(),
                additionalDetailsOnLocation = additionalDetails,
                email = email,
                username = username
            )
            firestore.collection("donations").document(id).set(donation).await()

//            val orgDonation = OrgDonation(
//                id = id,
//                accepted = false,
//                rejected = false,
//                status = status
//            )
//
//            val organisationIds = firestore.collection("organisations").get()
//                .await().documents.map { it.id }
//            organisationIds.forEach { orgId ->
//                firestore.collection("organisations").document(orgId)
//                    .collection("donationsList").document(id).set(orgDonation).await()
//            }

            emit(ResponseState.Success(true))
        } catch (e: Exception) {
            emit(ResponseState.Error(e.message ?: "An unexpected error occurred!"))
        }
    }.catch { e ->
        emit(ResponseState.Error(e.message ?: "An unexpected error occurred!"))
    }

    private suspend fun uploadDonationImages(image : Uri, id : String) : String {
        val fileName = "donation_${id}_${System.currentTimeMillis()}.jpg"
        val storageResult = supabaseClient.storage["donation_images"]
            .upload(
                fileName,
                image,
                upsert = true
            )
        return supabaseClient.storage["donation_images"]
            .publicUrl(fileName)
    }

    override suspend fun getDonationList(): Flow<ResponseState<List<Donation>>> = callbackFlow {
        ResponseState.Loading
        val snapshotListener = firestore.collection("donations")
            .whereEqualTo("status", DonationStatus.TRANSACTIONCOMPLETE.status)
            .addSnapshotListener { snapshot, error ->
                val response = if (snapshot != null) {

                    val donationList = snapshot.documents.map {
                        Log.d("DonationRepositoryImpl", "getDonationList: ${it.id}")
                        Donation(
                            id = it.id,
                            name = it.getString("name") ?: "",
                            description = it.getString("description") ?: "",
                            quantity = it.getDouble("quantity") ?: 0.0,
                            imageUrl = it.get("imageUrl") as? List<String> ?: emptyList(),
                            location = it.getString("location") ?: "",
                            contact = it.getString("contact") ?: "",
                            entryDate = it.getString("entryDate") ?: "",
                            expiryDate = it.getString("expiryDate") ?: "",
                            time = it.getString("time") ?: "",
                            status = it.getString("status") ?: "",
                            userId = it.getString("userId") ?: "",
                            donateeList = it.get("donateeList") as? List<String> ?: emptyList(),
                            acceptedBy = it.getString("acceptedBy") ?: "",
                            additionalDetailsOnLocation = it.getString("additionalDetailsOnLocation") ?: "",
                            email = it.getString("email") ?: "",
                            username = it.getString("username") ?: "",
                            donateeName = it.getString("donateeName") ?: ""
                        )
                    }
                    Log.d("DonationRepositoryImpl", "getDonationList: $donationList")
                    ResponseState.Success(donationList)
                } else {
                    ResponseState.Error(error?.message ?: error.toString())
                }
                trySend(response).isSuccess
            }
        awaitClose {
            snapshotListener.remove()
        }
    }.catch {
        emit(ResponseState.Error(it.message ?: "An unexpected error occurred"))
    }

    override suspend fun getDonationListByUserId(userId: String): Flow<ResponseState<List<Donation>>> =
        callbackFlow {
            ResponseState.Loading
            val snapshotListener = firestore.collection("donations")
                .whereEqualTo("userId", userId)
                .whereNotEqualTo("status", DonationStatus.TRANSACTIONCOMPLETE.status)
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
        }.catch {
            emit(ResponseState.Error(it.message ?: "An unexpected error occurred"))
        }


    override suspend fun deleteDonation(id: String): Flow<ResponseState<Boolean>> = flow<ResponseState<Boolean>> {
        emit(ResponseState.Loading)
        val organisationIds = firestore.collection("organisations").get()
            .await().documents.map { it.id }
        organisationIds.forEach { orgId ->
            firestore.collection("organisations").document(orgId)
                .collection("donationsList").document(id).delete().await()
        }
        firestore.collection("donations")
            .document(id)
            .delete()
            .await()
    }.catch {
        emit(ResponseState.Error(it.message ?: "An unexpected error occurred"))
    }

    override suspend fun donateToOrg(
        id: String,
        status: String,
        orgId: String
    ): Flow<ResponseState<Boolean>> = flow {
        emit(ResponseState.Loading)
        Log.d("DonationRepositoryImpl", "updateDonationStatus1: $status")
        firestore.collection("donations")
            .document(id)
            .update("status", status)
            .await()
        firestore.collection("organisations")
            .document(orgId)
            .collection("donationsList")
            .document(id)
            .update("status", status)
            .await()
        firestore.collection("donations")
            .document(id)
            .update("acceptedBy", orgId)
            .await()
        emit(ResponseState.Success(true))
    }.catch{
        emit(ResponseState.Error(it.message ?: "An unexpected error occurred"))
    }



    override suspend fun getDonationHistory(userId: String): Flow<ResponseState<List<Donation>>> = callbackFlow {
        ResponseState.Loading
        val snapshotListener = firestore.collection("donations")
            .whereEqualTo("userId", userId)
            .whereEqualTo("status", DonationStatus.TRANSACTIONCOMPLETE.status)
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
    }.catch {
        emit(ResponseState.Error(it.message ?: "An unexpected error occurred"))
    }

    override suspend fun getDonationStatus(id: String): Flow<ResponseState<String>> = callbackFlow{
        ResponseState.Loading
        val snapshotListener = firestore.collection("donations")
            .document(id)
            .addSnapshotListener { snapshot, error ->
                val response = if(snapshot != null){
                    val status = snapshot.getString("status")
                    Log.d("DonationRepositoryImpl", "getDonationStatus: $status")
                    ResponseState.Success(status?:"")
                }else{
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

    override suspend fun acceptDonateeStatus(
        donationId: String,
        status: String,
        name: String
    ): Flow<ResponseState<Boolean>> = flow{
        emit(ResponseState.Loading)
        firestore.collection("donations")
            .document(donationId)
            .update("status", status)
            .await()
        firestore.collection("organisations")
            .document(name)
            .collection("donationsList")
            .document(donationId)
            .update("status", status)
            .await()
        firestore.collection("organisations")
            .document(name)
            .collection("donationsList")
            .document(donationId)
            .update("accepted", true)
            .await()
        emit(ResponseState.Success(true))
    }.catch {
        emit(ResponseState.Error(it.message ?: "An unexpected error occurred"))
    }

    override suspend fun rejectDonateeStatus(
        donationId: String,
        status: String,
        orgId: String
    ): Flow<ResponseState<Boolean>>  = flow{
        emit(ResponseState.Loading)
        firestore.collection("organisations")
            .document(orgId)
            .collection("donationsList")
            .document(donationId)
            .update("status", status)
            .await()
        firestore.runTransaction { transaction ->
            val donationRef = firestore.collection("donations").document(donationId)
            val donationSnapshot = transaction.get(donationRef)
            val donateeList =
                (donationSnapshot.get("donateeList") as? List<String>)?.toMutableList()
                    ?: mutableListOf()
            donateeList.remove(orgId)
            transaction.update(donationRef, "donateeList", donateeList)
            if (donateeList.isEmpty()) {
                transaction.update(donationRef, "status", "Pending")
            }
        }.await()
        emit(ResponseState.Success(true))
    }.catch {
        emit(ResponseState.Error(it.message ?: "An unexpected error occurred"))
    }

    override suspend fun getUserById(userId: String): Flow<ResponseState<User>> = callbackFlow {
        ResponseState.Loading
        val snapshotListener = firestore.collection("users")
            .document(userId)
            .addSnapshotListener { snapshot, error ->
                val response = if(snapshot != null){
                    val user = User(
                        userId = snapshot.id,
                        name = snapshot.getString("name") ?: "",
                        userName = snapshot.getString("userName") ?: "",
                        profilePictureUrl = snapshot.getString("profilePictureUrl") ?: "",
                        password = snapshot.getString("password") ?: "",
                        email = snapshot.getString("email") ?: "",
                        isUser = snapshot.getBoolean("isUser") ?: false,
                        phoneNumber = snapshot.getString("phoneNumber") ?: "",
                        weeklyGoal = snapshot.getLong("weeklyGoal")?.toInt() ?: 0,
                        monthlyGoal = snapshot.getLong("monthlyGoal")?.toInt() ?: 0,
                        yearlyGoal = snapshot.getLong("yearlyGoal")?.toInt() ?: 0,
                        missionStatement = snapshot.getString("missionStatement") ?: "",
                        totalDonation = snapshot.getLong("totalDonation")?.toInt() ?: 0,
                        ngosDonated = snapshot.getLong("ngosDonated")?.toInt() ?: 0,
                        noOfDonations = snapshot.getLong("noOfDonations")?.toInt() ?: 0,
                    )
                    ResponseState.Success(user)
                }else{
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

    override suspend fun getUsernamesByUserIds(userIds: List<String>): Flow<ResponseState<List<Pair<String,String>>>> = flow {
        emit(ResponseState.Loading)
        val usernames = mutableListOf<Pair<String,String>>()
        for (userId in userIds){
            val userSnapshot = firestore.collection("users")
                .document(userId)
                .get()
                .await()
            val username = userSnapshot.getString("userName")
            Log.d("DonationRepositoryImpl", "getUsernamesByUserIds: $username")
            if (username != null){
                usernames.add(userId to username)
            }
        }
        emit(ResponseState.Success(usernames))
    }.catch {
        emit(ResponseState.Error(it.message ?: "An unexpected error occurred"))
    }

    override suspend fun getDonationHistoryOrg(orgId: String): Flow<ResponseState<List<Donation>>> = callbackFlow{
        ResponseState.Loading
        val snapshotListener = firestore.collection("donations")
            .whereEqualTo("acceptedBy", orgId)
            .whereEqualTo("status", DonationStatus.TRANSACTIONCOMPLETE.status)
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
    }.catch {
        emit(ResponseState.Error(it.message ?: "An unexpected error occurred"))
    }


}

