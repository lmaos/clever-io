编解码器的实现部分.

需要兼容 Tcp,WebSocket的协议.

传输协议:
```
TCP: BinaryPacket
WebSocket: JsonPacket
```

---

业务中需要的协议结构:

```
请求: Request
应答: Response
推送: Push
```