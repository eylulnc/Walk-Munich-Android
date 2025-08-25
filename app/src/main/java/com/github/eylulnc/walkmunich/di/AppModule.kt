package com.github.eylulnc.walkmunich.di

import com.github.eylulnc.walkmunich.feature.main.data.CityRepository
import com.github.eylulnc.walkmunich.feature.main.data.CityService
import com.github.eylulnc.walkmunich.feature.main.data.CityServiceImpl
import com.github.eylulnc.walkmunich.feature.main.viewModel.MainScreenViewModel
import kotlinx.serialization.json.Json
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val appModule = module {
    single { Json { ignoreUnknownKeys = true } }
    single<CityService> { CityServiceImpl(androidContext(), get()) }
    single { CityRepository(get()) }
    viewModelOf(::MainScreenViewModel)
}