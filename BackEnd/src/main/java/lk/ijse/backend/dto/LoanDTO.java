package lk.ijse.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LoanDTO {

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

    private Long businessId;  // Reference to Business
}
