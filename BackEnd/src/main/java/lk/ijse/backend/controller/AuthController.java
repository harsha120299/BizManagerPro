package lk.ijse.backend.controller;

import lk.ijse.backend.dto.ApiResponse;
import lk.ijse.backend.dto.AuthDTO;
import lk.ijse.backend.dto.AuthResponseDTO;
import lk.ijse.backend.dto.RegisterDTO;
import lk.ijse.backend.service.AuthService;
import lk.ijse.backend.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final JwtUtil jwtUtil;

    @PostMapping("/register")
    public ResponseEntity<ApiResponse> registerUser(@RequestBody RegisterDTO registerDTO) {
        System.out.println(registerDTO);
        return ResponseEntity.ok(new ApiResponse(
                200,
                "OK",
                authService.register(registerDTO)));
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse> login(@RequestBody AuthDTO authDTO) {
        AuthResponseDTO authResponseDTO = authService.authenticate(authDTO);

        ResponseCookie refreshCookie = ResponseCookie.from("refreshToken", authResponseDTO.getRefreshToken())
                .httpOnly(true)
                .secure(false)        // false for local HTTP
                .path("/")            // accessible to all endpoints
                .maxAge(60L * 60 * 24 * 30)
                .sameSite("none")      // "None" requires secure HTTPS
                .build();

        System.out.println("login refreshCookie " + refreshCookie.getValue());

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, refreshCookie.toString())
                .body(new ApiResponse(200, "OK", authResponseDTO));
    }

    @PostMapping("/refresh")
    public ResponseEntity<ApiResponse> refresh(@RequestBody Map<String, String> request) {
        String refreshToken = request.get("refreshToken");
        System.out.println("refreshToken"+refreshToken);

        if (refreshToken == null || !jwtUtil.validateToken(refreshToken)) {
            return ResponseEntity.status(401)
                    .body(new ApiResponse(401, "error", "Invalid or missing refresh token"));
        }

        if (!"refresh".equals(jwtUtil.getTokenType(refreshToken))) {
            return ResponseEntity.status(401)
                    .body(new ApiResponse(401, "error", "Token is not a refresh token"));
        }

        String username = jwtUtil.extractUsername(refreshToken);
        String newAccess = jwtUtil.generateToken(username);

        return ResponseEntity.ok(new ApiResponse(200, "OK", Map.of(
                "accessToken", newAccess,
                "tokenType", "Bearer",
                "expiresIn", 60 * 15
        )));
    }


    @PostMapping("/logout")
    public ResponseEntity<ApiResponse> logout() {
        System.out.println("logout");
        ResponseCookie cookie = ResponseCookie.from("refreshToken", "")
                .httpOnly(true)
                .secure(false) // false for local HTTP, true for HTTPS
                .path("/")     // should match cookie path
                .maxAge(0)
                .build();

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, cookie.toString())
                .body(new ApiResponse(200, "OK", "Logged out successfully"));
    }
}
