package seg4910.group17.capstoneserver;

import org.hibernate.exception.ConstraintViolationException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import seg4910.group17.capstoneserver.dao.User;
import seg4910.group17.capstoneserver.repository.UserJpaRepository;

import javax.persistence.PersistenceException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class UserEntityTest extends BaseEntityTest {

    @Autowired
    private UserJpaRepository userRepo;

    @Test
    public void testPersistsOneUserEntity() {
        User user = entityManager.persistAndFlush(new User("admin", "admin@mcadmin.com",
                "admin", "McAdmin", "password"));
        // make sure it got an id
        Integer id = user.getId();
        assertThat(id).isNotNull();
//        assertThat(userRepo.count()).isEqualTo(1);

        // retrieve
        User savedUser = userRepo.getOne(id);
        assertThat(user.getUsername()).isEqualTo("admin");
        assertThat(user.getEmail()).isEqualTo("admin@mcadmin.com");
        assertThat(user.getFirstName()).isEqualTo("admin");
        assertThat(user.getLastName()).isEqualTo("McAdmin");
        assertThat(user.getPassword()).isEqualTo("password");
        assertThat(user.getAddress()).isNull();
        assertThat(user.getPhone()).isNull();
        assertThat(user.getListings()).isNotNull().isEmpty();
    }

    @Test
    public void testUniqueUsernameOrException() {
        entityManager.persistAndFlush(new User("admin", "admin@mcadmin.com",
                "admin", "McAdmin", "password"));
        assertThatThrownBy(() -> {
            entityManager.persistAndFlush(new User("admin", "admin@mcadmin.com",
                    "admin", "McAdmin", "password"));
            })
                .isInstanceOf(PersistenceException.class)
                .hasCauseInstanceOf(ConstraintViolationException.class);
    }

}
