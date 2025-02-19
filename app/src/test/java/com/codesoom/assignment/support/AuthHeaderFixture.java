package com.codesoom.assignment.support;

public enum AuthHeaderFixture {
    VALID_TOKEN_1(
            "Bearer",
            "12345678901234567890123456789010",
            1L,
            "eyJhbGciOiJIUzI1NiJ9.eyJ1c2VySWQiOjF9.neCsyNLzy3lQ4o2yliotWT06FwSGZagaHpKdAkjnGGw",
            "Bearer eyJhbGciOiJIUzI1NiJ9.eyJ1c2VySWQiOjF9.neCsyNLzy3lQ4o2yliotWT06FwSGZagaHpKdAkjnGGw"
    ),
    VALID_TOKEN_2(
            "Bearer",
            "12345678901234567890123456789010",
            2L,
            "eyJhbGciOiJIUzI1NiJ9.eyJ1c2VySWQiOjJ9.i-iHszAs6H2JFTdm3vOVuN18tb_w6n2FqEYIRtr6gaU",
            "Bearer eyJhbGciOiJIUzI1NiJ9.eyJ1c2VySWQiOjJ9.i-iHszAs6H2JFTdm3vOVuN18tb_w6n2FqEYIRtr6gaU"
    ),
    VALID_ADMIN_TOKEN_1004(
            "Bearer",
            "12345678901234567890123456789010",
            1004L,
            "eyJhbGciOiJIUzI1NiJ9.eyJ1c2VySWQiOjEwMDR9.iWkN84MBJ12T5IN0ZR_UVrPdfrh6cZP6R2McdZpW8zk",
            "Bearer eyJhbGciOiJIUzI1NiJ9.eyJ1c2VySWQiOjEwMDR9.iWkN84MBJ12T5IN0ZR_UVrPdfrh6cZP6R2McdZpW8zk"
    ),
    INVALID_TYPE_TOKEN_1(
            "Gibeom",
            "12345678901234567890123456789010",
            1L,
            "eyJhbGciOiJIUzI1NiJ9.eyJ1c2VySWQiOjF9.neCsyNLzy3lQ4o2yliotWT06FwSGZagaHpKdAkjnGGw",
            "Giibeom eyJhbGciOiJIUzI1NiJ9.eyJ1c2VySWQiOjF9.neCsyNLzy3lQ4o2yliotWT06FwSGZagaHpKdAkjnGGw"
    ),
    INVALID_VALUE_TOKEN_1(
            "Bearer",
            "12345678901234567890123456789010",
            1L,
            "eyJhbGciOiJIUzI1NiJ9.eyJ1c2VySWQiOjF9.ZZ3CUxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx",
            "Bearer eyJhbGciOiJIUzI1NiJ9.eyJ1c2VySWQiOjF9.ZZ3CUxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx"
    )
    ;

    private final String tokenType;
    private final String secretKey;
    private final Long userId;
    private final String tokenValue;
    private final String tokenHeader;

    AuthHeaderFixture(String tokenType, String secretKey, Long userId, String tokenValue, String tokenHeader) {
        this.tokenType = tokenType;
        this.secretKey = secretKey;
        this.userId = userId;
        this.tokenValue = tokenValue;
        this.tokenHeader = tokenHeader;
    }

    public String 토큰_타입() {
        return tokenType;
    }

    public String 시크릿_키() {
        return secretKey;
    }

    public Long 아이디() {
        return userId;
    }

    public String 토큰_값() {
        return tokenValue;
    }

    public String 인증_헤더값() {
        return tokenHeader;
    }
}
