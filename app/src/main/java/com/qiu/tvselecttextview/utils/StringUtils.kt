package com.qiu.tvselecttextview.utils

import android.text.TextUtils

object StringUtils {
    fun isEmpty(content: String): Boolean {
        return TextUtils.isEmpty(content) || content.trim { it <= ' ' }
            .isEmpty() || "null" == content || "NULL" == content
    }

    fun isLettersWithStr(str: String): Boolean {
        return if (isEmpty(str)) true else str.matches(Regex("[a-zA-Z]+"))
    }

    fun firstChineseChar(s: String): Int {
        for (index in s.indices) {
            val w = s.substring(index, index + 1)
            if (w >= "\u4e00" && w <= "\u9fa5") { // \u4e00-\u9fa5 中文汉字的范围
                return index
            }
        }
        return -1
    }

    val PUNCTUATIONS = charArrayOf(
        ' ', '，', '、', '。', '？', '；',
        '：', '！', '—', '\t', '\n', '\r', '　'
    )

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
        if (isEmpty(content)) return -1
        var index = -1
        for (i in 0 until content.length) {
            val c = content[i]
            if (isPunctuation(c)) continue
            if (firstChineseChar(c.toString()) != -1) {
                index = i
                break
            }
        }
        return index
    }

    fun isChinese(input: String): Boolean {
        for (i in 0 until input.length) {
            val ub = Character.UnicodeBlock.of(
                input[i]
            )
            if (!(ub === Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS || ub === Character.UnicodeBlock.CJK_COMPATIBILITY_IDEOGRAPHS || ub === Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_A)
            ) {
                return false
            }
        }
        return true
    }
}