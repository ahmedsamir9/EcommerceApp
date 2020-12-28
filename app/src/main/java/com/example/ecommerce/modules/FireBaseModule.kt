package com.example.ecommerce.modules

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreSettings
import com.google.firebase.firestore.ktx.firestoreSettings
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import javax.inject.Singleton



@Module
@InstallIn(ApplicationComponent::class)
class FireBaseModule {
    @Provides
    @Singleton
    public fun proviedFireStoreInstance():FirebaseFirestore{
        val db = FirebaseFirestore.getInstance()
        val settings = firestoreSettings {
            isPersistenceEnabled = true
        }
        db.firestoreSettings = settings
        return db
    }
    @Provides
    @Singleton
    public fun proviedAuthInstance():FirebaseAuth= FirebaseAuth.getInstance()


}