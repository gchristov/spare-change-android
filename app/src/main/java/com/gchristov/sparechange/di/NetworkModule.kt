package com.gchristov.sparechange.di

import android.content.Context
import android.content.res.Resources
import com.gchristov.sparechange.R
import com.gchristov.sparechange.api.StarlingService
import com.gchristov.sparechange.common.fromMinorUnit
import com.squareup.moshi.FromJson
import com.squareup.moshi.Moshi
import com.squareup.moshi.ToJson
import com.squareup.moshi.adapters.Rfc3339DateJsonAdapter
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.apache.commons.lang3.time.DateFormatUtils
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.moshi.MoshiConverterFactory
import java.math.BigDecimal
import java.util.*

private const val BASE_URL = "https://api-sandbox.starlingbank.com/"
private const val HEADER_AUTHORIZATION = "Authorization"

val networkModule = module {
    single { provideDateFormat() }
    single { provideResources(get()) }
    single { provideAuthInterceptor(get()) }
    single { provideLogInterceptor() }
    single { provideMinorUnitAdapter() }
    single { provideOkHttpClient(get(), get()) }
    single { provideApiService(get(), get()) }
}

private fun provideDateFormat() = DateFormatUtils.ISO_8601_EXTENDED_DATETIME_TIME_ZONE_FORMAT

private fun provideResources(context: Context) = context.resources

private fun provideAuthInterceptor(resources: Resources): Interceptor {
    return Interceptor { chain ->
        val original = chain.request()
        val hb = original.headers.newBuilder()
        hb.add(HEADER_AUTHORIZATION, "Bearer ${resources.getString(R.string.starling_api_key)}")
        chain.proceed(original.newBuilder().headers(hb.build()).build())
    }
}

private fun provideLogInterceptor(): HttpLoggingInterceptor {
    val interceptor = HttpLoggingInterceptor()
    interceptor.setLevel(HttpLoggingInterceptor.Level.BODY)
    return interceptor
}

private fun provideOkHttpClient(
    authInterceptor: Interceptor,
    logInterceptor: HttpLoggingInterceptor
): OkHttpClient {
    val clientBuilder = OkHttpClient.Builder()
    clientBuilder.addInterceptor(authInterceptor)
    clientBuilder.addInterceptor(logInterceptor)
    return clientBuilder.build()
}

private fun provideMinorUnitAdapter(): Any = object : Any() {
    @FromJson
    fun fromJson(string: String) = BigDecimal(string).fromMinorUnit()

    @ToJson
    fun toJson(value: BigDecimal) = value.unscaledValue().toString()
}

private fun provideApiService(
    okHttpClient: OkHttpClient,
    minorUnitAdapter: Any
): StarlingService {
    val jsonConverter = Moshi.Builder()
            .add(minorUnitAdapter)
            .add(KotlinJsonAdapterFactory())
            .add(Date::class.java, Rfc3339DateJsonAdapter())
            .build()
    return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(MoshiConverterFactory.create(jsonConverter))
            .client(okHttpClient)
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build()
            .create(StarlingService::class.java)
}
