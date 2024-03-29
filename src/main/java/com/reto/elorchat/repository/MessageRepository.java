package com.reto.elorchat.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.reto.elorchat.model.enums.TextTypeEnum;
import com.reto.elorchat.model.persistence.Message;

public interface MessageRepository extends CrudRepository<Message,Integer>{

	@Query("SELECT m FROM Message m WHERE m.chatId = :chatId")
	Iterable<Message> findAllMessagesByChatId(@Param("chatId") Integer chatId);

	@Query("SELECT m FROM Message m WHERE m.chatId IN (:userChatList)")
	Iterable<Message> findAllMessagesOfUser(@Param("userChatList") List<Integer> userChatIds);    

	@Query("SELECT m FROM Message m WHERE m.chatId IN (:userChatList) AND m.id > :givenId")
	Iterable<Message> findAllMessagesOfUserCreatedAfterId(@Param("givenId") Integer givenId, @Param("userChatList") List<Integer> userChatList);

	// New query method to retrieve messages with a specific textType
	@Query("SELECT m FROM Message m WHERE m.textType = :textType")
	Iterable<Message> findAllMessagesByType(@Param("textType") TextTypeEnum textType);



	//    @Modifying
	//    @Query("UPDATE Message m SET m.text = :newText WHERE m.id = :messageId")
	//    Optional<Message> updateTextForMessage(@Param("newText") String newText, @Param("messageId") Integer messageId);

}
