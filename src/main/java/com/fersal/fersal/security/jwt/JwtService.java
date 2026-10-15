package com.fersal.fersal.security.jwt;


import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;

import org.springframework.stereotype.Service;


import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;


@Service
public class JwtService {


    private static final String SECRET_KEY =
            "FER-SAL-SECRET-KEY-2026-MUY-LARGO-SEGURIDAD";


    private final Key key =
            Keys.hmacShaKeyFor(
                    SECRET_KEY.getBytes(StandardCharsets.UTF_8)
            );


    public String generarToken(String email) {


        return Jwts.builder()

                .subject(email)

                .issuedAt(new Date())

                .expiration(
                        new Date(
                                System.currentTimeMillis()
                                        +
                                        1000 * 60 * 60 * 24
                        )
                )

                .signWith(key)

                .compact();

    }


    public String extraerEmail(String token) {


        return Jwts.parser()

                .verifyWith((javax.crypto.SecretKey) key)

                .build()

                .parseSignedClaims(token)

                .getPayload()

                .getSubject();

    }


    public boolean validarToken(String token) {


        try {

            Jwts.parser()

                    .verifyWith(
                            (javax.crypto.SecretKey) key
                    )

                    .build()

                    .parseSignedClaims(token);


            return true;


        } catch (Exception e) {

            return false;

        }

    }

}