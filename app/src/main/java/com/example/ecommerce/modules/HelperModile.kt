package com.example.ecommerce.modules

import android.content.Context
import android.content.SharedPreferences
import com.example.ecommerce.utils.NetworkHelper
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Singleton

@Module
@InstallIn(ApplicationComponent::class)
class HelperModule {
    @Provides
    @Singleton
    fun provideSharedPerfInstance(@ApplicationContext ctx:Context)= ctx.getSharedPreferences("Boxo",Context.MODE_PRIVATE)
    @Provides
    @Singleton
    fun provideNetWorkHelper(@ApplicationContext ctx:Context)= NetworkHelper(ctx)
}