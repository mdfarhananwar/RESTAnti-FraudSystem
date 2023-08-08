package antifraud.dao.userDao;

import antifraud.models.userModel.Role;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, Long> {

    @Override
    void delete(@NotNull Role role);
}
