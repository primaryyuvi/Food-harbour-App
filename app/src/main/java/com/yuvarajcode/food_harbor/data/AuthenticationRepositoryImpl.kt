package com.yuvarajcode.food_harbor.data

import android.util.Log
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.yuvarajcode.food_harbor.domain.model.User
import com.yuvarajcode.food_harbor.domain.repository.AuthenticationRepository
import com.yuvarajcode.food_harbor.utilities.ResponseState
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class AuthenticationRepositoryImpl @Inject constructor(
    private val auth :FirebaseAuth,
    private val firestore: FirebaseFirestore
) : AuthenticationRepository {
    override fun isUserAuthenticated(): Boolean {
        return auth.currentUser != null
    }

    override fun getAuthState(): Flow<Boolean> = callbackFlow {
        val authStateListener = FirebaseAuth.AuthStateListener {
            trySend(
                auth.currentUser == null
            )
        }
        auth.addAuthStateListener(authStateListener)
        awaitClose {
            auth.removeAuthStateListener(authStateListener)
        }
    }

    override fun firebaseSignIn(email: String, password: String): Flow<ResponseState<Boolean>> =
        flow {
                emit(ResponseState.Loading)
                Log.d("FirebaseSignIn", "firebaseSignIn,before: ${auth.currentUser}")
                val result = auth.signInWithEmailAndPassword(email, password).await()
                Log.d("FirebaseSignIn", "firebaseSignIn,after: ${result.user?.email}")
                Log.d("FirebaseSignIn", "firebaseSignIn,after: ${auth.currentUser?.email}")
                emit(ResponseState.Success(true))
        }.catch  {
            emit(ResponseState.Error(it.message ?: "An unexpected error occurred"))
        }


    override fun firebaseSignOut(): Flow<ResponseState<Boolean>> = flow {
        emit(ResponseState.Loading)
        auth.signOut()
        auth.currentUser?.delete()
        val result = auth.currentUser
        Log.d("FirebaseSignOut", "firebaseSignOut: $result")
        emit(ResponseState.Success(true))
    }.catch {
        emit(ResponseState.Error(it.message ?: "An unexpected error occurred"))
    }

    override fun firebaseRegister(
        name: String,
        username: String,
        phoneNumber: String,
        email: String,
        password: String,
        isUser: Boolean
    ): Flow<ResponseState<Boolean>> = flow {
        try {
            emit(ResponseState.Loading)
            val authResult = auth.createUserWithEmailAndPassword(email, password).await()
            val userId = authResult.user?.uid
            if (userId != null) {
                val user = mutableMapOf<String,Any>()
                user["userId"] = userId
                user["name"] = name
                user["userName"] = username
                user["profilePictureUrl"] = ""
                user["password"] = password
                user["email"] = email
                user["phoneNumber"] = phoneNumber
                user["isUser"] = isUser
                firestore.collection("users").document(userId).set(user as Map<String, Any>).await()
                if(!isUser){
                    firestore.collection("organisations").document(userId).set(user as Map<String,Any>).await()
                }
                emit(ResponseState.Success(true))
            } else {
                emit(ResponseState.Error("User ID is null after registration"))
            }
        } catch (e: Exception) {
            emit(ResponseState.Error(e.localizedMessage ?: "An unexpected error occurred"))
        }
    }
}