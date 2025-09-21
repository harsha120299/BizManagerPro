package lk.ijse.backend.controller;

import lk.ijse.backend.dto.ApiResponse;
import lk.ijse.backend.dto.MaterialDTO;
import lk.ijse.backend.service.MaterialService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/material")
@RequiredArgsConstructor
public class MaterialController {

    private final MaterialService MATERIAL_SERVICE;

    @PostMapping("/add")
    public ResponseEntity<ApiResponse> addMaterial(@RequestBody MaterialDTO materialDTO) {
        System.out.println("add material"+materialDTO);
            return ResponseEntity.ok(new ApiResponse(
                    200,
                    "OK",
                    MATERIAL_SERVICE.saveMaterial(materialDTO)));
    }

    @GetMapping("/getAll/{id}")
    public ResponseEntity<ApiResponse> getAllMaterialsPaginated(
            @PathVariable Long id,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String search
    ) {
        Page<MaterialDTO> materialsPage = MATERIAL_SERVICE.getAllMaterialByPages(id, page, size, search);
        return ResponseEntity.ok(new ApiResponse(200, "OK", materialsPage));
    }

    @GetMapping("/getAllList/{id}")
    public ResponseEntity<ApiResponse> getAllMaterialsList(@PathVariable Long id) {
        System.out.println("getAllMaterialsList"+id);
        List<MaterialDTO> materials = MATERIAL_SERVICE.getAllMaterials(id);
        System.out.println("getAllMaterialsList"+materials);
        return ResponseEntity.ok(new ApiResponse(200, "OK", materials));
    }


    @PutMapping("/update")
    public ResponseEntity<ApiResponse> updateMaterial(@RequestBody MaterialDTO materialDTO) {
        System.out.println("updateMaterial");
        System.out.println("update material"+materialDTO);
        return ResponseEntity.ok(new ApiResponse(
                200,
                "OK",
                MATERIAL_SERVICE.updateMaterial(materialDTO)
        ));
    }
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<ApiResponse> deleteMaterial(@PathVariable Long id) {
        return ResponseEntity.ok(new ApiResponse(
                200,
                "OK",
                MATERIAL_SERVICE.deleteMaterial(id)
        ));
    }
}
