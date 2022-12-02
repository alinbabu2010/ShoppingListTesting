package com.sample.shoppinglist.di

import android.content.Context
import androidx.room.Room
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.sample.shoppinglist.R
import com.sample.shoppinglist.data.local.ShoppingItemDatabase
import com.sample.shoppinglist.data.remote.PixabayAPI
import com.sample.shoppinglist.utils.Constants.BASE_URL
import com.sample.shoppinglist.utils.Constants.DATABASE_NAME
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun providesShoppingItemDatabase(
        @ApplicationContext context: Context
    ) = Room.databaseBuilder(
        context, ShoppingItemDatabase::class.java,
        DATABASE_NAME
    ).build()

    @Singleton
    @Provides
    fun providesShoppingDao(
        shoppingItemDatabase: ShoppingItemDatabase
    ) = shoppingItemDatabase.shoppingDao()


    @Singleton
    @Provides
    fun providesPixabayAPI() = Retrofit.Builder()
        .addConverterFactory(GsonConverterFactory.create())
        .baseUrl(BASE_URL).build().create(PixabayAPI::class.java)

    @Singleton
    @Provides
    fun provideGlideInstance(
        @ApplicationContext context: Context
    ) = Glide.with(context).setDefaultRequestOptions(
        RequestOptions()
            .placeholder(R.drawable.ic_image)
            .error(R.drawable.ic_image)
    )

}