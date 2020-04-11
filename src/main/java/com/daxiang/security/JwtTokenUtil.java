package com.daxiang.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;

/**
 * Created by jiangyitao.
 */
public class JwtTokenUtil {

    private static final String SECRET_KEY = "opendx";
    private static final long TOKEN_EXPIRE_IN_MS = 30 * 24 * 60 * 60 * 1000L; // token过期时间: 30天

    private static final String TOKEN_HTTP_REQUEST_HEADER = "token";

    public static String createToken(String username) {
        Claims claims = Jwts.claims().setSubject(username);

        return Jwts.builder()
                .setClaims(claims)
                .setExpiration(new Date(System.currentTimeMillis() + TOKEN_EXPIRE_IN_MS))
                .signWith(SignatureAlgorithm.HS256, SECRET_KEY)
                .compact();
    }

    public static String parseUsername(String token) {
        try {
            // token错误，过期 都会抛出异常
            return Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(token).getBody().getSubject();
        } catch (Exception e) {
            return null;
        }
    }

    public static String getTokenFromHttpRequestHeader(HttpServletRequest request) {
        return request.getHeader(TOKEN_HTTP_REQUEST_HEADER);
    }

}
