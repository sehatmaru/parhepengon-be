package xcode.parhepengon.service;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.stereotype.Service;
import xcode.parhepengon.domain.model.UserModel;

import java.util.Date;

import static xcode.parhepengon.shared.Utils.getTomorrowDate;

@Service
public class JwtService {

    public String generateToken(UserModel user) {
        return Jwts.builder()
                .setSubject(user.getSecureId())
                .setIssuedAt(new Date())
                .setExpiration(getTomorrowDate())
                .signWith(SignatureAlgorithm.HS256, "xcode")
                .compact();
    }
}
