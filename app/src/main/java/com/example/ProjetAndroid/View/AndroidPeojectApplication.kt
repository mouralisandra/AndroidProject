package com.example.ProjetAndroid.View

import android.app.Application
import com.example.ProjetAndroid.ViewModel.DaggerApplicationComponent

class AndroidPeojectApplication : Application() {

    val component by lazy {
        DaggerApplicationComponent.factory().create(this)
    }
}