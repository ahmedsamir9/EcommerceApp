package com.example.ecommerce

import android.app.Application
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.firestoreSettings
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class app :Application() {
    override fun onCreate() {
        super.onCreate()
        val settings = firestoreSettings {
            isPersistenceEnabled = true
        }

    }
}