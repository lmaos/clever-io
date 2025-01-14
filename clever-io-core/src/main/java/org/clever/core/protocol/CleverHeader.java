package org.clever.core.protocol;

import lombok.Getter;
import lombok.Setter;

/**
 * @author clever.cat
 *
 * 包属性. 简要信息.
 */
@Getter
@Setter
public class CleverHeader {

    public CleverHeader() {}
    public CleverHeader(CleverHeader header) {
        if (header != null) {
            // 克隆
            this.status = header.status;
            this.msgId = header.msgId;
            this.userId = header.userId;
            this.roomId = header.roomId;
            this.token = header.token;
            this.contentType = header.contentType;
        }
    }

    /**
     * 状态 (应答是必填)
     */
    private String status;
    /**
     * request资源地址 或 push 的 topic
     */
    private String uri;

    /**
     * 消息ID (必填)
     */
    private String msgId; // 消息ID
    /**
     * 当前用户 (必填)
     */
    private String userId;
    /**
     * 消息在哪个房间. 如果是群聊, 则为群聊ID, 如果是私聊, 则为空
     */
    private String roomId;
    /**
     * 令牌, 需要UserId 和 RoomId 验证/获取 (必填)
     */
    private String token;
    /** 消息结构 (可选)
     * 例如:
     *  binary: 二进制数据
     *  json: json数据
     *  null: 由程序选择默认结构.
     *  ---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
     * 后续可以有开发者自行定义消息结构的实现.
     */
    private String contentType;



}
