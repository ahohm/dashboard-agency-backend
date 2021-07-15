package com.akkurad.dashboardagencybackend.dao;

import com.akkurad.dashboardagencybackend.model.Application;
import com.akkurad.dashboardagencybackend.model.EApplicationType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface IApplicationDao extends JpaRepository<Application, Long> {
    Optional<Application> findByName(EApplicationType name);
}
