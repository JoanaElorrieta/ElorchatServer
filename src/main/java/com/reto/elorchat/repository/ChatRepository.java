package com.reto.elorchat.repository;

import java.util.Date;
import java.util.Optional;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import com.reto.elorchat.model.enums.ChatTypeEnum;
import com.reto.elorchat.model.persistence.Chat;
import com.reto.elorchat.security.persistance.User;

public interface ChatRepository extends CrudRepository<Chat, Integer> {

	// CONSULTA SQL NATIVO
	// USER HAS PERMISSION TO ENTRY CHAT
	@Query(value = "SELECT COUNT(chat_Id) FROM user_chat WHERE chat_Id = :id AND user_id = :idUser AND deleted IS NULL", nativeQuery = true)
	Integer canEnterUserChat(@Param("id") Integer idChat, @Param("idUser") Integer idUser);

	// CONSULTA JPQL
	// USER HAS PERMISSION TO DELETE THE CHAT
	@Query(value = "SELECT COUNT(c) FROM Chat c WHERE c.id = :id AND c.adminId = :idUser")
	Integer canDeleteChat(@Param("id") Integer idChat, @Param("idUser") Integer idUser);

	// Consulta con KeyWords
	// EXISTS ON CHAT
	@Query(value = "SELECT CASE WHEN COUNT(*) > 0 THEN true ELSE false END FROM user_chat cu "
			+ "JOIN chats c ON cu.chat_id = c.id " + "JOIN users u ON cu.user_id = u.id "
			+ "WHERE c.id = :id AND u.id = :idUser AND cu.deleted IS NULL", nativeQuery = true)
	Long existsOnChat(@Param("id") Integer idChat, @Param("idUser") Integer idUser);

	@Query(value = "SELECT CASE WHEN COUNT(*) > 0 THEN true ELSE false END FROM user_chat u WHERE u.chat_id = :chatId AND u.user_id = :userId", nativeQuery = true)
	Long existsUserChatRelation(@Param("chatId") Integer chatId, @Param("userId") Integer userId);

	// CONSULTA SQL NATIVO
	// ADD TO CHAT (Add to user_chat)
	@Modifying
	@Transactional
	@Query(value = "INSERT INTO user_chat (chat_Id, user_Id, joined) VALUES (:id, :idUser, :joinDate)", nativeQuery = true)
	void addUserToChat(@Param("id") Integer idChat, @Param("idUser") Integer idUser, @Param("joinDate") Date joinDate);

	// CONSULTA SQL NATIVO
	// LEAVE FROM CHAT (Delete from user_chat)
	@Modifying
	@Transactional
	@Query(value = "UPDATE user_chat SET deleted = :deleteDate WHERE chat_Id = :id AND user_Id = :idUser", nativeQuery = true)
	void leaveChat(@Param("id") Integer idChat, @Param("idUser") Integer idUser, @Param("deleteDate") Date deleteDate);

	// Consulta con KeyWords
	// EXISTS BY NAME
	boolean existsByName(@Param("name") String name);

	// Consulta con KeyWords
	// FIND BY NAME
	Optional<Chat> findByName(String name);

	// SI QUIERO QUE ME DEVUELVA SOLO LOS QUE ESTAN REALMENTE
	@Query("SELECT c FROM Chat c LEFT JOIN FETCH c.users WHERE c.id = :chatId")
	Optional<Chat> findChatWithUsersById(@Param("chatId") Integer chatId);

	@Modifying
	@Transactional
	@Query("UPDATE Chat c SET c.deleted = :deleteDate WHERE c.id = :id")
	Integer updateDeleteById(@Param("id") Integer id, @Param("deleteDate") Date deleteDate);

	@Modifying
	@Transactional
	@Query(value = "UPDATE user_chat SET deleted = null, joined = :joinDate WHERE chat_id = :chatId AND user_id = :userId", nativeQuery = true)
	void updateJoinDateInUserChat(@Param("chatId") Integer chatId, @Param("userId") Integer userId,
			@Param("joinDate") Date joinDate);

	// TODO CORREGIR Check if chat has been deleted
	@Query("SELECT CASE WHEN COUNT(c) > 0 THEN true ELSE false END FROM Chat c WHERE c.id = :id AND c.deleted IS NOT NULL")
	boolean isChatDeleted(@Param("id") Integer chatId);

	@Query(value = "SELECT CASE WHEN COUNT(*) > 0 THEN true ELSE false END FROM user_chat u WHERE u.chat_id = :chatId AND u.user_id = :userId AND u.deleted IS NOT NULL", nativeQuery = true)
	Long isDeletedUserChat(@Param("chatId") Integer chatId, @Param("userId") Integer userId);

	@Query(value = "SELECT CASE WHEN COUNT(*) > 0 THEN true ELSE false END FROM user_chat u WHERE u.chat_id = :chatId AND u.user_id = :userId AND u.deleted IS NULL", nativeQuery = true)
	Long isUserAlreadyOnChat(@Param("chatId") Integer chatId, @Param("userId") Integer userId);

	@Query("SELECT c FROM Chat c WHERE c.id > :givenId AND c.type = :public")
	Iterable<Chat> findAllPublicChatsCreatedAfterId(@Param("givenId") Integer givenId,
			@Param("public") ChatTypeEnum pub);

	@Query("SELECT c FROM Chat c WHERE c.deleted IS NULL")
	Iterable<Chat> findAllChats();

	@Query("SELECT c FROM Chat c WHERE c.id > :givenId AND c.deleted IS NULL")
	Iterable<Chat> findAllUserChatsCreatedAfterId(@Param("givenId") Integer givenId);

	@Modifying
	@Transactional
	@Query(value = "UPDATE user_chat SET deleted = :deleteDate WHERE chat_Id = :idChat", nativeQuery = true)
	void deleteUserChatRelations(@Param("idChat") Integer idChat, @Param("deleteDate") Date deleteDate);
	
	@Query("SELECT c FROM Chat c WHERE c.id = :chatId")
	Optional<Chat> findByIdUpdatedChat(@Param("chatId") Integer chatId);

}
