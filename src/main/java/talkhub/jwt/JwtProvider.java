package talkhub.jwt;

import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import talkhub.model.User;

import javax.crypto.SecretKey;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;


@Component
public class JwtProvider {

    private final SecretKey jwtAccessSecret;

    public JwtProvider(@Value("${jwt.secret}") String jwtAccessSecret) {
        this.jwtAccessSecret = Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtAccessSecret));
    }

    public String generateAccessToken(@NonNull User user) {
        final LocalDateTime now = LocalDateTime.now();
        final Instant accessExpirationInstant = now.plusYears(1).atZone(ZoneId.systemDefault()).toInstant();
        final Date accessExpiration = Date.from(accessExpirationInstant);
        return Jwts.builder()
                .subject(user.getLogin())
                .expiration(accessExpiration)
                .signWith(jwtAccessSecret)
                .compact();
    }

    public boolean validateAccessToken(@NonNull String accessToken) {
        return validateToken(accessToken, jwtAccessSecret);
    }

    private boolean validateToken(@NonNull String token, @NonNull SecretKey secret) {
        try {
            Jwts.parser()
                    .verifyWith(secret)
                    .build()
                    .parseSignedClaims(token);
            return true;
        } catch (ExpiredJwtException expEx) {
            System.out.println("Token expired " + expEx.getMessage());
        } catch (UnsupportedJwtException unsEx) {
            System.out.println("Unsupported jwt" + unsEx.getMessage());
        } catch (MalformedJwtException mjEx) {
            System.out.println("Malformed jwt" + mjEx.getMessage());
        } catch (SignatureException sEx) {
            System.out.println("Invalid signature" + sEx.getMessage());
        } catch (Exception e) {
            System.out.println("invalid token" + e.getMessage());
        }
        return false;
    }

    public String getLogin(@NonNull String token) {
        return Jwts.parser()
                .verifyWith(jwtAccessSecret)
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .getSubject();
    }
}
