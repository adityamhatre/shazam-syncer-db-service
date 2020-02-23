package com.adityamhatre.db_service.repository;

import com.adityamhatre.db_service.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, String> {
	List<User> findUsersByBootStrapped(boolean isBootStrapped);
}
