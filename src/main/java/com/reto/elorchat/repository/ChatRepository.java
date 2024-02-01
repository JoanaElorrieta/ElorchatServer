package com.reto.elorchat.repository;

import java.util.Date;
import java.util.Optional;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import com.reto.elorchat.model.persistence.Chat;

public interface ChatRepository extends CrudRepository<Chat, Integer>{

	//CONSULTA SQL NATIVO
	//USER HAS PERMISSION TO ENTRY CHAT
	@Query(value ="SELECT COUNT(chat_Id) FROM user_chat WHERE chat_Id = :id AND user_id = :idUser", nativeQuery = true)
	Integer canEnterUserChat(@Param("id") Integer idChat, @Param("idUser") Integer idUser);

	//CONSULTA JPQL
	//USER HAS PERMISSION TO DELETE THE CHAT
	@Query(value ="SELECT COUNT(c) FROM Chat c WHERE c.id = :id AND c.adminId = :idUser")
	Integer canDeleteChat(@Param("id") Integer idChat, @Param("idUser") Integer idUser);	

	//Consulta con KeyWords
	//EXISTS ON CHAT
	boolean existsByIdAndUsers_Id(@Param("id") Integer idChat, @Param("idUser") Integer idUser);

	//CONSULTA SQL NATIVO
	//ADD TO CHAT (Add to user_chat)
	@Modifying
	@Transactional
	@Query(value = "INSERT INTO user_chat (chat_Id, user_Id, joined) VALUES (:id, :idUser, :joinDate)", nativeQuery = true)
	void addUserToChat(@Param("id") Integer idChat, @Param("idUser") Integer idUser, @Param("joinDate") Date joinDate);

	//CONSULTA SQL NATIVO
	//LEAVE FROM CHAT (Delete from user_chat)
	@Modifying
	@Transactional
	@Query(value = "UPDATE user_chat SET deleted = :deleteDate WHERE chat_Id = :id AND user_Id = :idUser", nativeQuery = true)
	void leaveChat(@Param("id") Integer idChat, @Param("idUser") Integer idUser, @Param("deleteDate") Date deleteDate);

	//	@Modifying
	//	@Transactional
	//	@Query(value = "DELETE FROM user_chat WHERE chat_Id = :id AND user_Id = :idUser", nativeQuery = true)
	//	void leaveChat(@Param("id") Integer idChat, @Param("idUser") Integer idUser);

	//Consulta con KeyWords
	//EXISTS BY NAME
	boolean existsByName(@Param("name")String name);

	//Consulta con KeyWords
	//FIND BY NAME
	Optional<Chat> findByName(String name);

	@Query("SELECT c FROM Chat c LEFT JOIN FETCH c.users WHERE c.id = :chatId")
	Optional<Chat> findChatWithUsersById(@Param("chatId") Integer chatId);

	@Modifying
	@Transactional
	@Query("UPDATE Chat c SET c.deleted = :deleteDate WHERE c.id = :id")
	void updateDeleteById(@Param("id") Integer id, @Param("deleteDate") Date deleteDate);

	@Query("SELECT c FROM Chat c WHERE c.id > :givenId")
	Iterable<Chat> findAllChatsCreatedAfterId(@Param("givenId") Integer givenId);

	//TODO CORREGIR Check if chat has been deleted
	@Query("SELECT CASE WHEN COUNT(c) > 0 THEN true ELSE false END FROM Chat c WHERE c.id = :id AND c.deleted IS NOT NULL")
	boolean isChatDeleted(@Param("id") Integer chatId);
}
