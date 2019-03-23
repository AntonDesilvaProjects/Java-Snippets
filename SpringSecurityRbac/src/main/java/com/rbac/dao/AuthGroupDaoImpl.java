package com.rbac.dao;

import com.rbac.domain.AuthGroup;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class AuthGroupDaoImpl implements AuthGroupDao {

    @Autowired
    NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Override
    public List<AuthGroup> findByUsername(String username) {
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("username", username );
        List<AuthGroup> authGroups = namedParameterJdbcTemplate.query( "SELECT * FROM AUTH_USER_GROUP WHERE username = :username", params, new AuthGroup() );
        return authGroups;
    }
}
