package com.example.ProjetAndroid.presentation

import android.app.Application
import com.example.ProjetAndroid.di.DaggerApplicationComponent

class VndbApplication : Application() {

    val component by lazy {
        DaggerApplicationComponent.factory().create(this)
    }
}