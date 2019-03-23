package com.rbac.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.hierarchicalroles.RoleHierarchyAuthoritiesMapper;
import org.springframework.security.access.hierarchicalroles.RoleHierarchyImpl;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.authority.mapping.GrantedAuthoritiesMapper;
import org.springframework.security.core.authority.mapping.SimpleAuthorityMapper;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;

import java.util.Arrays;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class ApplicationSecurityConfiguration extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .authorizeRequests()
                .anyRequest().authenticated() //lock down all requests !
                .and()
                .httpBasic(); //use a modal pop-up to get username + password
    }

    /*
    * Returns an implementation of the UserDetailsService interface. See CustomUserDetailsService
    * class for more information.
    * */
    @Bean
    public UserDetailsService getCustomUserDetailService()
    {
        return new CustomUserDetailsService();
    }

    /*
    *   UNUSED
    *
    *   Below we creating a Bean for mapping authorities. In the simplest case,
    *   we only want the roles defined in the database to be assigned to user.
    *   For example, if jsmith is assigned ADMIN, then only role assigned assigned to
    *   jsmith would ADMIN. This can be done via a SimpleAuthorityMapper object.
    *
    * */
    @Bean
    public GrantedAuthoritiesMapper authoritiesMapper()
    {
        SimpleAuthorityMapper authorityMapper = new SimpleAuthorityMapper();
        authorityMapper.setConvertToUpperCase(true);
        authorityMapper.setDefaultAuthority("USER");
        return authorityMapper;
    }

    /*
    *  Used to assign hierarchical authorities to users. This mapping is done via transitive
    *  authority mapping. Below we set the hierarchy as follows:
    *
    *  ADMIN > PREMIUM ADMIN > X-TRA  PREMIUM > USER USER > GUEST
    *
    *  The spaces between the roles can be converted for better readability:
    *
    *  ADMIN > PREMIUM
    *  ADMIN > X-TRA
    *  PREMIUM > USER
    *  USER > GUEST
    *
    *  If a user is assigned an ADMIN role, then it trabsitively follows that he has the roles
    *  of ADMIN, PREMIUM, X-TRA, USER, GUEST
    *
    *  If a user is assigned PREMIUM, then he only has access to PREMIUM, USER, GUEST
    * */
    @Bean
    public RoleHierarchyAuthoritiesMapper roleHierarchyAuthoritiesMapper()
    {
        RoleHierarchyImpl roleHierarchy = new RoleHierarchyImpl();
        roleHierarchy.setHierarchy("ADMIN > PREMIUM ADMIN > X-TRA  PREMIUM > USER USER > GUEST");
        return new RoleHierarchyAuthoritiesMapper( roleHierarchy );
    }

    /*
    *  This is the bean the puts everything together for authenticating a user. We are creating DAO
    *  based authentication where we are passing in the beans for:
    *
    *  UserDetailsService - knows how to fetch an UserDetails object which contains all the information about
    *  a user including username, password, authorities, etc
    *
    *  PasswordEncoder - knows how to encode a password. Typically, we don't store passwords as plain-text in DB. Rather
    *  we use a one-way mathematical hashing function on the password, and store the hashed value:
    *
    *       hash( original-password ) ----> ha$Hed_p@$$w0rd
    *
    *       Above is a one-way operation. We cannot re-generate the original password from the hashed-password. However, hashing
    *       functions are consistent - hashing the same password will always generate the same hashed-value
    *
    *       Thus, the only way to do password validation is to hash the presented password and compare with hashed value stored in DB.
    *
    *       The password encoder will handle this operation for us.
    *
    *   AuthoritiesMapper - this object will take the roles provided as part of the UserDetail object and generate any additional
    *   roles(such as the transitive ones).
    *
    * */
    @Bean
    public DaoAuthenticationProvider getDaoAuthenticationProvider()
    {
        DaoAuthenticationProvider daoAuthProvider = new DaoAuthenticationProvider();
        daoAuthProvider.setUserDetailsService( getCustomUserDetailService() );
        daoAuthProvider.setPasswordEncoder( NoOpPasswordEncoder.getInstance() );
        daoAuthProvider.setAuthoritiesMapper( roleHierarchyAuthoritiesMapper() );
        return daoAuthProvider;
    }

    /*
    * AuthenticationManager is responsible for authenticating and creating a session for a user.
    * It is an interface with a single method, 'authenticate' which we would pass an Authentication
    * object. Spring provides a default implementation with the <b>ProviderManager</b> class. Purpose
    * of the AuthenticationManager is to manage different objects that can actually authenticate users
    *
    * In order for an AuthenticationManager to work, it needs one or more AuthenticationProvider's
    * This is another interface which contains method 'authenticate' which again takes an Authentication
    * object. This class is actually responsible for authenticating a user against a database, LDAP, flat-file,
    * whatever. Spring provides couple of out of the box implementations including DaoAuthentictaionProvider,
    * LdapAuthenticationProvider, etc.
    * */
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        //we can pass in a list of AuthenticationProvider implementors
        //the ProviderManager will try each one until one of them successfully authenticates
        //the current user
        return new ProviderManager( Arrays.asList( getDaoAuthenticationProvider() ));
    }
}
