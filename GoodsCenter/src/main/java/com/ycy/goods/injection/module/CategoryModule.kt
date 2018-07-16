package com.ycy.goods.injection.module

import com.ycy.goods.service.CategoryService
import com.ycy.goods.service.impl.CategoryServiceImpl
import dagger.Module
import dagger.Provides


/*
    商品分类Module
 */
@Module
class CategoryModule {

    @Provides
    fun provideCategoryService(categoryService: CategoryServiceImpl): CategoryService {
        return categoryService
    }

}