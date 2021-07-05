package com.akkurad.dashboardagencybackend.dao;

import com.akkurad.dashboardagencybackend.model.Lockz;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ILockDao extends JpaRepository<Lockz, Long> {
}
