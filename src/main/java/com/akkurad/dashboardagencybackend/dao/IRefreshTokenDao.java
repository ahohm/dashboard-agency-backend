package com.akkurad.dashboardagencybackend.dao;

import com.akkurad.dashboardagencybackend.model.RefreshToken;
import com.akkurad.dashboardagencybackend.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;

import java.util.Optional;

public interface IRefreshTokenDao extends JpaRepository<RefreshToken, Long> {
    Optional<RefreshToken> findByToken(String token);
    @Modifying
    int deleteByUser(User user);
}
