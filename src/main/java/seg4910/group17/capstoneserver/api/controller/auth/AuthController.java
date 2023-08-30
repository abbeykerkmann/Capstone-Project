package seg4910.group17.capstoneserver.api.controller.auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import seg4910.group17.capstoneserver.api.controller.common.BaseController;
import seg4910.group17.capstoneserver.api.model.auth.LoginCredentials;
import seg4910.group17.capstoneserver.api.model.auth.PasswordChangeDetails;
import seg4910.group17.capstoneserver.api.model.resource.UserResponse;
import seg4910.group17.capstoneserver.config.security.JwtTokenUtil;
import seg4910.group17.capstoneserver.dao.User;
import seg4910.group17.capstoneserver.service.UserService;

import javax.validation.Valid;

@RestController
@RequestMapping("auth")
public class AuthController extends BaseController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    private UserService userService;

    @PostMapping("authenticate")
    public ResponseEntity<UserResponse> authenticateUser(@Valid @RequestBody LoginCredentials creds) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(creds.getUsername(), creds.getPassword())
            );
            User user = (User) authentication.getPrincipal();
            return produceAuthResponse(user);
        } catch(BadCredentialsException ex) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    @PostMapping("signup")
    public ResponseEntity<UserResponse> createAndAuthUser(@Valid @RequestBody User user) {
        userService.createUser(user);
        return produceAuthResponse(user);
    }

    @PostMapping("changepass")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<UserResponse> changePassword(@Valid @RequestBody PasswordChangeDetails details) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if(!userService.verifyPassword(user, details.getOldPassword()))
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        userService.changePassword(user, details.getNewPassword());
        return ResponseEntity.ok().body(userService.convertUserToUserResponse(user));
    }

    private ResponseEntity<UserResponse> produceAuthResponse(User user) {
        return ResponseEntity.ok()
                .header(HttpHeaders.AUTHORIZATION, jwtTokenUtil.generateToken(user))
                .body(userService.convertUserToUserResponse(user));
    }

}
