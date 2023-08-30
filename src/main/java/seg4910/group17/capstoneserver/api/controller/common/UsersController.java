package seg4910.group17.capstoneserver.api.controller.common;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import seg4910.group17.capstoneserver.api.model.resource.UserResponse;
import seg4910.group17.capstoneserver.dao.User;
import seg4910.group17.capstoneserver.repository.UserJpaRepository;
import seg4910.group17.capstoneserver.service.UserService;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/users")
public class UsersController extends BaseController {

    @Autowired
    private UserJpaRepository userJpaRepository;

    @Autowired
    private UserService userService;

    @GetMapping("")
    public List<UserResponse> getAll() {
        return userService.getAllUsers();
    }

    @GetMapping("/{id}")
    public UserResponse get(@PathVariable Integer id) {
        return userService.getUser(id);
    }

    @PostMapping("")
    public User save(@Valid @RequestBody final User user) {
        return userService.createUser(user);
    }

    @PutMapping("")
    public User updateById(@Valid @RequestBody final User user) {
        return userService.updateUserDetails(user.getId(), user);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Integer id) {
        userJpaRepository.deleteById(id);
    }
}
