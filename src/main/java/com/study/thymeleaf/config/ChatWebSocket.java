package com.study.thymeleaf.config;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.concurrent.CopyOnWriteArrayList;
 
/**
 * @author SONG
 * @date 2019/4/20 21:41
 */
@Component
@ServerEndpoint("/websocket") // 客户端访问的url
public class ChatWebSocket {
  //  concurrent包的线程安全 set
  // 用来存放每个客户端对应的chatWebSocket对象
  private static CopyOnWriteArrayList<ChatWebSocket> chatWebSockets = new CopyOnWriteArrayList<>();
  // 与某个客户端的连接会话对象.通过session给客户端发送数据
  private Session session;
 
  /**
   * 连接建立成功回调方法
   *
   * @param session
   */
  @OnOpen
  public void onOpen(Session session) {
    this.session = session;
    chatWebSockets.add(this);
    System.out.println("有新连接加入, 当前连接数为: " + chatWebSockets.size());
    // 发送消息
    try {
      sendMessage("当前共有 " + chatWebSockets.size() + " 位用户在线");
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
 
  /**
   * 连接关闭回调方法
   */
  @OnClose
  public void onClose() {
    chatWebSockets.remove(this);
    sendInfo("有一个连接关闭! 当前在线人数: " + chatWebSockets.size());
    System.out.println("有一个连接关闭! 当前在线人数: " + chatWebSockets.size());
  }
 
  /**
   * 收到当前客户端消息后的回调方法
   *
   * @param message
   * @param session
   */
  @OnMessage
  public void onMessage(String message, Session session) {
    System.out.println("来自客户端的消息: " + message);
    sendInfo(message);
  }
 
  @OnError
  public void onError(Session session, Throwable throwable) {
    System.out.println("发生错误");
    throwable.printStackTrace();
  }
 
  /**
   * 通过连接会话对象发送数据
   *
   * @param message
   * @throws IOException
   */
  private void sendMessage(String message) throws IOException {
    this.session.getBasicRemote().sendText(message);
  }
 
  /**
   * 群发自定义信息
   *
   * @param message
   */
  private void sendInfo(String message) {
    for (ChatWebSocket item : chatWebSockets) {
      try {
        item.sendMessage(message);
      } catch (IOException e) {
        continue;
      }
    }
  }
}