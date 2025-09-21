package lk.ijse.backend.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "materials")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Material {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long materialId;

    private String name;
    private Double quantity;
    private String type;
    private Double price;
    private String supplier;

    @ManyToOne
    @JoinColumn(name = "business_id")
    @JsonIgnore // Prevent circular serialization
    @ToString.Exclude
    private Business business;

    @OneToMany(mappedBy = "material", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore // Prevent circular serialization with ProductMaterial
    @ToString.Exclude
    private List<ProductMaterial> productMaterials;
}
