package uz.adkhamjon.mobileprogrammingproject.di.module

import android.content.Context
import com.chuckerteam.chucker.api.ChuckerInterceptor
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import uz.adkhamjon.mobileprogrammingproject.data.remote.ImageApiService
import uz.adkhamjon.mobileprogrammingproject.data.repository.ImageRepositoryImpl
import uz.adkhamjon.mobileprogrammingproject.domain.repository.ImageRepository
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DIModule {

    @Singleton
    @Provides
    fun provideGsonConvertorFactory(): GsonConverterFactory = GsonConverterFactory.create()

    @Singleton
    @Provides
    fun provideRetrofit(
        gsonConverterFactory: GsonConverterFactory,
        builder: OkHttpClient
    ): Retrofit {

        return Retrofit.Builder()
            .baseUrl("https://pixabay.com/api/")
            .client(builder)
            .addConverterFactory(gsonConverterFactory)
            .build()
    }

    @Singleton
    @Provides
    fun provideOkHttpClient(
        @ApplicationContext context: Context
    ): OkHttpClient {
        val chuckInterceptor = ChuckerInterceptor.Builder(context)
            .maxContentLength(500_000L)
            .redactHeaders("Content-Type", "application/json")
            .alwaysReadResponseBody(true)
            .build()
        return OkHttpClient.Builder()
            .addInterceptor(chuckInterceptor)
            .build()
    }

    @Singleton
    @Provides
    fun provideImageApiService(retrofit: Retrofit): ImageApiService {
        return retrofit.create(ImageApiService::class.java)
    }

    @Singleton
    @Provides
    fun provideImageRepository(
        imageApiService: ImageApiService
    ): ImageRepository {
        return ImageRepositoryImpl(imageApiService)
    }
}