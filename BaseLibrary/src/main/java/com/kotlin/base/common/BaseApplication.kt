package com.kotlin.base.common

import android.app.Application
import com.kotlin.base.injection.component.AppComponent
import com.kotlin.base.injection.component.DaggerAppComponent
import com.kotlin.base.injection.module.AppModule

class BaseApplication : Application() {

    lateinit var mAppComponent: AppComponent

    override fun onCreate() {
        super.onCreate()

        initAppInjection()
    }

    private fun initAppInjection() {
        mAppComponent = DaggerAppComponent.builder().appModule(AppModule(this)).build()
    }
}