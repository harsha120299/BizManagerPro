package lk.ijse.backend.service;

import lk.ijse.backend.dto.MaterialDTO;
import org.springframework.data.domain.Page;

import java.util.List;

public interface MaterialService {
    String saveMaterial(MaterialDTO materialDTO);

    List<MaterialDTO> getAllMaterials(Long id);

    Page<MaterialDTO> getAllMaterialByPages(Long id, int page, int size, String search);

    String updateMaterial(MaterialDTO materialDTO);

    String deleteMaterial(Long id);
}

