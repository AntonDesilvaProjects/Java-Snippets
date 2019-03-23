package com.rbac.dao;

import com.rbac.domain.AuthGroup;

import java.util.List;

public interface AuthGroupDao {
    List<AuthGroup> findByUsername(String username);
}
