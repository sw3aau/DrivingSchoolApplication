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

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = this.userRepository.findByUsername(username);
        if(null==user){
            throw new UsernameNotFoundException("Cannot find username: " + username);
        }
        List<AuthGroup> authGroups = this.authGroupRepository.findByUsername(username);
        Account account = this.accountRespository.findByUsername(username);
        return new CmsUserPrincipal(user, authGroups, account);
    }
}
