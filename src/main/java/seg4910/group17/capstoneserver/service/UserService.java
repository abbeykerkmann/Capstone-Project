package seg4910.group17.capstoneserver.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import seg4910.group17.capstoneserver.api.model.resource.UserResponse;
import seg4910.group17.capstoneserver.dao.User;
import seg4910.group17.capstoneserver.repository.UserJpaRepository;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserService implements UserDetailsService {

    @Autowired
    UserJpaRepository userRepo;

    @Autowired
    PasswordEncoder encoder;

    public User createUser(User user) {
        user.setPassword(encoder.encode(user.getPassword()));
        return userRepo.save(user);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepo.findByUsername(username).orElseThrow(
                () -> new UsernameNotFoundException("User with username " + username + " not found.")
        );
    }

    public List<UserResponse> getAllUsers() {
        List<User> users = userRepo.findAll();
        List<UserResponse> userResponse = new ArrayList<>();
        for(User u : users) {
            userResponse.add(convertUserToUserResponse(u));
        }
        return userResponse;
    }

    public UserResponse getUser(Integer id) {
        User user = userRepo.findById(id).get();
        return convertUserToUserResponse(user);
    }

    public UserResponse convertUserToUserResponse(User user) {
        return new UserResponse(user.getId(), user.getUsername(), user.getEmail(), user.getAddress(), user.getFirstName(), user.getLastName(), user.getPhone());
    }

    public boolean verifyPassword(User user, String password) {
        return encoder.matches(password, user.getPassword());
    }

    public void changePassword(User user, String newPassword) {
        user.setPassword(encoder.encode(newPassword));
        userRepo.save(user);
    }

    public User updateUserDetails(Integer userId, User aInUser) {
        User user = userRepo.findById(userId).get();
        if(aInUser.getUsername() != null && !aInUser.getUsername().isEmpty()) {
            user.setUsername(aInUser.getUsername());
        }
        if(aInUser.getEmail() != null && !aInUser.getEmail().isEmpty()) {
            user.setEmail(aInUser.getEmail());
        }
        if(aInUser.getAddress() != null && !aInUser.getEmail().isEmpty()) {
            user.setAddress(aInUser.getAddress());
        }
        if(aInUser.getFirstName() != null && !aInUser.getFirstName().isEmpty()) {
            user.setFirstName(aInUser.getFirstName());
        }
        if(aInUser.getLastName() != null && !aInUser.getLastName().isEmpty()) {
            user.setLastName(aInUser.getLastName());
        }
        if(aInUser.getPhone() != null && !aInUser.getPhone().isEmpty()) {
            user.setPhone(aInUser.getPhone());
        }
        return userRepo.save(user);
    }
}
