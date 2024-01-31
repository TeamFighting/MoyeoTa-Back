package com.moyeota.moyeotaproject.config.webSocket;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        // 메시지를 구독하는 요청 url -> 메시지 수신
        registry.enableSimpleBroker("/sub");
        // 메시지를 발행하는 요청 url -> 메시지 송신
        registry.setApplicationDestinationPrefixes("/pub");
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        // stomp 접속 url -> /wss-stomp 이 경로로 서버와 handshake
        registry.addEndpoint("/wss-stomp")
                .setAllowedOriginPatterns("*") //허용할 origin 패턴 지정
                .withSockJS(); //SockJS를 사용하여 브라우저에서 websocket을 지원하지 않을 경우 대체 옵션을 지원
    }
}