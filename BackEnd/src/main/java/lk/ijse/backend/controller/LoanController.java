package lk.ijse.backend.controller;

import lk.ijse.backend.dto.ApiResponse;
import lk.ijse.backend.dto.LoanDTO;
import lk.ijse.backend.service.LoanService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/loans")
@RequiredArgsConstructor
public class LoanController {

    private final LoanService LOAN_SERVICE;

    @PostMapping("/add")
    public ResponseEntity<ApiResponse> createLoan(@RequestBody LoanDTO loanDTO) {
        return ResponseEntity.ok(new ApiResponse(
                200,
                "OK",
                LOAN_SERVICE.saveLoan(loanDTO)));
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<ApiResponse> updateLoan(@PathVariable("id") Long id,
                                              @RequestBody LoanDTO loanDTO) {
        return ResponseEntity.ok(new ApiResponse(
                200,
                "OK",
                LOAN_SERVICE.updateLoan(id, loanDTO)));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse> getLoan(@PathVariable("id") Long id) {
        return ResponseEntity.ok(new ApiResponse(
                200,
                "OK",
                LOAN_SERVICE.getLoanById(id)));
    }

    @GetMapping
    public ResponseEntity<ApiResponse> getAllLoans() {
        return ResponseEntity.ok(new ApiResponse(
                200,
                "OK",
                LOAN_SERVICE.getAllLoans()));
    }

    @GetMapping("/business/{businessId}")
    public ResponseEntity<ApiResponse> getLoansByBusiness(@PathVariable Long businessId) {
        return ResponseEntity.ok(new ApiResponse(
                200,
                "OK",
                LOAN_SERVICE.getLoansByBusiness(businessId)));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<ApiResponse> deleteLoan(@PathVariable Long id) {
        return ResponseEntity.ok(new ApiResponse(
                200,
                "OK",
                LOAN_SERVICE.deleteLoan(id)
        ));
    }
}
