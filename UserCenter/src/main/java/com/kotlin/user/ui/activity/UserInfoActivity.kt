package com.kotlin.user.ui.activity

import android.Manifest
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.view.View
import com.bigkoo.alertview.AlertView
import com.bigkoo.alertview.OnItemClickListener
import com.jph.takephoto.app.TakePhoto
import com.jph.takephoto.app.TakePhotoImpl
import com.jph.takephoto.compress.CompressConfig
import com.jph.takephoto.model.TResult
import com.kotlin.base.ext.onClick
import com.kotlin.base.ui.activity.BaseMVPActivity
import com.kotlin.user.R
import com.kotlin.user.injection.component.DaggerUserComponent
import com.kotlin.user.injection.module.UserModule
import com.kotlin.user.presenter.UserInfoPresenter
import com.kotlin.user.presenter.view.UserInfoView
import kotlinx.android.synthetic.main.activity_user_info.*
import pub.devrel.easypermissions.AfterPermissionGranted
import pub.devrel.easypermissions.EasyPermissions
import java.io.File

class UserInfoActivity : BaseMVPActivity<UserInfoPresenter>(), UserInfoView, View.OnClickListener,
        TakePhoto.TakeResultListener, EasyPermissions.PermissionCallbacks {

    companion object {
        const val RC_Camera = 1001

        private lateinit var mTakePhoto: TakePhoto

        private lateinit var mTempFile: File

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_info)

        mTakePhoto = TakePhotoImpl(this, this)

        initView()

        mTakePhoto.onCreate(savedInstanceState)
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
            mTakePhoto.onEnableCompress(CompressConfig.ofDefaultConfig(), false)
            when (position) {
                0 -> {
                    askCameraPerm()
                }
                1 -> {
                    mTakePhoto.onPickFromGallery()
                }
            }
        }).show()
    }

    override fun takeSuccess(result: TResult?) {

    }

    override fun takeCancel() {

    }

    override fun takeFail(result: TResult?, msg: String?) {

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        mTakePhoto.onActivityResult(requestCode, resultCode, data)
    }

    private fun createTempFile() {
        var tempFileName = "${com.kotlin.base.utils.DateUtils.curTime}.png"

        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageDirectory())) {
            mTempFile = File(Environment.getExternalStorageDirectory(), tempFileName)
            return
        }

        mTempFile = File(filesDir, tempFileName)
    }

    @AfterPermissionGranted(RC_Camera)
    private fun askCameraPerm() {
        if (EasyPermissions.hasPermissions(this, Manifest.permission.CAMERA)) {
            createTempFile()
            mTakePhoto.onPickFromCapture(Uri.fromFile(mTempFile))
        } else {
            EasyPermissions.requestPermissions(this, "需要获取系统的拍照权限！",
                    RC_Camera, Manifest.permission.CAMERA)
        }
    }

    override fun onPermissionsDenied(requestCode: Int, perms: MutableList<String>) {
        AlertView("请求权限", "需要获取系统权限才能正常的使用", null,
                arrayOf("确定"), null, this,
                AlertView.Style.Alert, null).show()
    }

    override fun onPermissionsGranted(requestCode: Int, perms: MutableList<String>) {
        Log.i(this.javaClass.simpleName, "$perms Permission Granted")
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this)
    }
}
