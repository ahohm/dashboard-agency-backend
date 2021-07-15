package com.akkurad.dashboardagencybackend.controller;

import com.akkurad.dashboardagencybackend.dao.IAuthenticationTokenDao;
import com.akkurad.dashboardagencybackend.model.AuthenticationToken;
import com.akkurad.dashboardagencybackend.payload.request.LogOutRequest;
import com.akkurad.dashboardagencybackend.payload.response.MessageResponse;
import com.akkurad.dashboardagencybackend.security.service.RefreshTokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@RestController
@RequestMapping("api")
public class LogoutController {

    @Autowired
    private RefreshTokenService refreshTokenService;
    @Autowired
    private IAuthenticationTokenDao iAuthenticationTokenDao;

    @PreAuthorize("hasRole('MODERATOR') or hasRole('ADMIN')")
    @PostMapping("/logout")
    public ResponseEntity<?> logoutUser(@Valid @RequestBody LogOutRequest logOutRequest, HttpServletRequest request) {
        String token = request.getHeader("Authorization").substring(7);
        refreshTokenService.deleteByUserId(logOutRequest.getUserId());
        iAuthenticationTokenDao.save(new AuthenticationToken(token));
        return ResponseEntity.ok(new MessageResponse("Log out successful!"));
    }
}
