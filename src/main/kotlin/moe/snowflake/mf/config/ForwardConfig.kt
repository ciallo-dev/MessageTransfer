package moe.snowflake.mf.config

import net.mamoe.mirai.console.data.AutoSavePluginConfig
import net.mamoe.mirai.console.data.ValueDescription
import net.mamoe.mirai.console.data.value

object ForwardConfig : AutoSavePluginConfig("config.") {

    @ValueDescription("监控群的群号")
    var sendGroups : List<String> by value(listOf("xx", "xxx"))

    @ValueDescription("接收群的群号")
    var revGroup : List<String> by value(listOf("xxx"))

    @ValueDescription("模式 1. 合并转发 2.单条原文转发")
    var mode : Int by value(2)

}