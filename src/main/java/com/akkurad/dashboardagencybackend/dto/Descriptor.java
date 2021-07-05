package com.akkurad.dashboardagencybackend.dto;

import com.google.gson.annotations.SerializedName;
import lombok.*;

import java.io.Serializable;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class Descriptor implements Serializable {

    @SerializedName("uuid")
    private UUID uuid;
}
