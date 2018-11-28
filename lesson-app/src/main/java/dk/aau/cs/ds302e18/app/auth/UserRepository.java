package dk.aau.cs.ds302e18.app.auth;

import org.springframework.data.jpa.repository.JpaRepository;

/* Spring uses JpaRepository to connect to the database. Here the user repository in the database is connected to.
   The repository can now be called through the objects from the UserRepository class. */
public interface UserRepository extends JpaRepository<User, Long>{
    User findByUsername(String username);
    User findById(long id);
}
