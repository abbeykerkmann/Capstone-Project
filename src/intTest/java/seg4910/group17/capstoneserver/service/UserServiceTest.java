package seg4910.group17.capstoneserver.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import seg4910.group17.capstoneserver.BaseAppTest;
import seg4910.group17.capstoneserver.dao.User;
import seg4910.group17.capstoneserver.repository.UserJpaRepository;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class UserServiceTest extends BaseAppTest {

    @Autowired
    private UserService userService;

    @Autowired
    private UserJpaRepository userRepo;

    @Autowired
    private PasswordEncoder encoder;

    private final String password = "userpassword";

    @Test
    void verifyPassword_returnsTrueForCorrectPassword() {
        User user = persistAUser();
        userService.verifyPassword(user, password);
        assertThat(userService.verifyPassword(user, password)).isTrue();
    }

    @Test
    void verifyPassword_returnsFalseForWrongPassword() {
        User user = persistAUser();
        assertThat(userService.verifyPassword(user, "wrongPassword")).isFalse();
    }

    @Test
    void changePasswordPersistsEncodedPassword() {
        User user = persistAUser();
        String newPassword = "newPassword";
        userService.changePassword(user, newPassword);

        User retrievedUser = userRepo.getOne(user.getId());
        assertThat(encoder.matches(newPassword, retrievedUser.getPassword())).isTrue();
    }

    private User persistAUser() {
        return userService.createUser(new User("user", "user@user.com",
                "user", "McUser", password));
    }

    @Test
    void updateUser_persistCorrectUserDetails() {
        User user = persistAUser();
        User updatedUser = new User("newUsername", "newemail@user.com", "newFirstname", "newLastname", null);
        userService.updateUserDetails(user.getId(), updatedUser);

        User retrievedUser = userRepo.getOne(user.getId());
        assertThat(retrievedUser.getUsername()).isEqualTo("newUsername");
        assertThat(retrievedUser.getEmail()).isEqualTo("newemail@user.com");
        assertThat(retrievedUser.getFirstName()).isEqualTo("newFirstname");
        assertThat(retrievedUser.getLastName()).isEqualTo("newLastname");
        assertThat(retrievedUser.getPassword()).isEqualTo(user.getPassword());
    }

}
