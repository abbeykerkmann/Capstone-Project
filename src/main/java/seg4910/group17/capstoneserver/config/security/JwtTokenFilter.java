package seg4910.group17.capstoneserver.config.security;

import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import seg4910.group17.capstoneserver.repository.UserJpaRepository;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Optional;

@Component
public class JwtTokenFilter extends OncePerRequestFilter {

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    private UserJpaRepository userRepo;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        // grab the header and make sure the token is in the right field
        final String header = request.getHeader(HttpHeaders.AUTHORIZATION);
        if(header == null || !header.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        // now we get the token and try to validate
        // split at after the "Bearer "
        final String[] splitHeader = header.split(" ");
        if(splitHeader.length < 2) {
            filterChain.doFilter(request, response);
            return;
        }

        final String token = splitHeader[1].trim();
        final Optional<DecodedJWT> decodedToken = jwtTokenUtil.decode(token);
        if(!decodedToken.isPresent()) {
            // token is not valid
            // user is not authenticated
            // pass on down the chain
            filterChain.doFilter(request, response);
            return;
        }

        // token is valid
        UserDetails userDetails = userRepo.findById(jwtTokenUtil.getUserID(decodedToken.get())).orElse(null);

        UsernamePasswordAuthenticationToken authentication = createAuthToken(userDetails);

        authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

        getSecurityContext().setAuthentication(authentication);
        filterChain.doFilter(request, response);
    }

    protected UsernamePasswordAuthenticationToken createAuthToken(UserDetails userDetails) {
        return new UsernamePasswordAuthenticationToken(
                userDetails, null, new ArrayList<>()
        );
    }

    protected SecurityContext getSecurityContext() {
        return SecurityContextHolder.getContext();
    }

}
