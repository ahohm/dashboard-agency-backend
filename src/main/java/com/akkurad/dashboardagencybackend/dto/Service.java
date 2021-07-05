package com.akkurad.dashboardagencybackend.dto;


import com.google.gson.annotations.SerializedName;
import lombok.*;

import java.io.Serializable;
import java.util.List;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class Service implements Serializable {

    @SerializedName("uuid")
    private UUID uuid;
    @SerializedName("is_scannable")
    private boolean scannable;
    @SerializedName("is_primary_service")
    private boolean primary_service;
    @SerializedName("characteristics")
    private List<Characteristic> characteristics;

}
