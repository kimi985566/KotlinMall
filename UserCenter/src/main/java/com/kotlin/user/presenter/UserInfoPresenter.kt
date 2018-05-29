package com.kotlin.user.presenter

import com.kotlin.base.ext.execute
import com.kotlin.base.presenter.BasePresenter
import com.kotlin.base.rx.BaseSubscriber
import com.kotlin.user.presenter.view.UserInfoView
import com.kotlin.user.service.UploadService
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

class UserInfoPresenter @Inject constructor() : BasePresenter<UserInfoView>() {

    @Inject
    lateinit var userService: UserService

    @Inject
    lateinit var uploadService: UploadService

    fun getUploadToken() {
        if (!checkNetWork()) {
            return
        }

        mView.showLoading()

        uploadService.getUploadToken().execute(object : BaseSubscriber<String>(mView) {
            override fun onNext(t: String) {
                mView.onGetUploadTokenResult(t)
            }
        }, lifecycleProvider)

    }

}