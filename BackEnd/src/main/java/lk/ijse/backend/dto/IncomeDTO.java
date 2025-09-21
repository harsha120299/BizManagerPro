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
public class IncomeDTO {

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

    private Long businessId;          // Reference to Business
    private Long orderId;             // Reference to Order
}