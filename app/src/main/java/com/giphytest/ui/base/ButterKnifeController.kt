package com.giphytest.ui.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.bluelinelabs.conductor.Controller

import butterknife.ButterKnife
import butterknife.Unbinder


abstract class ButterKnifeController : Controller {

    private var unbinder: Unbinder? = null

    protected constructor() {}

    protected constructor(args: Bundle) : super(args) {}

    protected abstract fun inflateView(inflater: LayoutInflater, container: ViewGroup): View

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup): View {
        val view = inflateView(inflater, container)
        onViewBound(view)
        return view
    }

    protected open fun onViewBound(view: View) {}

    override fun onDestroyView(view: View) {
        super.onDestroyView(view)
    }

}
