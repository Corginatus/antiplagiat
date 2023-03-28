package com.antiplagiat.repository;

import com.antiplagiat.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

public interface UserRepository extends JpaRepository<User, Long>{
    User findByUsername(String username);
}

//public interface UserRepository extends JpaRepository<User, Long> {
//    User findByUsername(String username);
//}
