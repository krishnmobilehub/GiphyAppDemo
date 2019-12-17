package com.giphytest.component

import android.app.Application
import android.content.Context

import com.giphytest.App
import com.giphytest.module.AppModule
import com.giphytest.ui.activity.MainActivity

import javax.inject.Singleton

import dagger.Component


@Singleton
@Component(modules = arrayOf(AppModule::class))
interface AppComponent {

    fun inject(app: App)

    fun inject(mainActivity: MainActivity)

    fun application(): Application

    fun context(): Context
}
