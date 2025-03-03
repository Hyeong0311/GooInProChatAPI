package org.hyeong.gooinprochatapi.chatroom.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.hyeong.gooinprochatapi.chatmessage.repository.ChatMessageRepository;
import org.hyeong.gooinprochatapi.chatroom.domain.ChatRoomEntity;
import org.hyeong.gooinprochatapi.chatroom.domain.Participant;
import org.hyeong.gooinprochatapi.chatroom.dto.ChatRoomAddDTO;
import org.hyeong.gooinprochatapi.chatroom.dto.ChatRoomFindDTO;
import org.hyeong.gooinprochatapi.chatroom.dto.ChatRoomListDTO;
import org.hyeong.gooinprochatapi.chatroom.dto.ChatRoomOutDTO;
import org.hyeong.gooinprochatapi.chatroom.repository.ChatRoomRepository;
import org.hyeong.gooinprochatapi.common.dto.PageRequestDTO;
import org.hyeong.gooinprochatapi.common.dto.PageResponseDTO;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Log4j2
@Transactional
@RequiredArgsConstructor
public class ChatRoomService {

    private final ChatRoomRepository chatRoomRepository;
    private final ChatMessageRepository chatMessageRepository;
    private final MongoTemplate mongoTemplate;

    //채팅방 새로운 참가자 만들기
    private Participant createParticipant(String email) {

        return Participant.builder()
                .email(email)
                .joinedAt(Date.from(Instant.now()))
                .build();
    }

    //채팅방 참가자 수 return(leftAt 값 있는 참가자 제외)
    private int countParticipants(List<Participant> participants) {
        return (int) participants.stream()
                .filter(participant -> participant.getLeftAt() == null)
                .count();
    }

    //채팅방 새로 만들기
    public ChatRoomEntity addChatRoomService(ChatRoomAddDTO chatRoomAddDTO) {

        List<Participant> participants = chatRoomAddDTO.getParticipants();
        participants.forEach(participant
                -> participant.setJoinedAt(Date.from(Instant.now())));

        String roomName;

        if(chatRoomAddDTO.getRoomName() != null) {

            roomName = chatRoomAddDTO.getRoomName();
        } else {

            List<String> names = chatRoomAddDTO.getParticipants()
                    .stream().map(Participant::getEmail).collect(Collectors.toList());

            roomName = names.stream().collect(Collectors.joining(", "));
        }

        ChatRoomEntity chatRoomEntity = ChatRoomEntity.builder()
                .roomName(roomName)
                .roomCreatedAt(Date.from(Instant.now()))
                .roomUpdatedAt(Date.from(Instant.now()))
                .createdBy(chatRoomAddDTO.getCreatedBy())
                .participants(participants)
                .build();

        return chatRoomRepository.save(chatRoomEntity);
    }

    //채팅방 찾기(1대 1 채팅)
    public ChatRoomEntity findChatRoomService(ChatRoomFindDTO chatRoomFindDTO) {

        Query query = new Query();
        // participants 배열이 정확히 2개이며, 두 개의 이메일 포함 하는 조건 추가
        Criteria criteria = new Criteria().andOperator(
                Criteria.where("participants").size(2),
                Criteria.where("participants").elemMatch(Criteria.where("email").is(chatRoomFindDTO.getSenderEmail())),
                Criteria.where("participants").elemMatch(Criteria.where("email").is(chatRoomFindDTO.getRecipientEmail()))
        );

        query.addCriteria(criteria);

        ChatRoomEntity chatRoom = mongoTemplate.findOne(query, ChatRoomEntity.class);

        //채팅방 없으면 새로 만들기
        if (chatRoom == null) {

            ChatRoomAddDTO chatRoomAddDTO = new ChatRoomAddDTO();

            List<Participant> participants =
                    List.of(createParticipant(chatRoomFindDTO.getSenderEmail()), createParticipant(chatRoomFindDTO.getRecipientEmail()));

            chatRoomAddDTO.setCreatedBy(chatRoomFindDTO.getSenderEmail());
            chatRoomAddDTO.setParticipants(participants);

            chatRoom = addChatRoomService(chatRoomAddDTO);

            if(chatRoom == null)
                throw new RuntimeException("채팅방 생성 실패");
        } else {

            Participant tmp = chatRoom.getParticipants().stream()
                    .filter(participant ->
                            chatRoomFindDTO.getSenderEmail().equals(participant.getEmail())
                                    && participant.getLeftAt() != null
                    )
                    .findFirst()
                    .orElse(null);

            if(tmp != null) {

                List<Participant> participants = chatRoom.getParticipants();

                participants.remove(tmp);
                participants.add(new Participant(chatRoomFindDTO.getSenderEmail(), Date.from(Instant.now())));

                chatRoom.setParticipants(participants);

                return chatRoomRepository.save(chatRoom);
            }
        }

        return chatRoom;
    }

    //내 채팅방 리스트(페이징 포함)
    public PageResponseDTO<ChatRoomListDTO> chatRoomListService(
            String email, PageRequestDTO pageRequestDTO) {

        int page = pageRequestDTO.getPage();
        int size = pageRequestDTO.getSize();
        int skip = (page - 1) * size;

        Criteria criteria = new Criteria().andOperator(
                Criteria.where("participants").elemMatch(
                        new Criteria().andOperator(
                                Criteria.where("email").is(email),
                                Criteria.where("leftAt").exists(false)
                        )
                )
        );


        Query query = new Query(criteria)
                .with(Sort.by(Sort.Order.desc("sentAt")))  // sentAt 기준 내림차순 정렬
                .skip(skip)
                .limit(size);

        log.info(query);

        List<ChatRoomListDTO> dtoList = mongoTemplate.find(query, ChatRoomListDTO.class);
        int totalCount = (int)mongoTemplate.count(query, ChatRoomListDTO.class);    //limit 는 count 에 영향 안줌

        return new PageResponseDTO<>(dtoList, pageRequestDTO, totalCount);
    }

    //채팅방 나가기
    public String chatRoomOutService(ChatRoomOutDTO chatRoomOutDTO) {

        ChatRoomEntity chatRoomEntity = chatRoomRepository.findById(chatRoomOutDTO.getRoomId())
                .orElseThrow(() -> new RuntimeException("findChatRoom failed"));

        List<Participant> participants = chatRoomEntity.getParticipants();

        //참여자가 1 이하일 때 메세지, 채팅방 Hard Delete
        if(countParticipants(participants) <= 1) {

            chatMessageRepository.deleteAllByRoomId(chatRoomOutDTO.getRoomId());
            chatRoomRepository.deleteById(chatRoomOutDTO.getRoomId());

            return "Delete All Messages and ChatRoom";
        }

        //Email 값 찾아서 leftAt 값 넣기
        participants.forEach(participant -> {
            if(participant.getEmail().equals(chatRoomOutDTO.getEmail()))
                participant.setLeftAt(Date.from(Instant.now()));
        });

        chatRoomEntity.setParticipants(participants);

        chatRoomRepository.save(chatRoomEntity);

        return "Chat Room left Successfully";
    }
}
