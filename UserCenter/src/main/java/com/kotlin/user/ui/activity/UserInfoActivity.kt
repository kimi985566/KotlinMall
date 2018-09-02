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
import com.kotlin.base.common.BaseConstant
import com.kotlin.base.ext.onClick
import com.kotlin.base.ui.activity.BaseMVPActivity
import com.kotlin.base.utils.AppPrefsUtils
import com.kotlin.base.utils.ColoredSnackbar
import com.kotlin.base.utils.GlideUtils
import com.kotlin.provider.common.ProviderConstant
import com.kotlin.user.R
import com.kotlin.user.data.protocol.UserInfo
import com.kotlin.user.injection.component.DaggerUserComponent
import com.kotlin.user.injection.module.UserModule
import com.kotlin.user.presenter.UserInfoPresenter
import com.kotlin.user.presenter.view.UserInfoView
import com.kotlin.user.utils.UserPrefsUtils
import com.qiniu.android.storage.UploadManager
import kotlinx.android.synthetic.main.activity_user_info.*
import pub.devrel.easypermissions.AfterPermissionGranted
import pub.devrel.easypermissions.AppSettingsDialog
import pub.devrel.easypermissions.EasyPermissions
import java.io.File

class UserInfoActivity : BaseMVPActivity<UserInfoPresenter>(), UserInfoView, View.OnClickListener,
        TakePhoto.TakeResultListener, EasyPermissions.PermissionCallbacks {

    companion object {

        const val RC_Camera = 1001
        const val RC_Storage = 1002

        private lateinit var mTakePhoto: TakePhoto

        private lateinit var mTempFile: File

        private val mUploadManager: UploadManager by lazy { UploadManager() }

        private var mLocalFileUrl: String? = null

        private var mRemoteFileUrl: String? = null

        private var mUserIcon: String? = null
        private var mUserName: String? = null
        private var mUserGender: String? = null
        private var mUserMobile: String? = null
        private var mUserSign: String? = null

        private var storagePerm = arrayOf(
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.CAMERA)

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_info)

        mTakePhoto = TakePhotoImpl(this, this)
        mTakePhoto.onCreate(savedInstanceState)

        initView()
        initData()
    }

    /**
     * 初始化视图
     * */
    private fun initView() {
        mUserIconView.onClick(this)
        mHeaderBar.getRightView().onClick {
            mPresenter.editUser(mRemoteFileUrl!!,
                    mUserNameEt.text?.toString() ?: "",
                    if (mGenderMaleRb.isChecked) "0" else "1",
                    mUserSignEt.text?.toString() ?: ""
            )
        }
    }

    private fun initData() {
        mUserIcon = AppPrefsUtils.getString(ProviderConstant.KEY_SP_USER_ICON)
        mUserName = AppPrefsUtils.getString(ProviderConstant.KEY_SP_USER_NAME)
        mUserMobile = AppPrefsUtils.getString(ProviderConstant.KEY_SP_USER_MOBILE)
        mUserGender = AppPrefsUtils.getString(ProviderConstant.KEY_SP_USER_GENDER)
        mUserSign = AppPrefsUtils.getString(ProviderConstant.KEY_SP_USER_SIGN)

        mRemoteFileUrl = mUserIcon
        if (mUserIcon != "") {
            GlideUtils.loadUrlImage(this, mUserIcon!!, mUserIconIv)
        }
        mUserNameEt.setText(mUserName)
        mUserMobileTv.text = mUserMobile

        if (mUserGender == "0") {
            mGenderMaleRb.isChecked = true
        } else {
            mGenderFemaleRb.isChecked = true
        }

        mUserSignEt.setText(mUserSign)

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
        AlertView("选择图片", null, "取消",
                null, arrayOf("拍照", "相册"), this,
                AlertView.Style.ActionSheet, OnItemClickListener { _, position ->
            mTakePhoto.onEnableCompress(CompressConfig.ofDefaultConfig(), false)
            when (position) {
                0 -> {
                    askCameraPerm()
                }
                1 -> {
                    askStoragePerm()
                }
            }
        }).show()
    }

    override fun takeSuccess(result: TResult?) {
        mLocalFileUrl = result?.image?.compressPath
        mPresenter.getUploadToken()
    }

    override fun takeCancel() {

    }

    override fun takeFail(result: TResult?, msg: String?) {

    }

    override fun onGetUploadTokenResult(result: String) {
        mUploadManager.put(mLocalFileUrl, null, result, { key, info, response ->
            mRemoteFileUrl = BaseConstant.IMAGE_SERVER_ADDRESS + response?.get("hash")
            GlideUtils.loadUrlImage(this@UserInfoActivity, mRemoteFileUrl!!, mUserIconIv)
        }, null)
    }

    override fun onEditUserResult(result: UserInfo) {
        ColoredSnackbar.confirm(mUserInfoRootView, "修改成功").show()
        UserPrefsUtils.putUserInfo(result)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        mTakePhoto.onActivityResult(requestCode, resultCode, data)
    }

    private fun createTempFile() {
        val tempFileName = "${com.kotlin.base.utils.DateUtils.curTime}.png"

        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageDirectory())) {
            mTempFile = File(Environment.getExternalStorageDirectory(), tempFileName)
            return
        }

        mTempFile = File(filesDir, tempFileName)
    }

    @AfterPermissionGranted(RC_Camera)
    private fun askCameraPerm() {
        if (EasyPermissions.hasPermissions(this, *storagePerm)) {
            createTempFile()
            mTakePhoto.onPickFromCapture(Uri.fromFile(mTempFile))
        } else {
            EasyPermissions.requestPermissions(this, "需要获取系统的拍照权限！",
                    RC_Camera, *storagePerm)
        }
    }

    @AfterPermissionGranted(RC_Storage)
    private fun askStoragePerm() {
        if (EasyPermissions.hasPermissions(this, *storagePerm)) {
            mTakePhoto.onPickFromGallery()
        } else {
            EasyPermissions.requestPermissions(this, "需要获取系统的拍照权限！",
                    RC_Storage, *storagePerm)
        }
    }

    override fun onPermissionsDenied(requestCode: Int, perms: MutableList<String>) {
        if (EasyPermissions.somePermissionPermanentlyDenied(this, perms)) {
            AppSettingsDialog.Builder(this).build().show()
        }
    }

    override fun onPermissionsGranted(requestCode: Int, perms: MutableList<String>) {
        Log.i(this.javaClass.simpleName, "$perms Permission Granted")
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        // Forward results to EasyPermissions
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this)
    }
}
