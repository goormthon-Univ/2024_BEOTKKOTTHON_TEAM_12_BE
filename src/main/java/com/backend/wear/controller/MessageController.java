package com.backend.wear.controller;

import com.backend.wear.dto.chat.ChatMessageDto;
import com.backend.wear.service.MessageService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;

@Controller
@RequiredArgsConstructor
@CrossOrigin(origins={"http://43.201.189.171:8080", "http://localhost:5173",
        "http://wear-frontend.s3-website.ap-northeast-2.amazonaws.com","http://localhost:8080"})
public class MessageController {

    private final SimpMessagingTemplate simpMessagingTemplate;
    private final MessageService messageService;
    private static final Logger log = LoggerFactory.getLogger(MessageController.class);

    // 새로운 사용자가 웹 소켓을 연결할 때 실행됨
    // @EventListener은 한개의 매개변수만 가질 수 있다.
//    @EventListener
//    public void handleWebSocketConnectListener(SessionConnectEvent event) {
//        log.info("Received a new web socket connection");
//    }

    // 메시지 보내기
    // /pub/api/chat/message/{chatRoomId}
    @MessageMapping(value="/api/chat/message/{chatRoomId}")
    public void sendMyMessage (@DestinationVariable("chatRoomId")Long chatRoomId,
                               ChatMessageDto.ChatRoomMessageDto dto) {

        log.info("채팅방 아이디: "+chatRoomId);

        log.info("로그인 사용자 아이디: "+dto.getSenderId());
        log.info("보낸사람 아이디: "+dto.getSenderId());
        log.info("메시지 내용: "+dto.getMessage());
        log.info("보낸사람 타입: "+dto.getUserType());
        log.info("보낸 시간: "+dto.getTimestamp());
        log.info("내가 보낸건지 여부: "+ dto.isMine());

        // 채팅 상대방 아이디 찾기
        Long partnerId=messageService.getPartnerId(chatRoomId,dto.getSenderId(), dto.getUserType());
        log.info("채팅 상대방 아이디: "+partnerId);

        // 메시지 저장 로직
        messageService.saveMessage(chatRoomId, dto.getSenderId(), dto);

        dto.setMine(false);

        // 구독자들에게 메시지 전달
        // /sub/api/chat/room/{chatRoomId}
        simpMessagingTemplate.convertAndSend("/sub/api/chat/room/"+chatRoomId, dto);
        log.info("상대편 입장 보낸건지 여부: "+ dto.isMine());
    }
}