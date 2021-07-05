package com.akkurad.dashboardagencybackend.dao;

import com.akkurad.dashboardagencybackend.model.Organization;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IOrganizationDao extends JpaRepository<Organization, Long> {
}
