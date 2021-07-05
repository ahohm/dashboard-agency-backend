package com.akkurad.dashboardagencybackend.service;

import com.akkurad.dashboardagencybackend.model.Organization;

import java.util.List;
import java.util.Optional;

public interface IOrganizationService {
    Organization save(Organization organization) throws Exception;
    List<Organization> findAll();
    Optional<Organization> findById(Long id);
    Organization findByUsername(String username);
    void deleteById(Long id);
    Organization update(Organization organization);
}
