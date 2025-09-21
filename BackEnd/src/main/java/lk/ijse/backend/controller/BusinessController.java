package lk.ijse.backend.controller;

import lk.ijse.backend.dto.ApiResponse;
import lk.ijse.backend.dto.BusinessDTO;
import lk.ijse.backend.entity.CurrencyType;
import lk.ijse.backend.service.BusinessRegisterService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/business")
@RequiredArgsConstructor
public class BusinessController {

    private final BusinessRegisterService BUSINESS_REGISTER_SERVICE;

    @PostMapping("/add")
    public ResponseEntity<ApiResponse> register(@RequestBody BusinessDTO businessDTO) {
        System.out.println("Business add");
        return ResponseEntity.ok(new ApiResponse(
                200,
                "OK",
                BUSINESS_REGISTER_SERVICE.save(businessDTO)));
    }
    @GetMapping("/currencies")
    public ResponseEntity<ApiResponse> getAllCurrencies() {
        System.out.println("Currencies list");
        return ResponseEntity.ok(
                new ApiResponse(
                        200,
                        "OK",
                        CurrencyType.values()  // returns array of enum names
                )
        );
    }
    @GetMapping("/exists")
    public ResponseEntity<ApiResponse> checkBusinessName(@RequestParam String name) {
        boolean exists = BUSINESS_REGISTER_SERVICE.isBusinessNameExist(name);
        return ResponseEntity.ok(
                new ApiResponse(
                        200,
                        "OK",
                        exists
                )
        );
    }
    @GetMapping("/user/{userId}")
    public ResponseEntity<ApiResponse> getBusinessesByUserId(@PathVariable Long userId) {
        System.out.println("Getting businesses for user: " + userId);
        return ResponseEntity.ok(
                new ApiResponse(
                        200,
                        "OK",
                        BUSINESS_REGISTER_SERVICE.getBusinessesByUserId(userId)
                )
        );
    }

}
