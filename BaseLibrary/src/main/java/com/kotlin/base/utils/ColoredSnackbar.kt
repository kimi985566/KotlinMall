package com.kotlin.base.utils

import android.support.design.widget.Snackbar
import android.view.View

/**
 * @author Chengyu Yang
 *
 * 具有颜色的SnackBar
 */

object ColoredSnackbar {

    private val RED = -0xbbcca
    private val GREEN = -0xb350b0
    private val BLUE = -0xde6a0d
    private val ORANGE = -0x3ef9

    private fun getSnackBarLayout(snackbar: Snackbar?): View? {
        return snackbar?.view
    }

    private fun colorSnackBar(view: View, text: String, colorId: Int): Snackbar {
        val snackbar = Snackbar.make(view, text, Snackbar.LENGTH_SHORT)
        val snackBarView = getSnackBarLayout(snackbar)
        snackBarView?.setBackgroundColor(colorId)
        return snackbar
    }

    fun info(view: View, text: String): Snackbar {
        return colorSnackBar(view, text, BLUE)
    }

    fun warning(view: View, text: String): Snackbar {
        return colorSnackBar(view, text, ORANGE)
    }

    fun alert(view: View, text: String): Snackbar {
        return colorSnackBar(view, text, RED)
    }

    fun confirm(view: View, text: String): Snackbar {
        return colorSnackBar(view, text, GREEN)
    }
}
