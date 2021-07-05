package com.akkurad.dashboardagencybackend.dao;

import com.akkurad.dashboardagencybackend.model.Owner;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IOwnerDao extends JpaRepository<Owner, String> {
    Owner findByUsername(String username);
}
