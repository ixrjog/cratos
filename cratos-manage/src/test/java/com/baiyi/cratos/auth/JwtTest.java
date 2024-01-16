package com.baiyi.cratos.auth;

import com.baiyi.cratos.BaseUnit;
import io.jsonwebtoken.Jwts;
import org.junit.jupiter.api.Test;

import javax.crypto.SecretKey;

/**
 * @Author baiyi
 * @Date 2024/1/16 10:50
 * @Version 1.0
 */
public class JwtTest extends BaseUnit {

    // https://github.com/jwtk/jjwt#installation

    @Test
    void test() {
        SecretKey key = Jwts.SIG.HS256.key().build();
        String jws = Jwts.builder().subject("Joe").signWith(key).compact();
        System.out.println(jws);
        assert Jwts.parser().verifyWith(key).build().parseSignedClaims(jws).getPayload().getSubject().equals("Joe");

    }

}
