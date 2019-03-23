package com.rbac.domain;

import org.springframework.jdbc.core.RowMapper;
import java.sql.ResultSet;
import java.sql.SQLException;

public class User implements RowMapper<User> {

    private String username;
    private String password;
    private long userId;

    public String getUsername() {
        return username;
    }

    public User setUsername(String username) {
        this.username = username;
        return this;
    }

    public String getPassword() {
        return password;
    }

    public User setPassword(String password) {
        this.password = password;
        return this;
    }

    public long getUserId() {
        return userId;
    }

    public User setUserId(long userId) {
        this.userId = userId;
        return this;
    }

    @Override
    public String toString() {
        return "User{" +
                "username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", userId=" + userId +
                '}';
    }

    @Override
    public User mapRow(ResultSet resultSet, int i) throws SQLException {
        return new User()
                .setUserId( resultSet.getLong("user_id"))
                .setUsername( resultSet.getString("username"))
                .setPassword( resultSet.getString("password"));
    }
}
