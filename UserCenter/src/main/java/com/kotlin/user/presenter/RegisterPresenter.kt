package com.kotlin.user.presenter

import com.kotlin.base.ext.execute
import com.kotlin.base.presenter.BasePresenter
import com.kotlin.base.rx.BaseSubscriber
import com.kotlin.user.presenter.view.RegisterView
import com.kotlin.user.service.UserService
import javax.inject.Inject

/**
 * Inject和Module维度
 *
 * Module优先级高于Inject构造函数
 * 查找到实例对象，依次查看其参数实例化
 * Module中存在创建实例方法，则停止查找Inject维度，如果没有，查找Inject构造函数
 *
 * */

class RegisterPresenter @Inject constructor() : BasePresenter<RegisterView>() {

    @Inject
    lateinit var userService: UserService

    fun register(mobile: String, pwd: String, verifyCode: String) {
        if (!checkNetWork()) {
            return
        }
        mView.showLoading()

        userService.register(mobile, pwd, verifyCode).execute(object : BaseSubscriber<Boolean>(mView) {
            override fun onNext(t: Boolean) {
                if (t)
                    mView.onRegisterResult("注册成功")
                else
                    mView.onRegisterResult("注册失败")
            }
        }, lifecycleProvider)
    }
}