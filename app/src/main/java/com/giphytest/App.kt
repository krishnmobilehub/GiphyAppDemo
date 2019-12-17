package com.giphytest

import android.app.Application
import android.content.Context
import android.support.v7.app.AppCompatDelegate
import com.giphytest.bean.MyObjectBox

import com.giphytest.component.AppComponent
import com.giphytest.component.DaggerAppComponent

import com.giphytest.module.AppModule
import com.squareup.leakcanary.LeakCanary
import com.squareup.leakcanary.RefWatcher

import io.objectbox.BoxStore


class App : Application() {

    lateinit var appComponent: AppComponent private set
    lateinit var boxStore: BoxStore private set

    override fun onCreate() {
        super.onCreate()
        appComponent = DaggerAppComponent.builder()
                .appModule(AppModule(this))
                .build()
        appComponent.inject(this)
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true)
        refWatcher = LeakCanary.install(this)
        boxStore = MyObjectBox.builder().androidContext(this@App).build()
    }

    companion object {
        lateinit var refWatcher: RefWatcher

        operator fun get(context: Context): App {
            return context.applicationContext as App
        }
    }
}
