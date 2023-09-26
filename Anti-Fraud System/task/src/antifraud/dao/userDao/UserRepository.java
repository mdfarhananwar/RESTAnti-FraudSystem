package antifraud.dao.userDao;

import antifraud.models.userModel.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repository interface for performing CRUD operations on User entities.
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    /**
     * Find a user by their username and eagerly fetch their associated role.
     *
     * @param username The username to search for.
     * @return An Optional containing the user with the specified username (including the associated role), or empty if not found.
     */
    @Query("SELECT u FROM User u JOIN FETCH u.role WHERE u.username = :username")
    Optional<User> findByUsername(String username);

    /**
     * Check if a user with the specified username (case-insensitive) exists.
     *
     * @param username The username to check.
     * @return true if a user with the specified username exists; otherwise, false.
     */
    boolean existsByUsernameIgnoreCase(String username);

    /**
     * Delete a user by their username (case-insensitive).
     *
     * @param username The username of the user to delete.
     */
    void deleteByUsernameIgnoreCase(String username);
}