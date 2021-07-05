package com.akkurad.dashboardagencybackend.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.redis.core.RedisHash;

@RedisHash("authenticationToken")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class AuthenticationToken {
    private String id;
}
