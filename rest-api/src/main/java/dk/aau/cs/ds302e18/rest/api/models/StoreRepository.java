package dk.aau.cs.ds302e18.rest.api.models;

import dk.aau.cs.ds302e18.shared.model.Store;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
/* Spring uses JpaRepository to connect to the database. Here the lesson repository in the database is connected to.
   The repository can now be be called through the objects from the LessonRepository class. */
@Repository
public interface StoreRepository extends JpaRepository<Store, Long> {
}
