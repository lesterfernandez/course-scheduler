package me.lesterfernandez.CourseScheduler.auth;

import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import java.util.Date;
import javax.crypto.SecretKey;
import org.springframework.stereotype.Service;

@Service
public class JwtService {

  private final static SecretKey JWT_SECRET = Keys.secretKeyFor(SignatureAlgorithm.HS256);

  public String generateJwt(String username) {
    Date currentDate = new Date();
    Date expirationDate = new Date(currentDate.getTime() + 1000 * 60 * 60 * 10);

    return Jwts.builder()
        .setSubject(username)
        .setIssuedAt(new Date())
        .setExpiration(expirationDate)
        .signWith(JWT_SECRET)
        .compact();
  }

  public String getUsernameFromJWT(String token) {
    JwtParser parser = Jwts.parserBuilder().setSigningKey(JWT_SECRET).build();
    return parser.parseClaimsJws(token).getBody().getSubject();
  }

  public boolean validateToken(String token) {
    try {
      return getUsernameFromJWT(token) != null;
    } catch (Exception ex) {
      System.out.println("invalid token");
      return false;
    }
  }
}
