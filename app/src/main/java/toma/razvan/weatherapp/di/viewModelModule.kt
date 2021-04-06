package toma.razvan.weatherapp.di

import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import toma.razvan.weatherapp.data.repository.ApiRepository
import toma.razvan.weatherapp.ui.viewmodels.WeatherViewModel

val viewModelModule = module {
    single { ApiRepository(get()) }

    viewModel { WeatherViewModel(get()) }
}