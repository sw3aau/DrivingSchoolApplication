package dk.aau.cs.ds302e18.website.auth;

import org.springframework.data.jpa.repository.JpaRepository;

/* Spring uses JpaRepository to connect to the database. Here the account repository in the database is connected to.
   The repository can now be called through the objects from the AccountRepository class. */
public interface AccountRespository extends JpaRepository<Account, Long>
{
    Account findByUsername(String username);

}