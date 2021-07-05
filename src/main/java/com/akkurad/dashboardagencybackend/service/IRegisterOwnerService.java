package com.akkurad.dashboardagencybackend.service;

import com.akkurad.dashboardagencybackend.model.Owner;

public interface IRegisterOwnerService {

    Owner save(Owner ownerToRegister) throws Exception;
    void remove(Owner ownerToRemove) throws Exception;
}
