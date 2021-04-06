package toma.razvan.weatherapp.di

import com.google.gson.GsonBuilder
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import toma.razvan.weatherapp.data.api.Api
import toma.razvan.weatherapp.utils.Constants.API_BASE_URL
import toma.razvan.weatherapp.utils.Constants.API_EXCLUDE_MINUTELY
import toma.razvan.weatherapp.utils.Constants.API_KEY
import toma.razvan.weatherapp.utils.Constants.API_UNITS

val networkModule = module {
    single { provideDefaultOkHttpClient() }
    single { provideRetrofit(get()) }
    single { provideApi(get()) }
}

class TokenInterceptor : Interceptor{
    override fun intercept(chain: Interceptor.Chain): Response {
        var original = chain.request()
        val url = original.url.newBuilder()
            .addQueryParameter("exclude", API_EXCLUDE_MINUTELY)
            .addQueryParameter("units", API_UNITS)
            .addQueryParameter("appid", API_KEY)
            .build()
        original = original.newBuilder().url(url).build()
        return chain.proceed(original)
    }
}

fun provideDefaultOkHttpClient(): OkHttpClient {
    val okHttpClient = OkHttpClient.Builder()

    okHttpClient.addInterceptor(TokenInterceptor())

    return okHttpClient.build()
}

fun provideRetrofit(client: OkHttpClient): Retrofit {
    val gson = GsonBuilder()
        .setLenient()
        .create()

    return Retrofit.Builder()
        .baseUrl(API_BASE_URL)
        .client(client)
        .addConverterFactory(GsonConverterFactory.create(gson))
        .build()
}

fun provideApi(retrofit: Retrofit): Api = retrofit.create(Api::class.java)