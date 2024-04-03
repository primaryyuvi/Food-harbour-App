package com.yuvarajcode.food_harbor.data

import com.google.firebase.firestore.FirebaseFirestore
import com.yuvarajcode.food_harbor.domain.model.User
import com.yuvarajcode.food_harbor.domain.repository.UserRepository
import com.yuvarajcode.food_harbor.utilities.ResponseState
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
    private val firestore : FirebaseFirestore
) : UserRepository {
   private var  operationSuccessful = false
    override fun getUserDetails(userId: String): Flow<ResponseState<User>> = callbackFlow {
        ResponseState.Loading
        val snapShotListener = firestore.collection("users")
            .document(userId)
            .addSnapshotListener { snapshot, error ->
                val response = if (snapshot != null) {
                    val userDetails = snapshot.toObject(User::class.java)
                    ResponseState.Success<User>(userDetails!!)
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
        phoneNumber: String
    ): Flow<ResponseState<Boolean>> = flow {
        operationSuccessful=false
        try {
            val userObj = mutableMapOf<String,String>()
            userObj["userName"] = username
            userObj["email"] = email
            userObj["password"] = password
            userObj["profilePictureUrl"] = profilePicture
            userObj["phoneNumber"] = phoneNumber
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
}
