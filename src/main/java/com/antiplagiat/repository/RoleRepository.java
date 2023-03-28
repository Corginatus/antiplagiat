package com.antiplagiat.repository;


import com.antiplagiat.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

public interface RoleRepository extends JpaRepository<Role, Long>{

}

//public interface RoleRepository extends JpaRepository<Role, Long> {
//}
