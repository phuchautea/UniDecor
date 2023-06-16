package uni.decor.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import uni.decor.common.CustomLogger;
import uni.decor.common.Enum;
import uni.decor.entity.User;
import uni.decor.repository.IRoleRepository;
import uni.decor.repository.IUserRepository;


@Service
public class UserService {
    @Autowired
    private IUserRepository userRepository;
    @Autowired
    private IRoleRepository roleRepository;
    public User getUserById(Long id) {
        return userRepository.findById(id).orElse(null);
    }
    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }
    public void save(User user)
    {
        userRepository.save(user);
        Long userId = userRepository.getUserIdByEmail(user.getEmail());
        Long roleId = roleRepository.getRoleIdByName("user");
        if(roleId != 0 && userId != 0)
        {
            userRepository.addRoleToUser(userId, roleId);
        }
    }
    public void saveOauthUser(String email, String fullName) {
        if(userRepository.findByEmail(email) != null)
            return;
        User user = new User();
        user.setEmail(email);
        user.setName(fullName);
        user.setProvider(Enum.Provider.GOOGLE);
        save(user);
        CustomLogger.info("Created new user: " + email);
    }
}
