package lk.ijse.backend.service;

import lk.ijse.backend.dto.CustomerDTO;
import org.springframework.data.domain.Page;

import java.util.List;

public interface CustomerService {
    String addCustomer(CustomerDTO customerDTO);
    String updateCustomer(CustomerDTO customerDTO);
    String deleteCustomer(Long customerId);
    List<CustomerDTO> getAllCustomers(Long businessId);
    Page<CustomerDTO> getAllCustomersByPages(Long businessId, int page, int size, String search);
}
