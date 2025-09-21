package lk.ijse.backend.service.impl;

import lk.ijse.backend.dto.CustomerDTO;
import lk.ijse.backend.dto.MaterialDTO;
import lk.ijse.backend.entity.Business;
import lk.ijse.backend.entity.Customer;
import lk.ijse.backend.entity.Material;
import lk.ijse.backend.exception.ResourceNotFoundException;
import lk.ijse.backend.repository.BusinessRepository;
import lk.ijse.backend.repository.CustomerRepository;
import lk.ijse.backend.service.CustomerService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepository CUSTOMER_REPOSITORY;
    private final BusinessRepository BUSINESS_REPOSITORY;
    private final ModelMapper MODEL_MAPPER;

    @Override
    public String addCustomer(CustomerDTO customerDTO) {
        Business business = BUSINESS_REPOSITORY.findById(customerDTO.getBusinessId())
                .orElseThrow(() -> new ResourceNotFoundException("Business not found"));

        // Check if a customer with same name exists in the same business
        if (!CUSTOMER_REPOSITORY.findCustomersByNameAndBusinessId(customerDTO.getName(), customerDTO.getBusinessId()).isEmpty()) {
            throw new RuntimeException("Customer with this name already exists in this business");
        }

        Customer customer = Customer.builder()
                .name(customerDTO.getName())
                .email(customerDTO.getEmail())
                .phone(customerDTO.getPhone())
                .address(customerDTO.getAddress())
                .business(business)
                .build();

        CUSTOMER_REPOSITORY.save(customer);
        return "Customer added successfully";
    }

    @Override
    public String updateCustomer(CustomerDTO customerDTO) {
        Customer customer = CUSTOMER_REPOSITORY.findById(customerDTO.getCustomerId())
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found"));

        Business business = BUSINESS_REPOSITORY.findById(customerDTO.getBusinessId())
                .orElseThrow(() -> new ResourceNotFoundException("Business not found"));

        customer.setName(customerDTO.getName());
        customer.setEmail(customerDTO.getEmail());
        customer.setPhone(customerDTO.getPhone());
        customer.setAddress(customerDTO.getAddress());
        customer.setBusiness(business);

        CUSTOMER_REPOSITORY.save(customer);
        return "Customer updated successfully";
    }

    @Override
    public String deleteCustomer(Long customerId) {
        Customer customer = CUSTOMER_REPOSITORY.findById(customerId)
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found"));

        CUSTOMER_REPOSITORY.delete(customer);
        return "Customer deleted successfully";
    }

    @Override
    public List<CustomerDTO> getAllCustomers(Long businessId) {
        List<Customer> customers = CUSTOMER_REPOSITORY.findAllCustomerByBusinessId(businessId);

        if (customers.isEmpty()) {
            throw new ResourceNotFoundException("Customers not found");
        }

        return customers.stream()
                .map(customer -> MODEL_MAPPER.map(customer, CustomerDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    public Page<CustomerDTO> getAllCustomersByPages(Long businessId, int page, int size, String search) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Customer> customerPage;
        customerPage = CUSTOMER_REPOSITORY .findByBusinessIdAndNameContainingIgnoreCase(businessId,search, pageable);
        Page<CustomerDTO> dtoPage = customerPage.map(customer -> MODEL_MAPPER.map(customer, CustomerDTO.class));
        if (dtoPage.isEmpty()) {
            throw new ResourceNotFoundException("Materials not found");
        }
        return dtoPage;
    }

}
