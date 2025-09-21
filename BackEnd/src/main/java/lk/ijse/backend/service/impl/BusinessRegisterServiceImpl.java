package lk.ijse.backend.service.impl;

import lk.ijse.backend.dto.BusinessDTO;
import lk.ijse.backend.dto.UserBusinessDTO;
import lk.ijse.backend.entity.Business;
import lk.ijse.backend.entity.User;
import lk.ijse.backend.entity.UserBusiness;
import lk.ijse.backend.repository.BusinessRepository;
import lk.ijse.backend.repository.UserBusinessRepository;
import lk.ijse.backend.repository.UserRepository;
import lk.ijse.backend.service.BusinessRegisterService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import static java.util.Arrays.stream;


@Service
@RequiredArgsConstructor
public class BusinessRegisterServiceImpl implements BusinessRegisterService {

    private final BusinessRepository BUSINESS_REPOSITORY;
    private final UserRepository USER_REPOSITORY;
    private final UserBusinessRepository USER_BUSINESS_REPOSITORY;
    private final ModelMapper MODEL_MAPPER;


    @Override
    public String save(BusinessDTO businessDTO) {
        if(BUSINESS_REPOSITORY.findByName(businessDTO.getName()).isPresent()){
            throw new RuntimeException("Business already exists");
        }
        Business business = Business.builder()
                .name(businessDTO.getName())
                .type(businessDTO.getType())
                .currencyType(businessDTO.getCurrencyType())
                .isEmployeeExist(businessDTO.isEmployeeExist())
                .address(businessDTO.getAddress())
                .createdDate(LocalDate.now())
                .build();

        BUSINESS_REPOSITORY.save(business);

        String username = ((UserDetails) SecurityContextHolder.getContext()
                .getAuthentication().getPrincipal()).getUsername();

        User currentUser = USER_REPOSITORY.findByEmail(username)
                .orElseThrow(() -> new RuntimeException("User not found: " + username));

        UserBusiness userBusiness = UserBusiness.builder()
                .user(currentUser)
                .business(business)
                .build();

        USER_BUSINESS_REPOSITORY.save(userBusiness);

        return "Business registered successfully";
    }

    @Override
    public boolean isBusinessNameExist(String name) {
        return BUSINESS_REPOSITORY.existsByNameIgnoreCase(name);
    }

    @Override
    public List<BusinessDTO> getBusinessesByUserId(Long userId) {
        List<Business> businesses = BUSINESS_REPOSITORY.findBusinessesByUserId(userId);
        if(businesses.isEmpty()){
        throw new RuntimeException("Business not found");
        }
        return businesses.stream()
                .map(b -> MODEL_MAPPER.map(b, BusinessDTO.class))
                .collect(Collectors.toList());
    }



}
