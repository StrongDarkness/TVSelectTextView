package com.qiu.tvselecttextview.utils

import android.app.Activity
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Rect
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.os.Build

object Tool {
    private const val PRE_DL =
        "http://www.52xuexi.net/Search.aspx?filetypeid=389&MachineID="
    private val MECHINE_IDS = intArrayOf(72, 102, 111, 110)
    private val MECHINES =
        arrayOf("H8", "H9", "H15", "H16")
    private val PUNCTUATIONS = charArrayOf(
        ' ', '，', '、', '。', '？', '；',
        '：', '！', '—', '\t', '\n', '\r', '　'
    )
    private const val WHITESPACE_TABLE = (""
            + "\u2002\u3000\r\u0085\u200A\u2005\u2000\u3000"
            + "\u2029\u000B\u3000\u2008\u2003\u205F\u3000\u1680"
            + "\u0009\u0020\u2006\u2001\u202F\u00A0\u000C\u2009"
            + "\u3000\u2004\u3000\u3000\u2028\n\u2007\u3000")

//    /**
//     * 全角符号转为半角，不包括空格
//     *
//     * @param input
//     * @return
//     */
//    fun full2Half(input: String): String {
//        val c = input.toCharArray()
//        for (i in c.indices) {
//            if (c[i] > 65280.toChar() && c[i] < 65375.toChar()) {
//                c[i] -= 65248
//            }
//        }
//        return String(c)
//    }

    /**
     * 截屏
     *
     * @param activity
     * @return
     */
    fun srceenShot(activity: Activity): Bitmap {
        val view = activity.window.decorView
        view.isDrawingCacheEnabled = true
        view.buildDrawingCache()
        var bitmap = view.drawingCache
        val rect = Rect()
        view.getWindowVisibleDisplayFrame(rect)
        val metrics = activity.resources.displayMetrics
        bitmap = Bitmap.createBitmap(
            bitmap, 0, rect.top, metrics.widthPixels,
            metrics.heightPixels - rect.top
        )
        view.destroyDrawingCache()
        return bitmap
    }

    /**
     * 网络是否可用
     *
     * @param context
     * @return
     */
    fun isNetworkAvailable(context: Context): Boolean {
        try {
            val connectivity = context
                .getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            if (connectivity == null) {
                return false
            } else {
                val info = connectivity.allNetworkInfo
                if (info != null) {
                    for (i in info.indices) {
                        if (info[i].state == NetworkInfo.State.CONNECTED) {
                            return true
                        }
                    }
                }
            }
        } catch (e: Exception) {
            return false
        }
        return false
    }


    /**
     * 是否为标点符号
     *
     * @param c
     * @return
     */
    fun isPunctuation(c: Char): Boolean {
        for (i in PUNCTUATIONS) {
            if (c == i) {
                return true
            }
        }
        return false
    }


    fun validChineseStr(content: String): Int {
        if (StringUtils.isEmpty(content)) return -1
        var index = -1
        for (i in 0 until content.length) {
            val c = content[i]
            if (StringUtils.isPunctuation(c)) continue
            if (StringUtils.firstChineseChar(c.toString()) != -1) {
                index = i
                break
            }
        }
        return index
    }


    fun isChinese(input: String): Boolean {
        for (element in input) {
            val ub = Character.UnicodeBlock.of(
                element
            )
            if (!(ub === Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS || ub === Character.UnicodeBlock.CJK_COMPATIBILITY_IDEOGRAPHS || ub === Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_A)
            ) {
                return false
            }
        }
        return true
    }

    val dataDlSite: String?
        get() {
            val model = Build.MODEL
            for (i in MECHINES.indices) {
                if (model.contains(MECHINES[i])) {
                    return PRE_DL + MECHINE_IDS[i]
                }
            }
            return null
        }

    fun isWhiteSpace(c: Char): Boolean {
        return WHITESPACE_TABLE.indexOf(c) > 0
    }
}