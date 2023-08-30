package seg4910.group17.capstoneserver.config.security;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.time.Clock;
import java.time.Instant;
import java.time.Period;
import java.time.ZoneId;
import java.util.Base64;
import java.util.Optional;

import org.mockito.junit.jupiter.MockitoExtension;
import seg4910.group17.capstoneserver.dao.User;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class JwtTokenUtilTest {

    private final String JWT_ISSUER = "com.sprout";

    @InjectMocks
    private final JwtTokenUtil util = new JwtTokenUtil();

    @Mock
    private User user;

    private final ObjectMapper mapper = new ObjectMapper();

    @Mock
    Clock clock;
    Clock fixedClock;

    @BeforeEach
    public void setUp() {
        doReturn(3).when(user).getId();

        fixedClock = Clock.fixed(Instant.now(), ZoneId.systemDefault());
        lenient().doReturn(fixedClock.getZone()).when(clock).getZone();
        lenient().doReturn(fixedClock.instant()).when(clock).instant();
    }

    @Test
    public void testUsesJWT() throws JsonProcessingException {
        String token = util.generateToken(user);
        JsonNode obj = mapper.readTree(getDecodedHeader(token));

        assertThat(obj.has("typ")).isTrue();
        assertThat(obj.get("typ").textValue()).isEqualTo("JWT");
    }

    @Test
    public void testUsesHS256() throws JsonProcessingException {
        String token = util.generateToken(user);
        JsonNode obj = mapper.readTree(getDecodedHeader(token));

        assertThat(obj.has("alg")).isTrue();
        assertThat(obj.get("alg").textValue()).isEqualTo("HS256");
    }

    @Test
    public void testTokenIncludesIssuer() throws JsonProcessingException {
        String token = util.generateToken(user);
        JsonNode obj = mapper.readTree(getDecodedPayload(token));

        assertThat(obj.has("iss")).isTrue();
        assertThat(obj.get("iss").textValue()).isEqualTo(JWT_ISSUER);
    }

    @Test
    public void testUsesCorrectSubject() throws JsonProcessingException {
        String token = util.generateToken(user);
        JsonNode obj = mapper.readTree(getDecodedPayload(token));

        assertThat(obj.has("sub")).isTrue();
        assertThat(obj.get("sub").asInt()).isEqualTo(3);
    }

    @Test
    public void testVerificationFailsWithCorruptSubject() throws JsonProcessingException {
        String token = util.generateToken(user);
        JsonNode obj = mapper.readTree(getDecodedPayload(token));
        ((ObjectNode) obj).put("sub", "123");

        String[] splitToken = token.split("\\.");
        String corruptToken = splitToken[0] + "." + encode(mapper.writeValueAsString(obj)) + "." + splitToken[2];
        Optional<DecodedJWT> decodedJWT = util.decode(corruptToken);
        assertThat(decodedJWT).isEmpty();
    }

    @Test
    public void testIssuedAt() throws JsonProcessingException {
        String token = util.generateToken(user);
        JsonNode obj = mapper.readTree(getDecodedPayload(token));
        assertThat(obj.has("iat")).isTrue();
        // jwt uses epoch seconds, even when given epoch millis
        assertThat(obj.get("iat").asLong()).isEqualTo(fixedClock.instant().getEpochSecond());
    }

    @Test
    public void testExpirationInOneWeek() throws JsonProcessingException {
        String token = util.generateToken(user);
        JsonNode obj = mapper.readTree(getDecodedPayload(token));
        assertThat(obj.has("exp")).isTrue();
        assertThat(obj.get("exp").asLong()).isEqualTo(fixedClock.instant().plus(Period.ofWeeks(1)).getEpochSecond());
    }

    @Test
    public void testInvalidIfOneWeekOld() throws JsonProcessingException {
        // give a date one week in the past
        doReturn(fixedClock.instant().minus(Period.ofWeeks(1)).minusSeconds(1)).when(clock).instant();
        String token = util.generateToken(user);

        Optional<DecodedJWT> decodedJWT = util.decode(token);
        assertThat(decodedJWT).isEmpty();
    }

    private String getDecodedHeader(String token) {
        return decode(token.split("\\.")[0]);
    }

    private String getDecodedPayload(String token) {
        return decode(token.split("\\.")[1]);
    }

    private String encode(String value) {
        return Base64.getUrlEncoder().encodeToString(value.getBytes());
    }

    private String decode(String base64EncodedString) {
        return new String(Base64.getUrlDecoder().decode(base64EncodedString));
    }

}