package com.github.eylulnc.walkmunich.di

import com.github.eylulnc.walkmunich.feature.main.viewModel.MainScreenViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val appModule = module {
    viewModelOf(::MainScreenViewModel)
}