package com.akkurad.dashboardagencybackend.service;

import com.akkurad.dashboardagencybackend.model.Application;
import lombok.Data;

import java.util.List;
import java.util.Optional;

public interface IApplicationService {
    Application save(Application application) ;
    Application getOne(String id) ;
    List<Application> findAll();
    void deleteById(String id) throws Exception;
    Application update(Application application);
}
