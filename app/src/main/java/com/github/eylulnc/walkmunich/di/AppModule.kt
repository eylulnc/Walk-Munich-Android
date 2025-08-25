package com.github.eylulnc.walkmunich.di

import com.github.eylulnc.walkmunich.feature.main.data.city.repository.CityRepository
import com.github.eylulnc.walkmunich.feature.main.data.city.service.CityService
import com.github.eylulnc.walkmunich.feature.main.data.city.service.CityServiceImpl
import com.github.eylulnc.walkmunich.feature.main.data.place.repository.PlacesRepository
import com.github.eylulnc.walkmunich.feature.main.data.place.service.PlacesMinService
import com.github.eylulnc.walkmunich.feature.main.data.place.service.PlacesMinServiceImpl
import com.github.eylulnc.walkmunich.feature.main.viewModel.MainScreenViewModel
import kotlinx.serialization.json.Json
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val appModule = module {
    single { Json { ignoreUnknownKeys = true } }

    single<CityService> { CityServiceImpl(androidContext(), get()) }
    single { CityRepository(get()) }

    single<PlacesMinService> { PlacesMinServiceImpl(androidContext(), get()) }
    single { PlacesRepository(get()) }

    viewModelOf(::MainScreenViewModel)
}