package moe.snowflake.mf

import moe.snowflake.mf.config.ForwardConfig
import net.mamoe.mirai.console.plugin.jvm.JvmPluginDescriptionBuilder
import net.mamoe.mirai.console.plugin.jvm.KotlinPlugin
import net.mamoe.mirai.console.plugin.jvm.reloadPluginConfig
import net.mamoe.mirai.contact.Group
import net.mamoe.mirai.event.GlobalEventChannel
import net.mamoe.mirai.event.events.GroupMessageEvent
import net.mamoe.mirai.message.data.*

object MessageForwardCore : KotlinPlugin(
    JvmPluginDescriptionBuilder(
        "moe.snowflake.mf",
        "0.1.0"
    ).name("messageForward")
        .author("snowflake")
        .build()
) {

    override fun onDisable() {
        logger.info("信息转发关闭中")
    }

    private val config = ForwardConfig;

    // 简洁
    override fun onEnable() {
        logger.info("信息转发启动中")
        // config
        reloadPluginConfig(config)
        // register message listener
        GlobalEventChannel.parentScope(this).subscribeAlways<GroupMessageEvent> { event ->
            forwardMessage(event);
        }
    }


    // 处理消息事件
    private suspend fun forwardMessage(event: GroupMessageEvent) {
        if (event.message is FileMessage) return

        config.sendGroups.forEach { targetID ->
            if (event.group.id == targetID.toLong()) {
                sendMessage(event)
            }
        }
    }

    // 发送转发信息
    private suspend fun sendMessage(event: GroupMessageEvent) {
        config.revGroup.forEach { revGroupID ->
            runCatching {
                val group = event.bot.getGroup(revGroupID.toLong())

                if (group != null){

                    when (config.mode)
                    {
                        1 -> group.sendMessage(event.message.toForwardMessage(event.sender))
                        2 -> group.sendMessage(MessageChainBuilder().append("${event.group.name} - ${event.senderName}\n").append(event.message).build())
                        else -> group.sendMessage(MessageChainBuilder().append("${event.group.name} - ${event.senderName}\n").append(event.message).build())
                    }

                }
            }.onSuccess {
            }.onFailure {
                exception -> exception.printStackTrace()
            }

        }
    }


}