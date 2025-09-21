package lk.ijse.backend.service.impl;

import lk.ijse.backend.dto.EmployeeDTO;
import lk.ijse.backend.entity.Business;
import lk.ijse.backend.entity.Employee;
import lk.ijse.backend.exception.ResourceNotFoundException;
import lk.ijse.backend.repository.BusinessRepository;
import lk.ijse.backend.repository.EmployeeRepository;
import lk.ijse.backend.service.EmployeeService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EmployeeServiceImpl implements EmployeeService {

    private final EmployeeRepository EMPLOYEE_REPOSITORY;
    private final BusinessRepository BUSINESS_REPOSITORY;
    private final ModelMapper MODEL_MAPPER;

    @Override
    public String addEmployee(EmployeeDTO employeeDTO) {
        Business business = BUSINESS_REPOSITORY.findById(employeeDTO.getBusinessId())
                .orElseThrow(() -> new ResourceNotFoundException("Business"));

        if (EMPLOYEE_REPOSITORY.findEmployeeByName(employeeDTO.getName()).isPresent()) {
            throw new RuntimeException("Employee with this name already exists");
        }

        Employee employee = Employee.builder()
                .name(employeeDTO.getName())
                .phone(employeeDTO.getPhone())
                .address(employeeDTO.getAddress())
                .salary(employeeDTO.getSalary())
                .business(business)
                .build();

        EMPLOYEE_REPOSITORY.save(employee);
        return "Employee added successfully";
    }

    @Override
    public String updateEmployee(EmployeeDTO employeeDTO) {
        Employee employee = EMPLOYEE_REPOSITORY.findById(employeeDTO.getEmployeeId())
                .orElseThrow(() -> new ResourceNotFoundException("Employee"));

        Business business = BUSINESS_REPOSITORY.findById(employeeDTO.getBusinessId())
                .orElseThrow(() -> new ResourceNotFoundException("Business"));

        employee.setName(employeeDTO.getName());
        employee.setPhone(employeeDTO.getPhone());
        employee.setAddress(employeeDTO.getAddress());
        employee.setSalary(employeeDTO.getSalary());
        employee.setBusiness(business);

        EMPLOYEE_REPOSITORY.save(employee);
        return "Employee updated successfully";
    }

    @Override
    public String deleteEmployee(Long employeeId) {
        Employee employee = EMPLOYEE_REPOSITORY.findById(employeeId)
                .orElseThrow(() -> new ResourceNotFoundException("Employee"));

        EMPLOYEE_REPOSITORY.delete(employee);
        return "Employee deleted successfully";
    }

    @Override
    public List<EmployeeDTO> getAllEmployees() {
        List<Employee> employees = EMPLOYEE_REPOSITORY.findAll();
        if (employees.isEmpty()) {
            throw new ResourceNotFoundException("Employee");
        }
        return EMPLOYEE_REPOSITORY.findAll().stream().map(employees1 -> MODEL_MAPPER.map(employees1, EmployeeDTO.class)).collect(Collectors.toList());


    }
}
