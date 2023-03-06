package me.lesterfernandez.CourseScheduler.auth;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import javax.crypto.SecretKey;
import me.lesterfernandez.CourseScheduler.user.UserEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@Component
public class JwtComponent {

  public static final long JWT_TOKEN_VALIDITY = 60 * 60 * 24;
  private final static SecretKey JWT_SECRET = Keys.hmacShaKeyFor(
      "secret............................".getBytes(StandardCharsets.UTF_8));

  public String getUsernameFromToken(String token) {
    return getTokenClaims(token).getSubject();
  }

  public Date getExpirationDateFromToken(String token) {
    return getTokenClaims(token).getExpiration();
  }

  private Claims getTokenClaims(String token) {
    return Jwts
        .parserBuilder()
        .setSigningKey(JWT_SECRET)
        .build()
        .parseClaimsJws(token)
        .getBody();
  }

  private boolean isTokenExpired(String token) {
    final Date expiration = getExpirationDateFromToken(token);
    return expiration.before(new Date());
  }

  public String generateToken(UserEntity userEntity) {
    return generateToken(userEntity.getUsername());
  }

  public String generateToken(String username) {
    return Jwts.builder()
        .setSubject(username)
        .setIssuedAt(new Date(System.currentTimeMillis()))
        .setExpiration(new Date(System.currentTimeMillis() + JWT_TOKEN_VALIDITY * 1000))
        .signWith(JWT_SECRET)
        .compact();
  }

  public boolean validateToken(String token, UserEntity userEntity) {
    if (!StringUtils.hasText(token)) {
      return false;
    }

    final String username = getUsernameFromToken(token);
    return (userEntity != null && username.equals(userEntity.getUsername()) && !isTokenExpired(
        token));
  }

  public boolean validateToken(String token) {
    return (StringUtils.hasText(token) && !isTokenExpired(token));
  }


}
