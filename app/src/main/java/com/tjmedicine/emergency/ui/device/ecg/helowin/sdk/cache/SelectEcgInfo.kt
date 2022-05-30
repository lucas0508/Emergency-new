package com.helowin.sdk.cache

import java.io.Serializable
import java.util.*
import kotlin.collections.ArrayList

class SelectEcgInfo {
    var datas:ArrayList<SelectEcg>? = ArrayList<SelectEcg>()

   class SelectEcg : Serializable {
        var uuid: String? = null
        var fileImagePath: String? = null
        var heartRate: String = "0"
        var length: String? = null
        var takeTime: String = System.currentTimeMillis().toString()
        var title: String = "null"
        var id: String? = null
        var ecgUrl: String? = null
        var diastolic // 舒张压 number
                : String? = null
        var filePath // 心电文件路径 string
                : String? = null
        var isNormal // 是否是正常心电 0-是 1-否 number
                : String? = null

        //length 测量时长 秒 number
        var reportType // 1-血压 2-心电 number
                : String? = null
        var systolic // 收缩压 number
                : String? = null
        var type // 0-快速 1-监测 2-睡眠 3-运动
                : String? = null
        var isRead: String? = null
        override fun equals(o: Any?): Boolean {
            if (this === o) return true
            if (o == null || javaClass != o.javaClass) return false
            val selectEcg = o as SelectEcg
            return fileImagePath == selectEcg.fileImagePath &&
                    heartRate == selectEcg.heartRate &&
                    length == selectEcg.length &&
                    takeTime == selectEcg.takeTime &&
                    title == selectEcg.title &&
                    id == selectEcg.id &&
                    ecgUrl == selectEcg.ecgUrl &&
                    diastolic == selectEcg.diastolic &&
                    filePath == selectEcg.filePath &&
                    isNormal == selectEcg.isNormal &&
                    reportType == selectEcg.reportType &&
                    systolic == selectEcg.systolic &&
                    type == selectEcg.type &&
                    isRead == selectEcg.isRead
        }

        override fun hashCode(): Int {
            return Objects.hash(
                fileImagePath,
                heartRate,
                length,
                takeTime,
                title,
                id,
                ecgUrl,
                diastolic,
                filePath,
                isNormal,
                reportType,
                systolic,
                type,
                isRead
            )
        }

        override fun toString(): String {
            return "SelectEcg{" +
                    "fileImagePath='" + fileImagePath + '\'' +
                    ", heartRate='" + heartRate + '\'' +
                    ", length='" + length + '\'' +
                    ", takeTime='" + takeTime + '\'' +
                    ", title='" + title + '\'' +
                    ", id='" + id + '\'' +
                    ", ecgUrl='" + ecgUrl + '\'' +
                    ", diastolic='" + diastolic + '\'' +
                    ", filePath='" + filePath + '\'' +
                    ", isNormal='" + isNormal + '\'' +
                    ", reportType='" + reportType + '\'' +
                    ", systolic='" + systolic + '\'' +
                    ", type='" + type + '\'' +
                    ", isRead='" + isRead + '\'' +
                    '}'
        }

        fun time(): String {
            val date = length!!.toInt()
            return if (date < 60) {
                date.toString() + "s"
            } else if (date >= 60 && date < 3600) {
                val m = date / 60
                val s = date % 60
                if (s == 0) {
                    m.toString() + "min"
                } else m.toString() + "min" + s + "s"
            } else {
                val h = date / 3600
                val m = date % 3600 / 60
                val s = date % 3600 % 60
                val stringBuffer =
                    StringBuilder(h.toString() + "h")
                if (m != 0) {
                    stringBuffer.append(m.toString() + "min")
                }
                if (s != 0) {
                    stringBuffer.append(s.toString() + "s")
                }
                stringBuffer.toString()
            }
        }

        companion object {
            fun getLength(string: String?): String {
                val selectEcg = SelectEcg()
                selectEcg.length = string
                return selectEcg.time()
            }
        }
    }
}