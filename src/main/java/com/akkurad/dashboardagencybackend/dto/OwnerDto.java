package com.akkurad.dashboardagencybackend.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OwnerDto {

    private String username;
    private String email;
    private boolean mailVerified = false;
}

