package seg4910.group17.capstoneserver.config.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTCreator;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import seg4910.group17.capstoneserver.dao.User;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.Clock;
import java.time.Period;
import java.util.Date;
import java.util.Optional;

@Component
public class JwtTokenUtil {

    private final Logger logger = LoggerFactory.getLogger(JwtTokenUtil.class);

    private final String JWT_SECRET = "1UZlnRvEZHFvreXzZ8K8B6iAneGeFMG3";
    private final String JWT_ISSUER = "com.sprout";

    private final Algorithm hashingAlgorithm = Algorithm.HMAC256(JWT_SECRET);
    private final JWTVerifier jwtVerifier = JWT.require(hashingAlgorithm).withIssuer(JWT_ISSUER).build();

    private final Period tokenLifetime = Period.ofWeeks(1);

    @Autowired
    private Clock clock;

    public String generateToken(User user) {
        return JWT.create()
                .withIssuer(JWT_ISSUER)
                .withIssuedAt(new Date(clock.instant().toEpochMilli()))
                .withExpiresAt(new Date(clock.instant().plus(tokenLifetime).toEpochMilli()))
                .withSubject(user.getId().toString())
                .sign(hashingAlgorithm);
    }

    public Optional<DecodedJWT> decode(String token) {
        try {
            return Optional.of(jwtVerifier.verify(token));
        } catch (JWTVerificationException exception) {
            // invalid signature or expired, or invalid json
            logger.error("JWT Verification failed: " + exception.getMessage());
            return Optional.empty();
        }
    }

    public Integer getUserID(DecodedJWT token) {
        try {
            return Integer.parseInt(token.getSubject());
        } catch(NumberFormatException exception) {
            // if token has been verified, then the only reason this could happen is if
            // we issued a token with a subject that isn't an integer
            logger.error("JWT Subject ID is not an integer.");
            throw exception;
        }
    }

}
