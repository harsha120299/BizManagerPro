package lk.ijse.backend.service;

import lk.ijse.backend.dto.EmployeeDTO;

import java.util.List;

public interface EmployeeService {
    String addEmployee(EmployeeDTO employeeDTO);
    String updateEmployee(EmployeeDTO employeeDTO);
    String deleteEmployee(Long employeeId);
    List<EmployeeDTO> getAllEmployees();
}
