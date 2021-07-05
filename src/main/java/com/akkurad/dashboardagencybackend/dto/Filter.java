package com.akkurad.dashboardagencybackend.dto;

import com.google.gson.annotations.SerializedName;
import lombok.*;

import java.io.Serializable;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class Filter implements Serializable {

    @SerializedName("id")
    private String id;
    @SerializedName("title")
    private String title;
    @SerializedName("type")
    private String type;
    @SerializedName("parser")
    private String parser;
    @SerializedName("manufacturer_data")
    private int[] manufacturer_data = new int[4];
    @SerializedName("services")
    private List<Service> services;
    @SerializedName("icon")
    private String icon;
    @SerializedName("is_enabled")
    private boolean enable;

}
