package antifraud.dao.userDao;

import antifraud.models.userModel.Role;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repository interface for performing CRUD operations on Role entities.
 */
public interface RoleRepository extends JpaRepository<Role, Long> {

    /**
     * Delete a role.
     *
     * @param role The role to be deleted.
     */
    @Override
    void delete(@NotNull Role role);
}
