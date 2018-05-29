package com.kotlin.user.service

import rx.Observable

interface UploadService {
    //用户注册
    fun getUploadToken(): Observable<String>
}