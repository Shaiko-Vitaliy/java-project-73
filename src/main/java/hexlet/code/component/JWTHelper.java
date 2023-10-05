package hexlet.code.component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Clock;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.impl.DefaultClock;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.Map;

import static io.jsonwebtoken.SignatureAlgorithm.HS256;
import static io.jsonwebtoken.impl.TextCodec.BASE64;

@Component
public class JWTHelper {

    private final String secretKey;
    private final String issuer = "task_manager";
    private final Clock clock;

    public JWTHelper(@Value("${jwt.secret:secret}") final String secret) {
        this.secretKey = BASE64.encode(secret);
        this.clock = DefaultClock.INSTANCE;
    }

    public String expiring(final Map<String, Object> attributes) {
        Long expirationSec = 86400L;
        return Jwts.builder()
                .signWith(HS256, secretKey)
                .setClaims(getClaims(attributes, expirationSec))
                .compact();
    }

    public Map<String, Object> verify(final String token) {
        long clockSkewSec = 300L;
        return Jwts.parser()
                .requireIssuer(issuer)
                .setClock(clock)
                .setAllowedClockSkewSeconds(clockSkewSec)
                .setSigningKey(secretKey)
                .parseClaimsJws(token)
                .getBody();
    }

    private Claims getClaims(final Map<String, Object> attributes, final Long expiresInSec) {
        final Claims claims = Jwts.claims();
        claims.setIssuer(issuer);
        claims.setIssuedAt(clock.now());
        claims.putAll(attributes);
        if (expiresInSec > 0) {
            claims.setExpiration(new Date(System.currentTimeMillis() + expiresInSec * 1000));
        }
        return claims;
    }
}
