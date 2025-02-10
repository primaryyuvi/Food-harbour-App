package com.yuvarajcode.food_harbor.di

import android.content.Context
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.yuvarajcode.food_harbor.R
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
import com.yuvarajcode.food_harbor.data.OrganisationRepositoryImpl
import com.yuvarajcode.food_harbor.domain.repository.DonationRepository
import com.yuvarajcode.food_harbor.domain.repository.NewsApiService
import com.yuvarajcode.food_harbor.domain.repository.NewsRepository
import com.yuvarajcode.food_harbor.domain.repository.OrganisationRepository
import com.yuvarajcode.food_harbor.domain.repository.StreamApi
import com.yuvarajcode.food_harbor.domain.usecases.DonationUseCases.AddDonation
import com.yuvarajcode.food_harbor.domain.usecases.DonationUseCases.DeleteDonation
import com.yuvarajcode.food_harbor.domain.usecases.DonationUseCases.DonationUseCases
import com.yuvarajcode.food_harbor.domain.usecases.DonationUseCases.GetDonationHistory
import com.yuvarajcode.food_harbor.domain.usecases.DonationUseCases.GetDonationList
import com.yuvarajcode.food_harbor.domain.usecases.DonationUseCases.GetDonationListByUserId
import com.yuvarajcode.food_harbor.domain.usecases.DonationUseCases.UpdateDonationStatus
import com.yuvarajcode.food_harbor.presentation.main.chat.StreamTokenProvider
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import io.getstream.chat.android.client.ChatClient
import io.getstream.chat.android.client.logger.ChatLogLevel
import io.getstream.chat.android.offline.model.message.attachments.UploadAttachmentsNetworkType
import io.getstream.chat.android.offline.plugin.configuration.Config
import io.getstream.chat.android.offline.plugin.factory.StreamOfflinePluginFactory
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.annotations.SupabaseInternal
import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.storage.Storage
import io.ktor.client.plugins.websocket.WebSockets
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
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

    @OptIn(SupabaseInternal::class)
    @Provides
    @Singleton
    fun provideSupabaseClient(): SupabaseClient = createSupabaseClient(
        supabaseUrl = "https://xkoabiacftqcxxnswtvd.supabase.co",
        supabaseKey = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6Inhrb2FiaWFjZnRxY3h4bnN3dHZkIiwicm9sZSI6ImFub24iLCJpYXQiOjE3MzkxNzUzNDYsImV4cCI6MjA1NDc1MTM0Nn0.tShcXCxenK_nZ2Kj3pN7cNU0FdL3Rji3ZeL4Okhqy2Q"
    ) {
        install(Storage)
        httpConfig {
            this.install(WebSockets)
        }
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
        supabaseClient: SupabaseClient
    ): UserRepository {
        return UserRepositoryImpl(
            firestore,
            supabaseClient
        )
    }

    private val logging = HttpLoggingInterceptor().apply { level = HttpLoggingInterceptor.Level.BODY }
    @Provides
    @Singleton
    fun provideOkHttpClient(): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(logging)
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
        supabaseClient: SupabaseClient
    ): DonationRepository {
        return DonationRepositoryImpl(
            firestore,
            supabaseClient
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
        updateDonationStatus = UpdateDonationStatus(donationRepositoryImpl),
        getDonationHistory = GetDonationHistory(donationRepositoryImpl),
    )

    

    @Provides
    fun provideOfflinePluginFactory(@ApplicationContext context: Context) =
        StreamOfflinePluginFactory(
            config = Config(
                backgroundSyncEnabled = true,
                userPresence = true,
                persistenceEnabled = true,
                uploadAttachmentsNetworkType = UploadAttachmentsNetworkType.NOT_ROAMING,
            ),
            appContext = context,
        )


    @Provides
    @Singleton
    fun provideChatClient(@ApplicationContext context: Context , offlinePluginFactory : StreamOfflinePluginFactory) =
        ChatClient.Builder(context.getString(R.string.chat_api_key), context)
            .withPlugin(offlinePluginFactory)
            .logLevel(ChatLogLevel.ALL)
            .build()


    @Provides
    @Singleton
    fun provideOrganisationRepository(
        firestore: FirebaseFirestore
    ) : OrganisationRepository {
        return OrganisationRepositoryImpl(
            firestore
        )
    }
}
