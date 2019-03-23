package com.rbac.domain;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.lang.Nullable;

import java.sql.ResultSet;
import java.sql.SQLException;

public class AuthGroup implements RowMapper<AuthGroup> {

    private long id;
    private String username;
    private String authGroup;

    public long getId() {
        return id;
    }

    public AuthGroup setId(long id) {
        this.id = id;
        return this;
    }

    public String getUsername() {
        return username;
    }

    public AuthGroup setUsername(String username) {
        this.username = username;
        return this;
    }

    public String getAuthGroup() {
        return authGroup;
    }

    public AuthGroup setAuthGroup(String authGroup) {
        this.authGroup = authGroup;
        return this;
    }

    @Nullable
    @Override
    public AuthGroup mapRow(ResultSet resultSet, int i) throws SQLException {
       AuthGroup authGroup = new AuthGroup();
       authGroup.setId( resultSet.getLong("AUTH_USER_GROUP_ID"));
       authGroup.setUsername( resultSet.getString("username") );
       authGroup.setAuthGroup( resultSet.getString("auth_group"));
       return authGroup;
    }
}
