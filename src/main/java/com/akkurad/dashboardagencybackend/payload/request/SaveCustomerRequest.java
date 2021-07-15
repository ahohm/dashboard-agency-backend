package com.akkurad.dashboardagencybackend.payload.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class SaveCustomerRequest {
    @NotBlank
    @Size(min = 3, max = 30)
    private String username;
    @NotBlank
    @Email
    private String email;
}
