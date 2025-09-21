package lk.ijse.backend.service;

import lk.ijse.backend.dto.BusinessDTO;

import java.util.List;

public interface BusinessRegisterService {
    String save(BusinessDTO businessDTO);
    boolean isBusinessNameExist(String name);
    List<BusinessDTO> getBusinessesByUserId(Long userId);
}
