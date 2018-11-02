package dk.aau.cs.ds302e18.app.auth;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CmsUserDetailsService implements UserDetailsService{

    private final UserRepository userRepository;
    private final AuthGroupRepository authGroupRepository;
    private final AccountRespository accountRespository;

    public CmsUserDetailsService(UserRepository userRepository, AuthGroupRepository authGroupRepository, AccountRespository accountRespository){
        super();
        this.userRepository = userRepository;
        this.authGroupRepository = authGroupRepository;
        this.accountRespository = accountRespository;
    }

    /* Springs way of storing user details / information. */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        /* An user object is created with information from the database that matches that users username.  */
        User user = this.userRepository.findByUsername(username);
        /* If an user with an username not in the database is signed in the system an exception is thrown. */
        if(null==user){
            throw new UsernameNotFoundException("Cannot find username: " + username);
        }
        /* All the authorities an user has is read from the database and added to an list.  */
        List<AuthGroup> authGroups = this.authGroupRepository.findByUsername(username);
        /* Account information is read from the database and stored in an account object. */
        Account account = this.accountRespository.findByUsername(username);
        /* User credentials, authorities and account information is stored and returned as an UserDetails object.  */
        return new CmsUserPrincipal(user, authGroups, account);
    }
}
