package com.yuvarajcode.food_harbor.di

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.yuvarajcode.food_harbor.data.AuthenticationRepositoryImpl
import com.yuvarajcode.food_harbor.data.DonationRepositoryImpl
import com.yuvarajcode.food_harbor.data.UserRepositoryImpl
import com.yuvarajcode.food_harbor.domain.repository.AuthenticationRepository
import com.yuvarajcode.food_harbor.domain.repository.UserRepository
import com.yuvarajcode.food_harbor.domain.usecases.authenticationUseCases.AuthenticationUseCases
import com.yuvarajcode.food_harbor.domain.usecases.authenticationUseCases.FireBaseSignIn
import com.yuvarajcode.food_harbor.domain.usecases.authenticationUseCases.FirebaseRegister
import com.yuvarajcode.food_harbor.domain.usecases.authenticationUseCases.FirebaseSignOut
import com.yuvarajcode.food_harbor.domain.usecases.authenticationUseCases.GetAuthState
import com.yuvarajcode.food_harbor.domain.usecases.authenticationUseCases.IsUserAuthenticated
import com.yuvarajcode.food_harbor.domain.usecases.userUseCases.GetUserDetails
import com.yuvarajcode.food_harbor.domain.usecases.userUseCases.SetUserDetails
import com.yuvarajcode.food_harbor.domain.usecases.userUseCases.UserUseCases
import com.yuvarajcode.food_harbor.data.NewsRepositoryImpl
import com.yuvarajcode.food_harbor.domain.repository.DonationRepository
import com.yuvarajcode.food_harbor.domain.repository.NewsApiService
import com.yuvarajcode.food_harbor.domain.repository.NewsRepository
import com.yuvarajcode.food_harbor.domain.usecases.DonationUseCases.AddDonation
import com.yuvarajcode.food_harbor.domain.usecases.DonationUseCases.DeleteDonation
import com.yuvarajcode.food_harbor.domain.usecases.DonationUseCases.DonationUseCases
import com.yuvarajcode.food_harbor.domain.usecases.DonationUseCases.GetDonationDetails
import com.yuvarajcode.food_harbor.domain.usecases.DonationUseCases.GetDonationList
import com.yuvarajcode.food_harbor.domain.usecases.DonationUseCases.GetDonationListByUserId
import com.yuvarajcode.food_harbor.domain.usecases.DonationUseCases.UpdateDonationStatus
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
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
    fun provideUserRepository(
        firestore: FirebaseFirestore,
    ): UserRepository {
        return UserRepositoryImpl(
            firestore,
        )
    }

    @Provides
    @Singleton
    fun provideOkHttpClient(): OkHttpClient {
        return OkHttpClient.Builder()
            .build()
    }

    @Provides
    @Singleton
    fun provideUserUseCases(
        userRepositoryImpl: UserRepository,
    ) = UserUseCases(
        getUserDetails = GetUserDetails(userRepositoryImpl),
        setUserDetails = SetUserDetails(userRepositoryImpl),
    )

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

    @Provides
    @Singleton
    fun provideDonationRepository(
        firestore: FirebaseFirestore,
    ): DonationRepository {
        return DonationRepositoryImpl(
            firestore,
        )
    }

    @Provides
    @Singleton
    fun provideDonationUseCases(
        donationRepositoryImpl: DonationRepository,
    ) = DonationUseCases(
        getDonationList = GetDonationList(donationRepositoryImpl),
        getDonationListByUserId = GetDonationListByUserId(donationRepositoryImpl),
        addDonation = AddDonation(donationRepositoryImpl),
        deleteDonation = DeleteDonation(donationRepositoryImpl),
        getDonationDetails = GetDonationDetails(donationRepositoryImpl),
        updateDonationStatus = UpdateDonationStatus(donationRepositoryImpl)
    )
}
