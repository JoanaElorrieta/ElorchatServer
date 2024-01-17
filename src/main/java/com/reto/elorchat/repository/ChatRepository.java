package com.reto.elorchat.repository;

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
	@Query(value = "INSERT INTO user_chat (chat_Id, user_Id) VALUES (:id, :idUser)", nativeQuery = true)
	void addUserToChat(@Param("id") Integer idChat, @Param("idUser") Integer idUser);

	//CONSULTA SQL NATIVO
	//LEAVE FROM CHAT (Delete from user_chat)
	@Modifying
	@Transactional
	@Query(value = "DELETE FROM user_chat WHERE chat_Id = :id AND user_Id = :idUser", nativeQuery = true)
	void leaveChat(@Param("id") Integer idChat, @Param("idUser") Integer idUser);



}
