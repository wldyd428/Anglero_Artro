package com.anglero.artro.config.jwt;

public interface JwtProperties {
    String SECRET = "cos";
    int EXPIRATION_TIME = 60000*10*6;
    String TOKEN_PREFIX = "Bearer ";
    String HEADER_STRING = "Authorization";

}
