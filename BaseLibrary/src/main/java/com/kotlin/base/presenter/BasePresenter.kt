package com.kotlin.base.presenter

import com.kotlin.base.presenter.view.BaseView
import com.trello.rxlifecycle.LifecycleProvider

open class BasePresenter<T : BaseView> {
    lateinit var mView: T

    lateinit var lifecycleProvider: LifecycleProvider<*>
}