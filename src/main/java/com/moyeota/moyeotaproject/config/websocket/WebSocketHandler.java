package com.moyeota.moyeotaproject.config.websocket;

import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import com.moyeota.moyeotaproject.domain.location.Location;
import com.moyeota.moyeotaproject.domain.location.LocationRepository;

@Component
public class WebSocketHandler extends TextWebSocketHandler {

	private final LocationRepository locationRepository;
	private static final ConcurrentHashMap<String, WebSocketSession> CLIENTS = new ConcurrentHashMap<String, WebSocketSession>();

	public WebSocketHandler(LocationRepository locationRepository) {
		this.locationRepository = locationRepository;
	}

	@Override
	public void afterConnectionEstablished(WebSocketSession session) throws Exception {
		CLIENTS.put(session.getId(), session);
	}

	@Override
	public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
		CLIENTS.remove(session.getId());
	}

	@Override
	protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
		String id = session.getId();  //메시지를 보낸 아이디
		Location location = Location.builder()
			.sessionId(id)
			.position(message.getPayload())
			.build();
		locationRepository.save(location);

		String responseMessage = "메시지 잘 받았소, sessionId: " + id;
		session.sendMessage(new TextMessage(responseMessage));
	}

}
