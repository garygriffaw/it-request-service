package com.garygriffaw.itrequestservice.services;

import com.garygriffaw.itrequestservice.config.JwtService;
import com.garygriffaw.itrequestservice.controllers.UnprocessableEntityException;
import com.garygriffaw.itrequestservice.entities.Role;
import com.garygriffaw.itrequestservice.enums.RoleEnum;
import com.garygriffaw.itrequestservice.model.UserAuthenticationResponseDTO;
import com.garygriffaw.itrequestservice.model.UserAuthenticationDTO;
import com.garygriffaw.itrequestservice.model.UserRegisterDTO;
import com.garygriffaw.itrequestservice.repositories.RoleRepository;
import com.garygriffaw.itrequestservice.token.Token;
import com.garygriffaw.itrequestservice.token.TokenRepository;
import com.garygriffaw.itrequestservice.token.TokenType;
import com.garygriffaw.itrequestservice.entities.User;
import com.garygriffaw.itrequestservice.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final TokenRepository tokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final UserDetailsServiceImpl userDetailsService;

    public UserAuthenticationResponseDTO register(UserRegisterDTO request) {
        if (userRepository.existsUserByUsername(request.getUsername())) {
            throw new UnprocessableEntityException();
        }

        User user = User.builder()
                .username(request.getUsername())
                .firstname(request.getFirstname())
                .lastname(request.getLastname())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .build();

        Role userRole = roleRepository.findByRole(RoleEnum.USER).get();
        user.addRole(userRole);

        User savedUser = userRepository.save(user);

        return getAuthenticationResponse(savedUser);
    }

    public UserAuthenticationResponseDTO authenticate(UserAuthenticationDTO request) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                request.getUsername(), request.getPassword()));

        User user = userRepository.findByUsername(request.getUsername()).orElseThrow();

        return getAuthenticationResponse(user);
    }

    private UserAuthenticationResponseDTO getAuthenticationResponse(User user) {
        revokeAllValidUserTokens(user);

        String jwtToken = jwtService.generateToken(userDetailsService.getUserDetailsUser(user));
        saveUserToken(user, jwtToken);

        return UserAuthenticationResponseDTO.builder()
                .token(jwtToken)
                .build();
    }

    private void revokeAllValidUserTokens(User user) {
        var validUserTokens = tokenRepository.findAllValidTokensByUserId(user.getId());

        if (validUserTokens.isEmpty())
            return;

        validUserTokens.forEach(token -> {
            token.setExpired(true);
            token.setRevoked(true);
        });

        tokenRepository.saveAll(validUserTokens);
    }

    private void saveUserToken(User user, String jwtToken) {
        Token token = Token.builder()
                .user(user)
                .token(jwtToken)
                .tokenType(TokenType.BEARER)
                .expired(false)
                .revoked(false)
                .build();

        tokenRepository.save(token);
    }
}
