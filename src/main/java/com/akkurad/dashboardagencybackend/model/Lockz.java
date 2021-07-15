package com.akkurad.dashboardagencybackend.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Lockz implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    //  @Pattern(regexp = "", message = "password doesn't match.")
    private String serialNumber;
    //  @Pattern(regexp = "", message = "password doesn't match.")
    private String macAddress;
    //  @Pattern(regexp = "", message = "password doesn't match.")
    private String rssi;
    @NotNull
    private ELockType type;



}

