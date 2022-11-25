package com.sample.shoppinglist.di

import com.sample.shoppinglist.data.repositories.DefaultShoppingRepository
import com.sample.shoppinglist.data.repositories.ShoppingRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface RepoModule {

    @Singleton
    @Binds
    fun provideShoppingRepository(
        defaultShoppingRepository: DefaultShoppingRepository
    ): ShoppingRepository

}