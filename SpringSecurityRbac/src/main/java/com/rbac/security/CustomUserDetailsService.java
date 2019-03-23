package com.rbac.security;

import com.rbac.dao.AuthGroupDao;
import com.rbac.dao.UserDao;
import com.rbac.domain.AuthGroup;
import com.rbac.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.List;

/*
*   UserDetailsService is an interface with just one method:
*
*       UserDetails loadUserByUsername(String var1) throws UsernameNotFoundException;
*
*   The implementor of this interface can use whatever means necessary(DB, LDAP, Flatfile, etc)
*   to generate a UserDetail instance when passed in a username. A UserDetails instance contains
*   all the information necessary to authenticate(check if valid user) & authorize(assign correct permissions)
*   a user.
*
*   Below implementation simply uses a DB to get all the information about a user and generate an implementation
*   of the UserDetails object.
*
* */
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    UserDao userDao;

    @Autowired
    AuthGroupDao authGroupDao;

    @Override
    public UserDetails loadUserByUsername( String username ) throws UsernameNotFoundException {
        User user = getUser( username ); //get user object
        if( user == null )
            throw new UsernameNotFoundException( "Cannot find a user with the username " + username );

        List<AuthGroup> authGroupsForUser = authGroupDao.findByUsername( username ); //get authorities for user
        return new CustomUserDetails( user, authGroupsForUser ); //generate UserDetails object
    }

    public User getUser( String username )
    {
        return userDao.findByUsername( username );
    }
}
