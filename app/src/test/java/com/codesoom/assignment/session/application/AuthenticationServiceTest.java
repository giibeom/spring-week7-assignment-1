package com.codesoom.assignment.session.application;

import com.codesoom.assignment.common.utils.JwtUtil;
import com.codesoom.assignment.session.application.exception.InvalidTokenException;
import com.codesoom.assignment.session.application.exception.LoginFailException;
import com.codesoom.assignment.session.domain.Role;
import com.codesoom.assignment.session.domain.RoleRepository;
import com.codesoom.assignment.user.domain.User;
import com.codesoom.assignment.user.domain.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Arrays;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.codesoom.assignment.support.AuthHeaderFixture.INVALID_VALUE_TOKEN_1;
import static com.codesoom.assignment.support.AuthHeaderFixture.VALID_ADMIN_TOKEN_1004;
import static com.codesoom.assignment.support.AuthHeaderFixture.VALID_TOKEN_1;
import static com.codesoom.assignment.support.IdFixture.ID_MIN;
import static com.codesoom.assignment.support.UserFixture.USER_1;
import static com.codesoom.assignment.support.UserFixture.USER_2;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

@DisplayName("AuthenticationService 유닛 테스트")
class AuthenticationServiceTest {
    private static final String SECRET = VALID_TOKEN_1.시크릿_키();

    private AuthenticationService authenticationService;

    private final UserRepository userRepository = mock(UserRepository.class);
    private final RoleRepository roleRepository = mock(RoleRepository.class);
    private PasswordEncoder passwordEncoder;

    @BeforeEach
    void setUp() {
        JwtUtil jwtUtil = new JwtUtil(SECRET);
        passwordEncoder = new BCryptPasswordEncoder();
        authenticationService = new AuthenticationService(userRepository, roleRepository, jwtUtil, passwordEncoder);
    }

    @Nested
    @DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
    class login_메서드는 {

        @Nested
        @DisplayName("유효한 회원 정보가 주어지면")
        class Context_with_valid_user {

            @BeforeEach
            void setUp() {
                User user = USER_1.회원_엔티티_생성(ID_MIN.value());

                user.changePassword(USER_1.비밀번호(), passwordEncoder);

                given(userRepository.findByEmail(USER_1.이메일()))
                        .willReturn(Optional.of(user));
            }

            @Test
            @DisplayName("토큰을 리턴한다")
            void it_returns_() {
                String token = authenticationService.login(USER_1.이메일(), USER_1.비밀번호());

                assertThat(token).isEqualTo(VALID_TOKEN_1.토큰_값());

                verify(userRepository).findByEmail(USER_1.이메일());
            }
        }

        @Nested
        @DisplayName("유효하지 않은 회원 정보가 주어지면")
        class Context_with_invalid_user {

            @Nested
            @DisplayName("찾을 수 없는 계정일 경우")
            class Context_with_not_exist_email {

                @BeforeEach
                void setUp() {
                    given(userRepository.findByEmail(USER_2.이메일()))
                            .willReturn(Optional.empty());
                }

                @Test
                @DisplayName("LoginFailException 예외를 던진다")
                void it_returns_exception() {
                    assertThatThrownBy(() -> authenticationService.login(USER_2.이메일(), USER_2.비밀번호()))
                            .isInstanceOf(LoginFailException.class);

                    verify(userRepository).findByEmail(USER_2.이메일());
                }
            }

            @Nested
            @DisplayName("틀린 비밀번호일 경우")
            class Context_with_exist_email {

                @BeforeEach
                void setUp() {
                    given(userRepository.findByEmail(USER_1.이메일()))
                            .willReturn(Optional.of(USER_1.회원_엔티티_생성(ID_MIN.value())));
                }

                @Test
                @DisplayName("LoginFailException 예외를 던진다")
                void it_returns_exception() {
                    assertThatThrownBy(() -> authenticationService.login(USER_1.이메일(), USER_2.비밀번호()))
                            .isInstanceOf(LoginFailException.class);

                    verify(userRepository).findByEmail(USER_1.이메일());
                }
            }
        }
    }


    @Nested
    @DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
    class parseToken_메서드는 {

        @Nested
        @DisplayName("유효한 토큰이 주어지면")
        class Context_with_valid_token {

            @Test
            @DisplayName("회원 고유 id를 리턴한다")
            void it_returns_user_id() {
                Long userId = authenticationService.parseToken(VALID_TOKEN_1.토큰_값());

                assertThat(userId).isEqualTo(VALID_TOKEN_1.아이디());
            }
        }

        @Nested
        @DisplayName("유효하지 않은 토큰이 주어지면")
        class Context_with_invalid_token {

            @Test
            @DisplayName("InvalidTokenException 예외를 던진다")
            void it_returns_excpetion() {
                assertThatThrownBy(() -> authenticationService.parseToken(INVALID_VALUE_TOKEN_1.토큰_값()))
                        .isInstanceOf(InvalidTokenException.class);
            }
        }
    }

    @Nested
    @DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
    class roles_메서드는 {

        @Nested
        @DisplayName("유저 토큰이 주어지면")
        class Context_with_user_token {

            @BeforeEach
            void setUp() {
                given(roleRepository.findAllByUserId(eq(VALID_TOKEN_1.아이디())))
                        .willReturn(Arrays.asList(new Role(VALID_TOKEN_1.아이디(), "USER")));
            }

            @Test
            @DisplayName("\"USER\"를 반환한다")
            void it_returs_users() {
                assertThat(
                        authenticationService.roles(VALID_TOKEN_1.아이디()).stream()
                                .map(Role::getName)
                                .collect(Collectors.toList())
                ).isEqualTo(Arrays.asList("USER"));
            }
        }

        @Nested
        @DisplayName("관리자 토큰이 주어지면")
        class Context_with_admin_token {

            @BeforeEach
            void setUp() {
                given(roleRepository.findAllByUserId(eq(VALID_ADMIN_TOKEN_1004.아이디())))
                        .willReturn(Arrays.asList(new Role(VALID_ADMIN_TOKEN_1004.아이디(), "ADMIN")));
            }

            @Test
            @DisplayName("\"ADMIN\"를 반환한다")
            void it_returs_admin() {
                assertThat(
                        authenticationService.roles(VALID_ADMIN_TOKEN_1004.아이디()).stream()
                                .map(Role::getName)
                                .collect(Collectors.toList())
                ).isEqualTo(Arrays.asList("ADMIN"));
            }
        }
    }
}
