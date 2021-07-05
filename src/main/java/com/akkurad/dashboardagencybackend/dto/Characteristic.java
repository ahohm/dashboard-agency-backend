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
public class Characteristic implements Serializable {

    @SerializedName("uuid")
    private UUID uuid;
    @SerializedName("properties")
    private List<String> properties;
    @SerializedName("descriptors")
    private List<Descriptor> descriptors;

}
