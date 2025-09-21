package lk.ijse.backend.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "customer_product_price",
        uniqueConstraints = {@UniqueConstraint(columnNames = {"customer_id", "product_id"})})
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CustomerProductPrice {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id", nullable = false)
    private Customer customer;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @Column(nullable = false)
    private Double price;
}
