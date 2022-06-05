package com.behl.pehchan.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.behl.pehchan.entity.User;

@Repository
public interface UserRepository extends JpaRepository<User, UUID> {

}