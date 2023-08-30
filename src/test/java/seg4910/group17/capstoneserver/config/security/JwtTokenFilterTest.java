package seg4910.group17.capstoneserver.config.security;

import com.auth0.jwt.interfaces.DecodedJWT;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.userdetails.UserDetails;
import seg4910.group17.capstoneserver.repository.UserJpaRepository;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.Optional;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class JwtTokenFilterTest {

    @Mock
    private JwtTokenUtil jwtTokenUtil;

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Mock
    private FilterChain chain;

    @Mock
    private UserJpaRepository userRepo;

    @Mock
    private UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken;

    @Mock
    private SecurityContext securityContext;

    @InjectMocks
    private final JwtTokenFilter filter = new JwtTokenFilter() {

        @Override
        public UsernamePasswordAuthenticationToken createAuthToken(UserDetails userDetails) {
            return usernamePasswordAuthenticationToken;
        }

        @Override
        public SecurityContext getSecurityContext() {
            return securityContext;
        }
    };

    @Test
    public void testNoAuthIfNoHeader() throws ServletException, IOException {
        doReturn(null).when(request).getHeader(HttpHeaders.AUTHORIZATION);
        filter.doFilterInternal(request, response, chain);

        verify(chain, times(1)).doFilter(request, response);
        verifyNoInteractions(jwtTokenUtil);
        verifyNoInteractions(securityContext);
    }

    @Test
    public void testNoAuthIfNotBearerToken() throws ServletException, IOException {
        doReturn("BLA BLA").when(request).getHeader(HttpHeaders.AUTHORIZATION);
        filter.doFilterInternal(request, response, chain);

        verify(chain, times(1)).doFilter(request, response);
        verifyNoInteractions(jwtTokenUtil);
        verifyNoInteractions(securityContext);
    }

    @Test
    public void testNoAuthIfNoTokenAfterBearer() throws ServletException, IOException {
        doReturn("Bearer ").when(request).getHeader(HttpHeaders.AUTHORIZATION);
        filter.doFilterInternal(request, response, chain);

        verify(chain, times(1)).doFilter(request, response);
        verifyNoInteractions(jwtTokenUtil);
        verifyNoInteractions(securityContext);
    }

    @Test
    public void testInvalidTokenNoAuth() throws ServletException, IOException {
        String fakeToken = "afjqpweo.fjqepofq.fqjoefjqp";
        doReturn("Bearer " + fakeToken).when(request).getHeader(HttpHeaders.AUTHORIZATION);

        doReturn(Optional.empty()).when(jwtTokenUtil).decode(fakeToken);
        filter.doFilterInternal(request, response, chain);

        verify(jwtTokenUtil).decode(fakeToken);
        verify(chain, times(1)).doFilter(request, response);
        verifyNoMoreInteractions(jwtTokenUtil);
        verifyNoInteractions(securityContext);
    }

    @Test
    public void testValidToken() throws ServletException, IOException {
        String fakeToken = "afjqpweo.fjqepofq.fqjoefjqp";
        doReturn("Bearer " + fakeToken).when(request).getHeader(HttpHeaders.AUTHORIZATION);

        DecodedJWT decodedJWT = mock(DecodedJWT.class);
        doReturn(Optional.of(decodedJWT)).when(jwtTokenUtil).decode(fakeToken);

        // user id 3
        doReturn(3).when(jwtTokenUtil).getUserID(decodedJWT);

        UserDetails userDetails = mock(UserDetails.class);
        doReturn(Optional.of(userDetails)).when(userRepo).findById(3);

        filter.doFilterInternal(request, response, chain);

        verify(userRepo).findById(3);
        verify(usernamePasswordAuthenticationToken).setDetails(any());
        verify(securityContext).setAuthentication(usernamePasswordAuthenticationToken);
        verify(chain).doFilter(request, response);
    }

}