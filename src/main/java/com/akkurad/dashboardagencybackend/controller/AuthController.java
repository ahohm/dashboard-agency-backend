package com.akkurad.dashboardagencybackend.controller;

import com.akkurad.dashboardagencybackend.dao.IRoleDao;
import com.akkurad.dashboardagencybackend.dao.IAuthenticationTokenDao;
import com.akkurad.dashboardagencybackend.dao.IUserDao;
import com.akkurad.dashboardagencybackend.exception.TokenRefreshException;
import com.akkurad.dashboardagencybackend.model.*;
import com.akkurad.dashboardagencybackend.payload.request.LogOutRequest;
import com.akkurad.dashboardagencybackend.payload.request.LoginRequest;
import com.akkurad.dashboardagencybackend.payload.request.SignupRequest;
import com.akkurad.dashboardagencybackend.payload.request.TokenRefreshRequest;
import com.akkurad.dashboardagencybackend.payload.response.JwtResponse;
import com.akkurad.dashboardagencybackend.payload.response.MessageResponse;
import com.akkurad.dashboardagencybackend.payload.response.TokenRefreshResponse;
import com.akkurad.dashboardagencybackend.security.jwt.JwtUtils;
import com.akkurad.dashboardagencybackend.security.service.RefreshTokenService;
import com.akkurad.dashboardagencybackend.security.service.UserDetailsImpl;
import com.akkurad.dashboardagencybackend.service.IOwnerService;
import io.jsonwebtoken.Jwts;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/auth")
@AllArgsConstructor
@Slf4j
public class AuthController {

    private AuthenticationManager authenticationManager;
    private IUserDao userRepository;
    private IRoleDao roleRepository;
    private PasswordEncoder encoder;
    private JwtUtils jwtUtils;
    private RefreshTokenService refreshTokenService;
    private IAuthenticationTokenDao iAuthenticationTokenDao;
    private IOwnerService iOwnerService;

    @PostMapping("/signin")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {

        Authentication authentication = authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

        String jwt = jwtUtils.generateJwtToken(userDetails);

        List<String> roles = userDetails.getAuthorities().stream().map(item -> item.getAuthority())
                .collect(Collectors.toList());

        RefreshToken refreshToken = refreshTokenService.createRefreshToken(userDetails.getId());

        return ResponseEntity.ok(new JwtResponse(jwt,
                refreshToken.getToken(),
                userDetails.getId(),
                userDetails.getUsername(),
                userDetails.getEmail(),
                roles));
    }

    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@Valid @RequestBody SignupRequest signUpRequest) {
        if (userRepository.existsByUsername(signUpRequest.getUsername())) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: Username is already taken!"));
        }

        if (userRepository.existsByEmail(signUpRequest.getEmail())) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: Email is already in use!"));
        }

        // Create new user' account
        User user = new User(signUpRequest.getUsername(),
                signUpRequest.getEmail(),
                encoder.encode(signUpRequest.getPassword()));

        Set<String> strRoles = signUpRequest.getRole();
        Set<Role> roles = new HashSet<>();

        if (strRoles == null) {
            Role userRole = roleRepository.findByName(ERole.ROLE_USER)
                    .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
            roles.add(userRole);
        } else {
            strRoles.forEach(role -> {
                switch (role) {
                    case "admin":
                        Role adminRole = roleRepository.findByName(ERole.ROLE_ADMIN)
                                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                        roles.add(adminRole);

                        break;
                    case "mod":
                        Role modRole = roleRepository.findByName(ERole.ROLE_MODERATOR)
                                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                        roles.add(modRole);

                        break;
                    default:
                        Role userRole = roleRepository.findByName(ERole.ROLE_USER)
                                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                        roles.add(userRole);
                }
            });
        }

        user.setRoles(roles);
        userRepository.save(user);

        return ResponseEntity.ok(new MessageResponse("User registered successfully!"));
    }

    @PostMapping("/refreshtoken")
    public ResponseEntity<?> refreshtoken(@Valid @RequestBody TokenRefreshRequest request) {
        String requestRefreshToken = request.getRefreshToken();

        return refreshTokenService.findByToken(requestRefreshToken)
                .map(refreshTokenService::verifyExpiration)
                .map(RefreshToken::getUser)
                .map(user -> {
                    String token = jwtUtils.generateTokenFromUsername(user.getUsername());
                    return ResponseEntity.ok(new TokenRefreshResponse(token, requestRefreshToken));
                })
                .orElseThrow(() -> new TokenRefreshException(requestRefreshToken, "Refresh token is not in database!"));
    }

    @PreAuthorize("hasRole('MODERATOR') or hasRole('ADMIN')")
    @PostMapping("/logout")
    public ResponseEntity<?> logoutUser(@Valid @RequestBody LogOutRequest logOutRequest, HttpServletRequest request) {
        String token = request.getHeader("Authorization").substring(7);
        refreshTokenService.deleteByUserId(logOutRequest.getUserId());
        iAuthenticationTokenDao.save(new AuthenticationToken(token));
        return ResponseEntity.ok(new MessageResponse("Log out successful!"));
    }

    @GetMapping("validateAccount")
    public ResponseEntity<?> validateAccount(@RequestParam("validationToken")String validationToken){
        log.debug(validationToken);
        log.debug(jwtUtils.getUserNameFromJwtToken(validationToken));

        try {
            iOwnerService.validate(validationToken);
            return new ResponseEntity( HttpStatus.ACCEPTED);
        }
        catch (Exception e){
            return new ResponseEntity(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }




}
