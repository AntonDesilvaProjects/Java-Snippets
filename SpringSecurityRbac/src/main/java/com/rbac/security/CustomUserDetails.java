package com.rbac.security;

import com.rbac.domain.AuthGroup;
import com.rbac.domain.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.*;

/*
*  Spring Security deals with 'Principal's instead of 'Users' for authentication/authorization.
*  Therefore, we need a class to map our User object to Spring's Principal object called UserDetails.
*  UserDetails is actually an interface that contains methods for retrieving information about the user including username,
*  password, and authorities. So our implementation should provide this information.
*
* */
public class CustomUserDetails implements UserDetails {

    private User user;
    private List<AuthGroup> authGroups;

    public CustomUserDetails( User user, List<AuthGroup> authGroups )
    {
        super();
        this.user = user;
        this.authGroups = authGroups;
    }

    /*
    * This function is used grant authorities to users. Authorities are things the user can
    * do once they are authenticated. In this particular case, we have implemented authorities
    * in a simple manner. We have the following table structure:
    *
    * ----------------------
    * | username | auth_group |
    * ----------------------
    * | jsmith   |   ADMIN    |
    * -------------------------
    *
    * Below we simply create a SimpleGrantedAuthority with the user's auth_group value
    * */
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Set<SimpleGrantedAuthority> authorities = new HashSet<>();
        if( authGroups != null ) {
           authGroups.forEach(authGroup -> {
                authorities.add( new SimpleGrantedAuthority(authGroup.getAuthGroup()) );
           });
       }
       return authorities;
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getUsername();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    public User getUser()
    {
        return this.user;
    }
}
