package antifraud.services;

import antifraud.dao.userDao.RoleRepository;
import antifraud.dao.userDao.UserRepository;

import antifraud.models.userModel.Role;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RoleService {
    private final RoleRepository roleRepository;
    @Autowired
    UserRepository userRepository;

    @Autowired
    public RoleService(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }
    @Transactional
    public Role saveRole(Role role) {
        return roleRepository.save(role);
    }
}
