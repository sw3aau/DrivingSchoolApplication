package dk.aau.cs.ds302e18.service.models;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/* Spring uses JpaRepository to connect to the database. Here the auth_user_group repository in the database is connected to.
   The repository can now be called through the objects from the the AuthGroupRepository class. */
public interface AuthGroupRepository extends JpaRepository<AuthGroup, Long> {
    List<AuthGroup> findByUsername(String username);
}
