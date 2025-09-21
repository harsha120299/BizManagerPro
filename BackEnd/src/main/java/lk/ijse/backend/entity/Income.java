package lk.ijse.backend.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "incomes")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Income {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long incomeId;

    private Double totalAmount;
    private Double paidAmount;
    private Double remainingAmount;

    private LocalDate date;
    private String description;

    private String paymentMethod;     // Cash, Cheque, Bank Transfer
    private String chequeNumber;
    private String bankName;
    private LocalDate lastPaymentDate;
    private boolean recurring;
    private String referenceNumber;

    private String source;            // Sales, Investment, Service
    private String customerName;
    private String category;          // Product Sales, Service Fees
    private boolean received;

    @ManyToOne
    @JoinColumn(name = "business_id")
    private Business business;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", unique = true)
    private Order order;
}
