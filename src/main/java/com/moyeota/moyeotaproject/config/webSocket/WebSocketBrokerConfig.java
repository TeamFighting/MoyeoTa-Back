//package com.moyeota.moyeotaproject.config.webSocket;
//
//import org.springframework.context.annotation.Configuration;
//import org.springframework.messaging.simp.config.MessageBrokerRegistry;
//import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
//import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
//import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;
//
//@Configuration
//@EnableWebSocketMessageBroker
//public class WebSocketBrokerConfig implements WebSocketMessageBrokerConfigurer {
//
//    //메시지 브로커를 구성하는 메서드로 유저가 메시지를 송수신 할 수 있게 중간에서 URL prefix(접두어)를 인식해 올바르게 송수신 할 수 있도록 중개해주는 역할
//    @Override
//    public void configureMessageBroker(MessageBrokerRegistry registry) {
//        //메시지 브로커가 /topic /queue 접두사가 붙은 대상에서 클라이언트로 메시지를 다시 전달
//        registry.enableSimpleBroker("/queue", "/topic");
//        //"/app"으로 시작하는 메시지가 메세지를 처리하는 메서드로 라우팅 되어야 한다고 정의
//        registry.setApplicationDestinationPrefixes("/app");
//    }
//
//    @Override
//    public void registerStompEndpoints(StompEndpointRegistry registry) {
//        registry.addEndpoint("/wss");
//    }
//
//}
