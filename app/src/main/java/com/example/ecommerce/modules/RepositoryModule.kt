package com.example.ecommerce.modules

import com.example.ecommerce.repository.HomeRepository
import com.google.firebase.firestore.FirebaseFirestore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import javax.inject.Singleton

@InstallIn(ApplicationComponent::class)
@Module
class RepositoryModule {
    @Provides
    @Singleton
    public fun provideHomeRepoInstance(firebaseFirestore: FirebaseFirestore) = HomeRepository(firebaseFirestore)
}