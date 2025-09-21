package lk.ijse.backend.service.impl;

import lk.ijse.backend.dto.MaterialDTO;
import lk.ijse.backend.entity.Business;
import lk.ijse.backend.entity.Material;
import lk.ijse.backend.exception.ResourceNotFoundException;
import lk.ijse.backend.repository.BusinessRepository;
import lk.ijse.backend.repository.MaterialRepository;
import lk.ijse.backend.service.MaterialService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MaterialServiceImpl implements MaterialService {

    private final MaterialRepository MATERIAL_REPOSITORY;
    private final BusinessRepository BUSINESS_REPOSITORY;
    private final ModelMapper MODEL_MAPPER;

    @Override
    public String saveMaterial(MaterialDTO materialDTO) {
        Business business = BUSINESS_REPOSITORY.findById(materialDTO.getBusinessId())
                .orElseThrow(() -> new ResourceNotFoundException("Business"));

        if (MATERIAL_REPOSITORY.findByName(materialDTO.getName()).isPresent()) {
            throw new RuntimeException("Material already exists with name: " + materialDTO.getName());
        }

        Material material = Material.builder()
                .name(materialDTO.getName())
                .type(materialDTO.getType())
                .quantity(materialDTO.getQuantity())
                .price(materialDTO.getPrice())
                .supplier(materialDTO.getSupplier())
                .business(business)
                .build();

        MATERIAL_REPOSITORY.save(material);
        return "Material saved successfully";
    }


    @Override
    public List<MaterialDTO> getAllMaterials(Long businessId) {
        List<Material> materials = MATERIAL_REPOSITORY.findMaterialsByBusinessId(businessId);
        if (materials.isEmpty()) {
            throw new ResourceNotFoundException("Materials");
        }
        return materials.stream()
                .map(material -> MODEL_MAPPER.map(material, MaterialDTO.class))
                .collect(Collectors.toList());
    }


    @Override
    public Page<MaterialDTO> getAllMaterialByPages(Long businessId, int page, int size, String search) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Material> materialsPage;
            materialsPage = MATERIAL_REPOSITORY .findByBusinessIdAndNameContainingIgnoreCase(businessId,search, pageable);
        Page<MaterialDTO> dtoPage = materialsPage.map(material -> MODEL_MAPPER.map(material, MaterialDTO.class));
        if (dtoPage.isEmpty()) {
            throw new ResourceNotFoundException("Materials not found");
        }
        return dtoPage;
    }

    @Override
    public String updateMaterial(MaterialDTO materialDTO) {
        Material existingMaterial = MATERIAL_REPOSITORY.findById(materialDTO.getMaterialId())
                .orElseThrow(() -> new ResourceNotFoundException("Material"));

        Business business = BUSINESS_REPOSITORY.findById(materialDTO.getBusinessId())
                .orElseThrow(() -> new ResourceNotFoundException("Business"));

        existingMaterial.setName(materialDTO.getName());
        existingMaterial.setType(materialDTO.getType());
        existingMaterial.setQuantity(materialDTO.getQuantity());
        existingMaterial.setPrice(materialDTO.getPrice());
        existingMaterial.setSupplier(materialDTO.getSupplier());
        existingMaterial.setBusiness(business);

        MATERIAL_REPOSITORY.save(existingMaterial);

        return "Material updated successfully";
    }

    @Override
    public String deleteMaterial(Long id) {
        Material material = MATERIAL_REPOSITORY.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Material"));

        MATERIAL_REPOSITORY.delete(material);
        return "Material deleted successfully with ID: " + id;
    }


}
