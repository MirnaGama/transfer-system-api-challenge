package com.mirna.transferapi.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import com.mirna.transferapi.domain.entities.User;

public interface UserRepository extends JpaRepository<User, Long>{

	Optional<User> findUserByDocument(@Param("screenName") String document);
	
	Optional<User> findUserByEmail(@Param("email") String email);
}
