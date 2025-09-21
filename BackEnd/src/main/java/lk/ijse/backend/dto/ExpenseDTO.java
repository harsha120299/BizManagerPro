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
public class ExpenseDTO {

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

    private Long businessId;  // Reference to Business
}
