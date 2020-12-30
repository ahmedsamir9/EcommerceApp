package com.example.ecommerce.modules

import android.content.SharedPreferences
import com.example.ecommerce.repository.*
import com.example.ecommerce.utils.NetworkHelper
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import javax.inject.Singleton


@Module
@InstallIn(ApplicationComponent::class)
class RepositoryModule {
    @Provides
    @Singleton
    public fun provideHomeRepoInstance(firebaseFirestore: FirebaseFirestore,firebaseAuth: FirebaseAuth) = HomeRepository(firebaseFirestore,firebaseAuth)
    @Provides
    @Singleton
    public fun provideCategoeyRepoInstance(firebaseFirestore: FirebaseFirestore) = CategoryRepo(firebaseFirestore)
    @Provides
    @Singleton
    public fun provideProductRepoInstance(firebaseFirestore: FirebaseFirestore,firebaseAuth: FirebaseAuth,networkHelper: NetworkHelper) = ProductRepo(firebaseFirestore,firebaseAuth,networkHelper)
    @Provides
    @Singleton
    public fun provideMapRepoInstance(firebaseFirestore: FirebaseFirestore) = MapRepo(firebaseFirestore)
    @Provides
    @Singleton
    public fun provideCheckOutRepoInstance(firebaseFirestore: FirebaseFirestore,firebaseAuth: FirebaseAuth,networkHelper: NetworkHelper) = CheckoutRepo(firebaseFirestore,firebaseAuth,networkHelper)
@Provides
    @Singleton
    public fun provideProfileRepoInstance(firebaseFirestore: FirebaseFirestore,firebaseAuth: FirebaseAuth) = ProfileRepo(firebaseFirestore,firebaseAuth)
    @Provides
    @Singleton
    public fun provideSearchRepoInstance(firebaseFirestore: FirebaseFirestore) = SearchRepo(firebaseFirestore)
    @Provides
    @Singleton
    public fun provideLoginRepoInstance(firebaseAuth: FirebaseAuth,sharedPerfrance :SharedPreferences) =LoginRepo(sharedPerfrance,firebaseAuth)
    @Provides
    @Singleton
    public fun provideSignRepoInstance(firebaseAuth: FirebaseAuth,sharedPerfrance :SharedPreferences,firebaseFirestore: FirebaseFirestore) =SignInRepo(sharedPerfrance,firebaseAuth,firebaseFirestore)

}