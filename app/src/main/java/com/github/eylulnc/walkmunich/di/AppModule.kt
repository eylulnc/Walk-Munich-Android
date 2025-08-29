package com.github.eylulnc.walkmunich.di

import com.github.eylulnc.walkmunich.feature.home.data.city.repository.CityRepository
import com.github.eylulnc.walkmunich.feature.home.data.city.service.CityService
import com.github.eylulnc.walkmunich.feature.home.data.city.service.CityServiceImpl
import com.github.eylulnc.walkmunich.feature.home.data.place.repository.PlacesRepository
import com.github.eylulnc.walkmunich.feature.home.data.place.service.PlacesService
import com.github.eylulnc.walkmunich.feature.home.data.place.service.PlacesServiceImpl
import com.github.eylulnc.walkmunich.feature.home.viewModel.HomeScreenViewModel
import com.github.eylulnc.walkmunich.feature.route.data.RoutesRepository
import com.github.eylulnc.walkmunich.feature.route.data.RoutesService
import com.github.eylulnc.walkmunich.feature.route.data.RoutesServiceImpl
import com.github.eylulnc.walkmunich.feature.route.viewmodel.RouteDetailViewModel
import com.github.eylulnc.walkmunich.feature.route.viewmodel.RouteListViewModel
import kotlinx.serialization.json.Json
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val appModule = module {
    single { Json { ignoreUnknownKeys = true } }

    single<CityService> { CityServiceImpl(androidContext(), get()) }
    single { CityRepository(get()) }

    single<PlacesService> { PlacesServiceImpl(androidContext(), get()) }
    single { PlacesRepository(get()) }

    single<RoutesService> { RoutesServiceImpl(androidContext(), get()) }
    single { RoutesRepository(get()) }

    viewModelOf(::HomeScreenViewModel)
    viewModelOf(::RouteListViewModel)
    viewModelOf(::RouteDetailViewModel)
}