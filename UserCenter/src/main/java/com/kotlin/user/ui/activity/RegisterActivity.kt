package com.kotlin.user.ui.activity

import android.os.Bundle
import com.kotlin.base.common.AppManager
import com.kotlin.base.ext.onClick
import com.kotlin.base.ui.activity.BaseMVPActivity
import com.kotlin.user.R
import com.kotlin.user.injection.component.DaggerUserComponent
import com.kotlin.user.injection.module.UserModule
import com.kotlin.user.presenter.RegisterPresenter
import com.kotlin.user.presenter.view.RegisterView
import kotlinx.android.synthetic.main.activity_register.*
import org.jetbrains.anko.design.snackbar

class RegisterActivity : BaseMVPActivity<RegisterPresenter>(), RegisterView {

    private var pressTime: Long = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        mRegisterBtn.onClick {
            mPresenter.register(mMobileEt.text.toString(), mPwdEt.text.toString(), mVerifyCodeEt.text.toString())
        }
    }

    override fun onRegisterResult(result: String) {
        snackbar(mRegisterRootView, result)
    }

    override fun injectComponent() {
        /**
         * Dagger2采用了apt代码自动生成技术，其注解是停留在编译时，不影响性能
         *
         * Module并不是必需的，但Component是必不可少的；
         * 编译后生成的Component实现类的名称是Dagger+我们所定义的Component接口的名称
         *
         * 注入到当前的Activity之中
         * */
        DaggerUserComponent
                .builder()
                .activityComponent(mActivityComponent)
                .userModule(UserModule())
                .build()
                .inject(this)

        mPresenter.mView = this
    }

    override fun onBackPressed() {
        val time = System.currentTimeMillis()
        if (time - pressTime > 2000) {
            snackbar(mRegisterRootView, resources.getString(R.string.press_again))
            pressTime = time
        } else {
            AppManager.instance.exitApp(this)
        }
    }
}
