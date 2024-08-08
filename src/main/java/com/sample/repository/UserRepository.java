package com.sample.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.sample.model.User;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    @Query(value = "SELECT * FROM user WHERE username=? AND modified_type='INSERTED'", nativeQuery = true)
    Optional<User> findActiveByUsername(String username);

    @Query(value = "SELECT * FROM user WHERE modified_type='INSERTED'", nativeQuery = true)
    List<User> findAllActive();

    @Query(value = "SELECT COUNT(*) FROM user WHERE modified_type='INSERTED'", nativeQuery = true)
    int countAllActive();
}
