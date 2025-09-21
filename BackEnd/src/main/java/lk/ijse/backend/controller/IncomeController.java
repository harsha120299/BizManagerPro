package lk.ijse.backend.controller;

import lk.ijse.backend.dto.ApiResponse;
import lk.ijse.backend.dto.IncomeDTO;
import lk.ijse.backend.service.IncomeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/incomes")
@RequiredArgsConstructor
public class IncomeController {

    private final IncomeService INCOME_SERVICE;

    @PostMapping("/add")
    public ResponseEntity<ApiResponse> createIncome(@RequestBody IncomeDTO incomeDTO) {
        return ResponseEntity.ok(new ApiResponse(
                200,
                "OK",
                INCOME_SERVICE.saveIncome(incomeDTO)));
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<ApiResponse> updateIncome(@PathVariable("id") Long id,
                                                  @RequestBody IncomeDTO incomeDTO) {
        return ResponseEntity.ok(new ApiResponse(
                200,
                "OK",
                INCOME_SERVICE.updateIncome(id, incomeDTO)));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse> getIncome(@PathVariable("id") Long id) {
        return ResponseEntity.ok(new ApiResponse(
                200,
                "OK",
                INCOME_SERVICE.getIncomeById(id)));
    }

    @GetMapping("/business/{businessId}")
    public ResponseEntity<ApiResponse> getIncomesByBusiness(@PathVariable Long businessId) {
        return ResponseEntity.ok(new ApiResponse(
                200,
                "OK",
                INCOME_SERVICE.getIncomesByBusiness(businessId)));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<ApiResponse> deleteIncome(@PathVariable Long id) {
        return ResponseEntity.ok(new ApiResponse(
                200,
                "OK",
                INCOME_SERVICE.deleteIncome(id)));
    }
}
