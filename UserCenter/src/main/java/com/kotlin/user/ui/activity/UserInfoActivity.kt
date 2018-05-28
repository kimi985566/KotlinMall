package com.kotlin.user.ui.activity

import android.os.Bundle
import android.view.View
import com.bigkoo.alertview.AlertView
import com.bigkoo.alertview.OnItemClickListener
import com.kotlin.base.ext.onClick
import com.kotlin.base.ui.activity.BaseMVPActivity
import com.kotlin.user.R
import com.kotlin.user.injection.component.DaggerUserComponent
import com.kotlin.user.injection.module.UserModule
import com.kotlin.user.presenter.UserInfoPresenter
import com.kotlin.user.presenter.view.UserInfoView
import kotlinx.android.synthetic.main.activity_user_info.*
import org.jetbrains.anko.toast

class UserInfoActivity : BaseMVPActivity<UserInfoPresenter>(), UserInfoView, View.OnClickListener {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_info)

        initView()
    }

    /**
     * 初始化视图
     * */
    private fun initView() {

        mUserIconView.onClick(this)
    }

    /**
     * 注入
     * */
    override fun injectComponent() {
        DaggerUserComponent
                .builder()
                .activityComponent(mActivityComponent)
                .userModule(UserModule())
                .build()
                .inject(this)

        mPresenter.mView = this
    }

    /**
     * 点击事件
     * */
    override fun onClick(v: View) {
        when (v.id) {
            R.id.mUserIconView -> {
                showAlertView()
            }
        }
    }

    private fun showAlertView() {
        AlertView("选择图片", "", "取消", null, arrayOf("拍照", "相册"), this,
                AlertView.Style.ActionSheet, OnItemClickListener { _, position ->
            when (position) {
                0 -> {
                    toast("拍照")
                }
                1 -> {
                    toast("相册")
                }
            }
        }).show()
    }
}