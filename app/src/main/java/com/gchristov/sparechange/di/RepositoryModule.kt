package com.gchristov.sparechange.di

import com.gchristov.sparechange.api.StarlingService
import com.gchristov.sparechange.repository.Repository
import org.apache.commons.lang3.time.FastDateFormat
import org.koin.dsl.module

val repositoryModule = module {
    single { provideRepository(get(), get()) }
}

private fun provideRepository(
    api: StarlingService,
    dateFormat: FastDateFormat
): Repository {
    return Repository(api, dateFormat)
}
