package com.akkurad.dashboardagencybackend.dao;

import com.akkurad.dashboardagencybackend.model.AuthenticationToken;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
@EnableRedisRepositories
public interface IAuthenticationTokenDao extends CrudRepository<AuthenticationToken, String> {
}
