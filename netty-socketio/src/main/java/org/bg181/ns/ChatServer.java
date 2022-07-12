package org.bg181.ns;

import com.corundumstudio.socketio.Configuration;
import com.corundumstudio.socketio.SocketIOServer;

/**
 * @author Sam Lu
 * @date 2022/3/13
 */
public class ChatServer {

    public static void main(String[] args) {
        Configuration config = new Configuration();
        config.setHostname("localhost");
        config.setPort(9092);

        SocketIOServer server = new SocketIOServer(config);
        // 监听连接事件
        server.addConnectListener(client ->
                System.out.println(client.getSessionId() + " connected."));
        // 监听断开连接事件
        server.addDisconnectListener(client ->
                System.out.println(client.getSessionId() + " disconnected."));
        // 监听自定义事件
        server.addEventListener("message", Message.class, (client, message, ackRequest) -> {
                    System.out.println("Receive message [" + client.getSessionId() + "] -> "
                            + message.getUserName() + ": " + message.getMessage());
                    server.getBroadcastOperations().sendEvent("broadcast", message);
                }
        );
        server.start();
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            server.stop();
        }));

        while (true) {
            try {
                Thread.sleep(50);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

}
