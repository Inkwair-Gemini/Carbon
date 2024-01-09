package utils;
import  io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.util.StringUtils;

import javax.crypto.SecretKey;
import java.util.Date;

public class JwtHelper {
    private static final long tokenExpiration = 365L * 24 * 60 * 60 * 1000;//有效时长
    private static final String tokenSignKey = "9JdJQITyln9TX+tBIa6h7qHTljZVp7q8y5D4ZtKFOJJY0Fzq70RRx15SM+fLnndxZ/PxZheGqhj/oZ2d4fWj9A==";//密钥

    // 根据用户 id 和用户名称， 生成token的字符串
    public static String createToken(String operatorCode, String clientId) {
        String token = Jwts.builder()
                //分类
                .setSubject("AUTH-USER")
                //设置token有效时长
                .setExpiration(new Date(System.currentTimeMillis() + tokenExpiration))
                //主体部分
                .claim("operatorCode", operatorCode)
                .claim("clientId", clientId)
                //签名部分
                .signWith(SignatureAlgorithm.HS512, tokenSignKey)
                .compressWith(CompressionCodecs.GZIP)
                .compact();
        return token;
    }

    public static String getOperatorCode(String token) {
        try {
            if (StringUtils.isEmpty(token)) return null;

            Jws<Claims> claimsJws = Jwts.parser().setSigningKey(tokenSignKey).parseClaimsJws(token);
            Claims claims = claimsJws.getBody();
            String userId = (String) claims.get("operatorCode");
            return userId;
        } catch (Exception e) {
            return null;
        }
    }

    public static String getClientId(String token) {
        try {
            if (StringUtils.isEmpty(token)) return "";

            Jws<Claims> claimsJws = Jwts.parser().setSigningKey(tokenSignKey).parseClaimsJws(token);
            Claims claims = claimsJws.getBody();
            return (String) claims.get("clientId");
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
