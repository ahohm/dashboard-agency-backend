package com.akkurad.dashboardagencybackend.dao;

import com.akkurad.dashboardagencybackend.model.ERole;
import com.akkurad.dashboardagencybackend.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface IRoleDao extends JpaRepository<Role, Long> {

    Optional<Role> findByName(ERole name);
}
