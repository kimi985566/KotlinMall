package com.kotlin.base.ui.fragment

import android.os.Bundle
import com.kotlin.base.common.BaseApplication
import com.kotlin.base.injection.component.ActivityComponent
import com.kotlin.base.injection.component.DaggerActivityComponent
import com.kotlin.base.injection.module.ActivityModule
import com.kotlin.base.injection.module.LifecycleProviderModule
import com.kotlin.base.presenter.BasePresenter
import com.kotlin.base.presenter.view.BaseView
import org.jetbrains.anko.toast
import javax.inject.Inject

abstract class BaseMVPFragment<T : BasePresenter<*>> : BaseFragment(), BaseView {

    @Inject
    lateinit var mPresenter: T

    lateinit var mActivityComponent: ActivityComponent

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initActivityInjection()

        injectComponent()

    }

    protected abstract fun injectComponent()

    override fun showLoading() {
    }

    override fun hideLoading() {
    }

    override fun onError(text: String) {
        toast(text)
    }

    private fun initActivityInjection() {
        mActivityComponent = DaggerActivityComponent
                .builder()
                .appComponent((activity.application as BaseApplication).mAppComponent)
                .activityModule(ActivityModule(activity))
                .lifecycleProviderModule(LifecycleProviderModule(this))
                .build()

    }
}
