package com.helowin.sdk.cache

import java.io.Serializable
import java.util.*

/**
 * abnorAnalysis 原因分析(支付后存在) string
 * avg 平均心率 number
 * ecgResult 心电分析结果(支付后存在) string
 * ecgResultTz 心电分析印象 string
 * ecgUrl 心电图跳转 string
 *
 *
 * extList 心电测量指标（异常） array<object>
 * company 单位 string
 * ecg 结果 string
 * name 测量指标 string
 * range 参考范围 string
 * state 0-正常 1-偏大 2-偏小 3-异常 number
 * fatigue_status 疲劳指数 0-正常 1-偏大 2-偏小(支付后存在) number
 * fatigue_value 疲劳指数(支付后存在) number
 * fileImagePath 心电浏览图 string
 * hdrisk_status 心脏疾病风险评估 0-正常 1-偏大 2-偏小(支付后存在) number
 * hdrisk_value 心脏疾病风险评估(支付后存在) number
 * healthCareAdvice 保健建议(支付后存在) string
 * heartRate 心电浏览图心率 number
 * heartbeatRate 心率偏快 number
 * hrv_status 心率变异指标 0-正常 1-偏大 2-偏小 number
 * hrv_value 心率变异指标 number
 * ifPayment 是否支付 0-未支付 1-已支付 number
 * interpretationReportId 人工解读id state-2时存在 number
 * length 时长 秒 number
 * max 最高心率 number
 * mental_status 精神压力指数 精神压力指数(支付后存在) number
 * mental_value 精神压力指数(支付后存在) number
 * min 最低心率 number
 *
 *
 * normalList  array<object>
 * company 单位 string
 * ecg 结果 number
 * name 测量指标 string
 * range 参考范围 string
 * state 0-正常 1-偏大 2-偏小 3-异常 number
 * normalRate 正常心率 number
 * slowRate 心率偏慢 number
 * state 状态 0-测量结束 1-数据干扰或导联脱落 2-解读完成 3-解读中 number
 * suggestion 处置建议(支付后存在) string
 * takeTime 测量时间 number
 * title
</object></object> */
class XEcgDetialBean : Serializable {
    var abnorAnalysis // 原因分析(支付后存在) string
            : String? = null
    var avg // 平均心率 number
            : String? = null
    var ecgResult // 心电分析结果(支付后存在) string
            : String? = null
    var ecgResultTz // 心电分析印象 string
            : String? = null
    var ecgUrl // 心电图跳转 string
            : String? = null
    var filePath: String? = null
    var extList // 心电测量指标（异常） array<object>
            : ArrayList<TargetBean>? = null
    var company // 单位 string
            : String? = null
    var ecg // 结果 string
            : String? = null
    var name // 测量指标 string
            : String? = null
    var range // 参考范围 string
            : String? = null
    var fatigue // 疲劳指数文字解释 string
            : String? = null
    var fatigue_status // 疲劳指数 0-正常 1-偏大 2-偏小(支付后存在) number
            : String? = null
    var fatigue_value // 疲劳指数(支付后存在) number
            : String? = null
    var fileImagePath // 心电浏览图 string
            : String? = null
    var hdrisk // 心脏疾病风险文字解释 string
            : String? = null
    var hdrisk_status // 心脏疾病风险评估 0-正常 1-偏大 2-偏小(支付后存在) number
            : String? = null
    var hdrisk_value // 心脏疾病风险评估(支付后存在) number
            : String? = null
    var healthCareAdvice // 保健建议(支付后存在) string
            : String? = null
    var heartRate // 心电浏览图心率 number
            : Int =0
    var heartbeatRate // 心率偏快 number
            : String? = null
    var hrv // 心率变异性指数文字解释 string
            : String? = null
    var hrv_status // 心率变异指标 0-正常 1-偏大 2-偏小 number
            : String? = null
    var hrv_value // 心率变异指标 number
            : String? = null
    var ifPayment // 是否支付 0-未支付 1-已支付 number
            : String? = null
    var interpretationReportId // 人工解读id state-2时存在 number
            : String? = null
    var length // 时长 秒 number
            : String? = null
    var max // 最高心率 number
            : Int = 0
    var mentalPressure // 精神压力文字解释
            : String? = null
    var mental_status // 精神压力指数 精神压力指数(支付后存在) number
            : String? = null
    var mental_value // 精神压力指数(支付后存在) number
            : String? = null
    var min // 最低心率 number
            : Int = 0
    var normalList //  array<object>
            : ArrayList<TargetBean>? = null
    var normalRate // 正常心率 number
            : Int = 0
    var slowRate // 心率偏慢 number
            : String? = null

    //    public String state;// 状态 0-测量结束 1-数据干扰或导联脱落 2-解读完成 3-解读中 number
    var state // 0-正常 1-偏大 2-偏小 3-异常 number
            : String? = null
    var suggestion // 处置建议(支付后存在) string
            : String? = null
    var takeTime // 测量时间 number
            : String? = null
    var title: String? = null
}