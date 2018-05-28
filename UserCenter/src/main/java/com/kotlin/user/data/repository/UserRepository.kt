package com.kotlin.user.data.repository

import com.kotlin.base.data.net.RetrofitFactory
import com.kotlin.base.data.protocol.BaseResp
import com.kotlin.user.data.api.UserApi
import com.kotlin.user.data.protocol.*
import rx.Observable
import javax.inject.Inject

class UserRepository @Inject constructor() {
    /**
     * 用户注册
     * */
    fun register(mobile: String, pwd: String, verifyCode: String): Observable<BaseResp<String>> {
        return RetrofitFactory.instance.create(UserApi::class.java).register(RegisterReq(mobile, pwd, verifyCode))
    }

    /**
     * 用户登陆
     * */
    fun login(mobile: String, pwd: String, pushId: String): Observable<BaseResp<UserInfo>> {
        return RetrofitFactory.instance.create(UserApi::class.java).login(LoginReq(mobile, pwd, pushId))
    }

    /**
     * 用户登陆
     * */
    fun forgetPwd(mobile: String, verifyCode: String): Observable<BaseResp<String>> {
        return RetrofitFactory.instance.create(UserApi::class.java).forgetPwd(ForgetPwdReq(mobile, verifyCode))
    }

    /**
     * 用户登陆
     * */
    fun resetPwd(mobile: String, pwd: String): Observable<BaseResp<String>> {
        return RetrofitFactory.instance.create(UserApi::class.java).resetPwd(ResetPwdReq(mobile, pwd))
    }
}