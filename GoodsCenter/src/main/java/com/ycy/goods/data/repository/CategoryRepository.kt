package com.ycy.goods.data.repository

import com.kotlin.base.data.net.RetrofitFactory
import com.kotlin.base.data.protocol.BaseResp
import com.ycy.goods.data.api.CategoryApi
import com.ycy.goods.data.protocol.Category
import com.ycy.goods.data.protocol.GetCategoryReq
import rx.Observable
import javax.inject.Inject


/*
    商品分类 数据层
 */
class CategoryRepository @Inject constructor() {
    /*
        获取商品分类
     */
    fun getCategory(parentId: Int): Observable<BaseResp<MutableList<Category>?>> {
        return RetrofitFactory.instance.create(CategoryApi::class.java)
                .getCategory(GetCategoryReq(parentId))
    }

}