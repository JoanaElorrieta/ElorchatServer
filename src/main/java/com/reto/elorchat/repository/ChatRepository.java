package com.reto.elorchat.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.reto.elorchat.model.persistence.Chat;

public interface ChatRepository extends CrudRepository<Chat, Integer>{

	//USER HAS PERMISSION TO ENTRY CHAT
	//Long countByUserId(Integer userId);
	Integer countByUsers_IdAndId(@Param("id") Integer idChat, @Param("idUser") Integer idUser);

	//USER HAS PERMISSION TO DELETE THE CHAT
	@Query(value ="SELECT COUNT(c) FROM Chat c WHERE c.id = :id AND c.adminId = :idUser")
	Integer countByIdAndAdminId(@Param("id") Integer idChat, @Param("idUser") Integer idUser);

	//DELETE FROM CHAT
	//void deleteByIdAndUsers_Id(Integer idChat, Integer idUser);
	void deleteByUsers_IdAndId(Integer idChat, Integer idUser);


	//EXISTS ON CHAT
	boolean existsByIdAndUsers_Id(@Param("id") Integer idChat, @Param("idUser") Integer idUser);

}
