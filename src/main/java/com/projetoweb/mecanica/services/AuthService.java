package com.projetoweb.mecanica.services;

import com.projetoweb.mecanica.dto.AuthResponseDto;
import com.projetoweb.mecanica.dto.LoginRequestDto;
import com.projetoweb.mecanica.dto.RegisterRequestDto;
import com.projetoweb.mecanica.entities.User;
import com.projetoweb.mecanica.repositories.UserRepository;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public AuthService(
            UserRepository userRepository,
            PasswordEncoder passwordEncoder,
            JwtService jwtService,
            AuthenticationManager authenticationManager
    ) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
    }

    public AuthResponseDto register(RegisterRequestDto request) {
        // Verificar se username já existe
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new RuntimeException("Username already exists");
        }

        // Verificar se email já existe
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email already exists");
        }

        // Criar novo usuário
        User user = new User();
        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole(request.getRole());
        user.setEnabled(true);

        userRepository.save(user);

        // Gerar token JWT
        String jwtToken = jwtService.generateToken(user);

        return new AuthResponseDto(
                jwtToken,
                user.getUsername(),
                user.getEmail(),
                user.getRole().name()
        );
    }

    public AuthResponseDto login(LoginRequestDto request) {
        // Autenticar usuário
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getUsername(),
                        request.getPassword()
                )
        );

        // Buscar usuário
        User user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Gerar token JWT
        String jwtToken = jwtService.generateToken(user);

        return new AuthResponseDto(
                jwtToken,
                user.getUsername(),
                user.getEmail(),
                user.getRole().name()
        );
    }
}
