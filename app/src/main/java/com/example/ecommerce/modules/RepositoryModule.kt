package com.example.ecommerce.modules

import com.example.ecommerce.repository.*
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
    public fun provideProductRepoInstance(firebaseFirestore: FirebaseFirestore,firebaseAuth: FirebaseAuth) = ProductRepo(firebaseFirestore,firebaseAuth)
    @Provides
    @Singleton
    public fun provideCheckOutRepoInstance(firebaseFirestore: FirebaseFirestore,firebaseAuth: FirebaseAuth) = CheckoutRepo(firebaseFirestore,firebaseAuth)
    @Provides
    @Singleton
    public fun provideSearchRepoInstance(firebaseFirestore: FirebaseFirestore) = SearchRepo(firebaseFirestore)
}