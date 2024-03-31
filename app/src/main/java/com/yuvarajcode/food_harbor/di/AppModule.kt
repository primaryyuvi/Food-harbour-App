package com.yuvarajcode.food_harbor.di

import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import com.yuvarajcode.food_harbor.data.AuthenticationRepositoryImpl
import com.yuvarajcode.food_harbor.data.NewsRepositoryImpl
import com.yuvarajcode.food_harbor.domain.repository.AuthenticationRepository
import com.yuvarajcode.food_harbor.domain.repository.NewsApiService
import com.yuvarajcode.food_harbor.domain.repository.NewsRepository
import com.yuvarajcode.food_harbor.domain.usecases.AuthenticationUseCases
import com.yuvarajcode.food_harbor.domain.usecases.FireBaseSignIn
import com.yuvarajcode.food_harbor.domain.usecases.FirebaseRegister
import com.yuvarajcode.food_harbor.domain.usecases.FirebaseSignOut
import com.yuvarajcode.food_harbor.domain.usecases.GetAuthState
import com.yuvarajcode.food_harbor.domain.usecases.IsUserAuthenticated
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideFirebaseAuthentication(): FirebaseAuth {
        return FirebaseAuth.getInstance()
    }

    @Provides
    @Singleton
    fun provideFirebaseFireStore(): FirebaseFirestore {
        return FirebaseFirestore.getInstance()
    }

    @Provides
    @Singleton
    fun provideFirebaseStorage(): FirebaseStorage {
        return FirebaseStorage.getInstance()
    }

    @Provides
    @Singleton
    fun provideAuthenticationRepository(
        firebaseAuth: FirebaseAuth,
        firebaseFirestore: FirebaseFirestore,
    ): AuthenticationRepository {
        return AuthenticationRepositoryImpl(
            firebaseAuth,
            firebaseFirestore,
        )
    }

    @Provides
    @Singleton
    fun provideAuthUseCases(
        authenticationRepositoryImpl: AuthenticationRepository,
    ) = AuthenticationUseCases(
        isUserAuthenticated = IsUserAuthenticated(authenticationRepositoryImpl),
        getAuthState = GetAuthState(authenticationRepositoryImpl),
        fireBaseSignIn = FireBaseSignIn(authenticationRepositoryImpl),
        fireBaseSignOut = FirebaseSignOut(authenticationRepositoryImpl),
        firebaseRegister = FirebaseRegister(authenticationRepositoryImpl),
    )

    @Provides
    @Singleton
    fun provideOkHttpClient(): OkHttpClient {
        return OkHttpClient.Builder()
            .build()
    }

    @Provides
    @Singleton
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl("https://newsapi.org/v2/")
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    fun provideApiService(retrofit: Retrofit): NewsApiService {
        return retrofit.create(NewsApiService::class.java)
    }

    @Provides
    @Singleton
    fun provideNewsRepository(newsApiService: NewsApiService): NewsRepository {
        return NewsRepositoryImpl(newsApiService)
    }
}
