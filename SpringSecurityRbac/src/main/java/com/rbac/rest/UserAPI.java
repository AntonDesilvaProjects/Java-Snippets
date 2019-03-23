package com.rbac.rest;

import com.rbac.domain.User;
import com.rbac.security.CustomUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.websocket.server.PathParam;
import java.util.Collection;

@RestController
public class UserAPI {

    @Autowired
    UserDetailsService userDetailsService;

    @RequestMapping(value="/test", method= RequestMethod.GET)
    public Collection getUserInfo()
    {
        return SecurityContextHolder.getContext().getAuthentication().getAuthorities();
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @RequestMapping(value="/userInfo", method= RequestMethod.GET)
    public User getUserInfo(@PathParam("username") String username)
    {
        return ((CustomUserDetailsService) userDetailsService ).getUser( username );
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @RequestMapping(value="/adminInfo", method= RequestMethod.GET)
    public String getAdminOnlyInfo()
    {
        return "Hello there Admin!";
    }
}
