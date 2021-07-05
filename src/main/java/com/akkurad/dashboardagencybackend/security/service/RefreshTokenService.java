package com.akkurad.dashboardagencybackend.security.service;

import com.akkurad.dashboardagencybackend.dao.IRefreshTokenDao;
import com.akkurad.dashboardagencybackend.dao.IUserDao;
import com.akkurad.dashboardagencybackend.exception.TokenRefreshException;
import com.akkurad.dashboardagencybackend.model.RefreshToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@Service
public class RefreshTokenService {

    @Value("${jwt.RefreshExpirationMs}")
    private Long refreshTokenDurationMs;

    @Autowired
    private IRefreshTokenDao iRefreshTokenDao;

    @Autowired
    private IUserDao iUserDao;

    public Optional<RefreshToken> findByToken(String token) {
        return iRefreshTokenDao.findByToken(token);
    }

    public RefreshToken createRefreshToken(String userId) {
        RefreshToken refreshToken = new RefreshToken();

        refreshToken.setUser(iUserDao.findById(userId).get());
        refreshToken.setExpiryDate(Instant.now().plusMillis(refreshTokenDurationMs));
        refreshToken.setToken(UUID.randomUUID().toString());

        refreshToken = iRefreshTokenDao.save(refreshToken);
        return refreshToken;
    }

    public RefreshToken verifyExpiration(RefreshToken token) {
        if (token.getExpiryDate().compareTo(Instant.now()) < 0) {
            iRefreshTokenDao.delete(token);
            throw new TokenRefreshException(token.getToken(), "Refresh token was expired. Please make a new signin request");
        }

        return token;
    }

    @Transactional
    public int deleteByUserId(String userId) {
        return iRefreshTokenDao.deleteByUser(iUserDao.findById(userId).get());
    }
}
