package com.rbac.dao;

import com.rbac.domain.User;

public interface UserDao {
    User findByUsername(String username);
}
