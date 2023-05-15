package com.blog.blograss.commons.jwt;

import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.blog.blograss.apis.adminuser.object.TokenDto;
import com.blog.blograss.commons.util.RedisUtil;

import java.security.Key;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.stream.Collectors;

@Component
public class JwtTokenProvider implements InitializingBean {

    private static final String AUTHORITIES_KEY = "authority";
    public static final String AUTHORIZATION_HEADER = "Authorization";
    public static final String REFRESH_AUTHORIZATION_HEADER = "RAuthorization";
    public static final String TOKEN_PREFIX = "Bearer ";
    public static final String AUTH_CODE = "auth";
    public static final String TOKEN_TYPE = "type";
    private final Logger logger = LoggerFactory.getLogger(JwtTokenProvider.class);
    private final String secret;

    private final RedisUtil redisUtil;
    private final long accessTokenValidityInMilliseconds;
    private final long refreshTokenValidityInMilliseconds;
    private final int accessTokenValidityInMinute;
    private final int refreshTokenValidityInMinute;

    private Key key;

    public JwtTokenProvider(
        @Value("${jwt.secret}") 
        String secret, 
        @Value("${jwt.access-token-validity-in-seconds}") 
        long accessTokenValidityInSeconds, 
        @Value("${jwt.refresh-token-validity-in-seconds}") 
        long refreshTokenValidityInSeconds, 
        RedisUtil redisUtil
        ){
        this.secret = secret;
        this.accessTokenValidityInMilliseconds = accessTokenValidityInSeconds * 1000;
        this.refreshTokenValidityInMilliseconds = refreshTokenValidityInSeconds * 1000;
        this.accessTokenValidityInMinute = (int) (accessTokenValidityInSeconds / 60);
        this.refreshTokenValidityInMinute = (int) (refreshTokenValidityInSeconds / 60);
        this.redisUtil = redisUtil;
    }

    @Override
    public void afterPropertiesSet() {
        byte[] keyBytes = Decoders.BASE64.decode(secret);
        this.key = Keys.hmacShaKeyFor(keyBytes);
    }

    public TokenDto createToken(Authentication authentication, String refreshToken) {

//      String authorities = authentication.getAuthorities().stream()
//              .map(GrantedAuthority::getAuthority)
//              .collect(Collectors.joining(","));

        String account = authentication.getName();
        String authorities = "ROLE_USER";
        long now = System.currentTimeMillis();
        Date validity = new Date(now + this.accessTokenValidityInMilliseconds);
    
        String accessToken = Jwts.builder()
                .setSubject(account)                                //토큰이름
                .setExpiration(validity)                            //만료시간
                .claim(TOKEN_TYPE, "Access")
                .claim(AUTHORITIES_KEY, authorities)                //페이로드
                .signWith(key, SignatureAlgorithm.HS512)            //서명
                .compact();


        validity = new Date(now + this.refreshTokenValidityInMilliseconds);

        redisUtil.set(accessToken, account, accessTokenValidityInMinute);

        return new TokenDto(accessToken, null);
    }

    public TokenDto createRefreshToken(Authentication authentication) {

        //      String authorities = authentication.getAuthorities().stream()
        //              .map(GrantedAuthority::getAuthority)
        //              .collect(Collectors.joining(","));
        
                String account = authentication.getName();
                String authorities = "ROLE_USER";
                long now = System.currentTimeMillis();
                Date validity = new Date(now + this.accessTokenValidityInMilliseconds);
                validity = new Date(now + this.refreshTokenValidityInMilliseconds);
        
                String refreshToken = Jwts.builder()
                        .setSubject(account)
                        .setExpiration(validity)
                        .claim(TOKEN_TYPE, "Refresh")
                        .claim(AUTHORITIES_KEY, authorities)
                        .signWith(key, SignatureAlgorithm.HS512)
                        .compact();

                redisUtil.set(refreshToken, account, refreshTokenValidityInMinute);
        
                return new TokenDto(null, refreshToken);
            }

    public Authentication getAuthentication(String token) {

        Claims claims = getClaims(token);

        Collection<? extends GrantedAuthority> authorities =
        Arrays.stream(claims.get(AUTHORITIES_KEY).toString().split(","))
              .map(SimpleGrantedAuthority::new)
              .collect(Collectors.toList());

        User principal = new User(claims.getSubject(), "", authorities);

        return new UsernamePasswordAuthenticationToken(principal, null, authorities);
    }

    public Claims getClaims(String token) {

        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public boolean validateToken(String token, HttpServletRequest req) {
        try {

            Claims claims = Jwts.parserBuilder()
                                .setSigningKey(key)
                                .build()
                                .parseClaimsJws(token)
                                .getBody();                                

            String email = (String) redisUtil.get(token);

            if(claims.get(TOKEN_TYPE).equals("Access") && email == null) {
                logger.info("로그아웃 상태입니다.");
                return false;
            }

            return true;

        } catch (io.jsonwebtoken.security.SecurityException | MalformedJwtException e) {
            logger.info("잘못된 JWT 서명입니다.");
        } catch (ExpiredJwtException e) {
            logger.info("만료된 JWT 토큰입니다.");
            req.setAttribute("exception", "ACCESSTOKEN_EXPRIED_ERR");
        } catch (UnsupportedJwtException e) {
            logger.info("지원되지 않는 JWT 토큰입니다.");
        } catch (IllegalArgumentException e) {
            logger.info("JWT 토큰이 잘못되었습니다.");
        }
        return false;
    }

    public String extractToken(HttpServletRequest request, String type) {

        String token = request.getHeader(type);

        if (StringUtils.hasText(token) && token.startsWith(TOKEN_PREFIX)) {
            return token.replace(TOKEN_PREFIX, "");
        }

        return null;
    }

    public String extractAccessToken(HttpServletRequest request){
        return extractToken(request, AUTHORIZATION_HEADER);
    }
    public String extractRefreshToken(HttpServletRequest request){
        return extractToken(request, REFRESH_AUTHORIZATION_HEADER);
    }

}