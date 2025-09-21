package lk.ijse.backend.controller;

import lk.ijse.backend.dto.ApiResponse;
import lk.ijse.backend.dto.EmployeeDTO;
import lk.ijse.backend.entity.Employee;
import lk.ijse.backend.service.EmployeeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/employee")
@RequiredArgsConstructor
public class EmployeeController {

    private final EmployeeService EMPLOYEE_SERVICE;

    @PostMapping("/add")
    public ResponseEntity<ApiResponse> addEmployee(@RequestBody EmployeeDTO employeeDTO) {
        return ResponseEntity.ok(new ApiResponse(
                200,
                "OK",
                EMPLOYEE_SERVICE.addEmployee(employeeDTO)));
    }
}
