package lk.ijse.backend.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Table(name = "loans")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Loan {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long loanId;

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

    private Double interestRate;
    private LocalDate dueDate;
    private String lender;
    private String status;
    private String loanType;
    private Integer installments;
    private Double installmentAmount;
    private Double totalPaid;

    @ManyToOne
    @JoinColumn(name = "business_id")
    private Business business;
}

