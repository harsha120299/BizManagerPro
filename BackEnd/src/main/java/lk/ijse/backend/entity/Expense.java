package lk.ijse.backend.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Table(name = "expenses")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Expense {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long expenseId;

    private Double totalAmount;
    private Double paidAmount;
    private Double remainingAmount;
    private LocalDate date;
    private String description;

    private String paymentMethod;
    private String chequeNumber;
    private String bankName;
    private LocalDate lastPaymentDate;
    private boolean recurring;
    private String referenceNumber;

    private String category;
    private String paidTo;
    private boolean paid;
    private LocalDate dueDate;

    @ManyToOne
    @JoinColumn(name = "business_id")
    private Business business;
}
