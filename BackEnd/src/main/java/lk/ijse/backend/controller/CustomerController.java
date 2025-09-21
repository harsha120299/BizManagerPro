package lk.ijse.backend.controller;

import lk.ijse.backend.dto.ApiResponse;
import lk.ijse.backend.dto.CustomerDTO;
import lk.ijse.backend.dto.MaterialDTO;
import lk.ijse.backend.service.CustomerService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/customer")
@RequiredArgsConstructor
public class CustomerController {

    private final CustomerService CUSTOMER_SERVICE;

    @PostMapping("/add")
    public ResponseEntity<ApiResponse> addCustomer(@RequestBody CustomerDTO customerDTO) {
        System.out.println("Customer adding");
        return ResponseEntity.ok(new ApiResponse(
                200,
                "OK",
                CUSTOMER_SERVICE.addCustomer(customerDTO)));
    }

    @GetMapping("/getAllList/{businessId}")
    public ResponseEntity<ApiResponse> getAllCustomers(@PathVariable Long businessId) {
        return ResponseEntity.ok(new ApiResponse(
                200,
                "OK",
                CUSTOMER_SERVICE.getAllCustomers(businessId)
        ));
    }
    @GetMapping("/getAll/{businessId}")
    public ResponseEntity<ApiResponse> getAllCustomersPaginated(
            @PathVariable Long businessId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String search
    )
    {
        System.out.println("Customer paginated");
        Page<CustomerDTO> customersPage = CUSTOMER_SERVICE.getAllCustomersByPages(businessId, page, size, search);
        System.out.println("Customer paginated"+customersPage.getTotalElements());
        return ResponseEntity.ok(new ApiResponse(200, "OK", customersPage));
    }


    @PutMapping("/update")
    public ResponseEntity<ApiResponse> updateCustomer(@RequestBody CustomerDTO customerDTO) {
        return ResponseEntity.ok(new ApiResponse(
                200,
                "OK",
                CUSTOMER_SERVICE.updateCustomer(customerDTO)
        ));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<ApiResponse> deleteCustomer(@PathVariable Long id) {
        return ResponseEntity.ok(new ApiResponse(
                200,
                "OK",
                CUSTOMER_SERVICE.deleteCustomer(id)
        ));
    }

}
