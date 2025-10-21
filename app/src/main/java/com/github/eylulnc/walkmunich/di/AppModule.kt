package com.github.eylulnc.walkmunich.di

import com.github.eylulnc.walkmunich.core.data.repository.PlacesRepository
import com.github.eylulnc.walkmunich.core.data.repository.UserPreferencesRepository
import com.github.eylulnc.walkmunich.core.data.service.PlacesService
import com.github.eylulnc.walkmunich.core.data.service.PlacesServiceImpl
import com.github.eylulnc.walkmunich.feature.home.ui.category.viewmodel.CategoryPlacesViewModel
import com.github.eylulnc.walkmunich.feature.home.data.repository.CityRepository
import com.github.eylulnc.walkmunich.feature.home.data.service.CityService
import com.github.eylulnc.walkmunich.feature.home.data.service.CityServiceImpl
import com.github.eylulnc.walkmunich.feature.home.ui.settings.SettingsViewModel
import com.github.eylulnc.walkmunich.feature.home.viewModel.HomeScreenViewModel
import com.github.eylulnc.walkmunich.feature.place.viewmodel.PlaceViewModel
import com.github.eylulnc.walkmunich.feature.route.data.RoutesRepository
import com.github.eylulnc.walkmunich.feature.route.data.RoutesService
import com.github.eylulnc.walkmunich.feature.route.data.RoutesServiceImpl
import com.github.eylulnc.walkmunich.feature.route.viewmodel.RouteDetailViewModel
import com.github.eylulnc.walkmunich.feature.route.viewmodel.RouteListViewModel
import kotlinx.serialization.json.Json
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.dsl.viewModel
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

    single { UserPreferencesRepository(androidContext()) }

    viewModelOf(::HomeScreenViewModel)
    viewModelOf(::RouteListViewModel)
    viewModelOf(::RouteDetailViewModel)
    viewModelOf(::SettingsViewModel)

    viewModel { (placeId: Long, subTitle: String?) ->
        PlaceViewModel(get(), placeId, subTitle)
    }

    viewModel { (category: com.github.eylulnc.walkmunich.core.data.model.Category) ->
        CategoryPlacesViewModel(get(), category)
    }
}