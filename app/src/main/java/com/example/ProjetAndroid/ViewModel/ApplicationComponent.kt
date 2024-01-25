package com.example.ProjetAndroid.ViewModel

import android.app.Application
import com.example.ProjetAndroid.ViewModel.viewmodels.UserViewModelFactory
import com.example.ProjetAndroid.ViewModel.viewmodels.VnItemViewModel
import com.example.ProjetAndroid.ViewModel.viewmodels.VnListViewModel
import dagger.BindsInstance
import dagger.Component


@ApplicationScope
@Component(
    modules = [
        DataModule::class
    ]
)
interface ApplicationComponent {

    fun vnItemViewModel(): VnItemViewModel.Factory

    fun vnListViewModel(): VnListViewModel.Factory

    fun userViewModelFactory(): UserViewModelFactory

    @Component.Factory
    interface Factory {
        fun create(@BindsInstance application: Application): ApplicationComponent
    }
}