package com.yuvarajcode.food_harbor.data

import android.net.Uri
import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.toObject
import com.yuvarajcode.food_harbor.domain.model.User
import com.yuvarajcode.food_harbor.domain.repository.UserRepository
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

class UserRepositoryImpl @Inject constructor(
    private val firestore : FirebaseFirestore,
    private val supabaseClient: SupabaseClient
) : UserRepository {
   private var  operationSuccessful = false
    override fun getUserDetails(userId: String): Flow<ResponseState<User>> = callbackFlow {
        ResponseState.Loading
        val snapShotListener = firestore.collection("users")
            .document(userId)
            .addSnapshotListener { snapshot, error ->
                val response = if (snapshot != null ) {
                    Log.d("UserRepositoryImpl", "getUserDetails: ${snapshot.data}")
                    val userDetails = User(
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
                        ngosDonated = snapshot.getLong("ngoDonated")?.toInt() ?: 0,
                        noOfDonations = snapshot.getLong("noOfDonations")?.toInt() ?: 0,
                    )
                    ResponseState.Success(userDetails)
                } else {
                    ResponseState.Error(error?.message ?: error.toString())
                }
                trySend(response).isSuccess
            }
        awaitClose {
            snapShotListener.remove()
        }
    }

    override fun setUserDetails(
        userId: String,
        username: String,
        email: String,
        password: String,
        profilePicture: String,
        phoneNumber: String,
        weeklyGoal: Int,
        monthlyGoal: Int,
        yearlyGoal: Int,
        missionStatement: String,
        name: String
    ): Flow<ResponseState<Boolean>> = flow {
        emit(ResponseState.Loading)
        operationSuccessful=false
        val picture = uploadUserThumbnail(Uri.parse(profilePicture), userId,profilePicture)
        try {
            val userObj = mutableMapOf<String,Any>()
            userObj["userName"] = username
            userObj["email"] = email
            userObj["password"] = password
            userObj["profilePictureUrl"] = picture
            userObj["phoneNumber"] = phoneNumber
            userObj["weeklyGoal"] = weeklyGoal
            userObj["monthlyGoal"] = monthlyGoal
            userObj["yearlyGoal"] = yearlyGoal
            userObj["missionStatement"] = missionStatement
            userObj["name"] = name
            firestore.collection("users")
                .document(userId)
                .update(userObj as Map<String, Any>).addOnSuccessListener {
                    operationSuccessful = true
                }.await()
            if(operationSuccessful){
                emit(ResponseState.Success(operationSuccessful))
            }
            else
                emit(ResponseState.Error("Edit error!!"))
        }catch (e : Exception){
            emit(ResponseState.Error(e.message ?:"An unexpected error occurred!"))
        }
    }

    private suspend fun uploadUserThumbnail(image : Uri, videoTitle : String,url : String) : String {
        if(url.substringBefore("public") != url){
            val path = url.substringAfterLast("/public/").substringAfter("/")
            supabaseClient.storage["user_images"]
                .delete(path)
        }
        val fileName = "user_${videoTitle}_${System.currentTimeMillis()}.jpg"
        val storageResult = supabaseClient.storage["user_images"]
            .upload(
                fileName,
                image,
                upsert = true
            )
        return supabaseClient.storage["user_images"]
            .publicUrl(fileName)
    }

    override suspend fun getProfilePic(userId: String): Flow<ResponseState<String>> =flow{
        emit(ResponseState.Loading)
        val profilePic = firestore.collection("users")
            .document(userId)
            .get().await().toObject<User>()?.profilePictureUrl
        emit(ResponseState.Success(profilePic?:""))
    }.catch {
        emit(ResponseState.Error(it.message ?: "An unexpected error occurred!"))
    }
}
