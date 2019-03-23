package com.rbac.dao;

import com.rbac.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.List;

@Component
public class UserDaoImpl implements UserDao {

    @Autowired
    NamedParameterJdbcTemplate namedParamJdbcTemplate;

    @Override
    public User findByUsername(String username) {
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue( "username", username );
        List<User> users = namedParamJdbcTemplate.query("SELECT * FROM user WHERE username = :username", params, new User() );
        if(CollectionUtils.isEmpty(users) || users.size() > 1 )
            return null;
        else
            return users.get(0);
    }
}
