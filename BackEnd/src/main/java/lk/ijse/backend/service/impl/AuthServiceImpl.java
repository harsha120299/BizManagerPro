package lk.ijse.backend.service.impl;

import lk.ijse.backend.dto.AuthDTO;
import lk.ijse.backend.dto.AuthResponseDTO;
import lk.ijse.backend.dto.RegisterDTO;
import lk.ijse.backend.entity.User;
import lk.ijse.backend.repository.UserRepository;
import lk.ijse.backend.service.AuthService;
import lk.ijse.backend.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    public AuthResponseDTO authenticate(AuthDTO authDTO) {
        User user=
                userRepository.findByEmail(authDTO.getEmail())
                        .orElseThrow(
                                ()->new UsernameNotFoundException
                                        ("Username not found"));
        if (!passwordEncoder.matches(
                authDTO.getPassword(),
                user.getPassword())) {
            throw new BadCredentialsException("Incorrect password");
        }
        String token=jwtUtil.generateToken(authDTO.getEmail());
        String refreshToken=jwtUtil.generateRefreshToken(authDTO.getEmail());
        String role = String.valueOf(user.getRole());
        return  new AuthResponseDTO(token,user,role,refreshToken);
    }
    public String register(RegisterDTO registerDTO) {
        if(userRepository.findUserByUsername(
                registerDTO.getUsername()).isPresent()){
            throw new RuntimeException("Username already exists");
        }
        User user=User.builder()
                .username(registerDTO.getUsername())
                .password(passwordEncoder.encode(
                        registerDTO.getPassword()))
                .email(registerDTO.getEmail())
                .contactNumber(registerDTO.getContactNumber())
                .build();
        userRepository.save(user);
        return  "User Registration Success";
    }
}
