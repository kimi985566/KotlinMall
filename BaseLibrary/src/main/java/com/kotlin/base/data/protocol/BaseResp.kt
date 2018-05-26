package com.kotlin.base.data.protocol

/**
 * Out (协变):将泛型作为内部方法的返回
 * In (逆变):将泛型对象作为函数的参数
 * */
class BaseResp<out T>(val status: Int, val message: String, val data: T) {
}