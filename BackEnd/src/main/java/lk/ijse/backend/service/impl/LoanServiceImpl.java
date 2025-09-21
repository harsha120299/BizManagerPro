package lk.ijse.backend.service.impl;

import lk.ijse.backend.dto.LoanDTO;
import lk.ijse.backend.entity.Business;
import lk.ijse.backend.entity.Loan;
import lk.ijse.backend.repository.BusinessRepository;
import lk.ijse.backend.repository.LoanRepository;
import lk.ijse.backend.service.LoanService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class LoanServiceImpl implements LoanService {

    private final LoanRepository loanRepository;
    private final BusinessRepository businessRepository;

    public LoanServiceImpl(LoanRepository loanRepository,
                           BusinessRepository businessRepository) {
        this.loanRepository = loanRepository;
        this.businessRepository = businessRepository;
    }

    @Override
    public String saveLoan(LoanDTO loanDTO) {
        Loan loan = mapToEntity(loanDTO);

        if (loanDTO.getBusinessId() != null) {
            Business business = businessRepository.findById(loanDTO.getBusinessId())
                    .orElseThrow(() -> new RuntimeException("Business not found"));
            loan.setBusiness(business);
        }

        Loan saved = loanRepository.save(loan);
        return "Loan saved successfully";
    }

    @Override
    public String updateLoan(Long loanId, LoanDTO loanDTO) {
        Loan existing = loanRepository.findById(loanId)
                .orElseThrow(() -> new RuntimeException("Loan not found"));

        existing.setTotalAmount(loanDTO.getTotalAmount());
        existing.setPaidAmount(loanDTO.getPaidAmount());
        existing.setRemainingAmount(loanDTO.getRemainingAmount());
        existing.setDate(loanDTO.getDate());
        existing.setDescription(loanDTO.getDescription());
        existing.setPaymentMethod(loanDTO.getPaymentMethod());
        existing.setChequeNumber(loanDTO.getChequeNumber());
        existing.setBankName(loanDTO.getBankName());
        existing.setLastPaymentDate(loanDTO.getLastPaymentDate());
        existing.setRecurring(loanDTO.isRecurring());
        existing.setReferenceNumber(loanDTO.getReferenceNumber());
        existing.setInterestRate(loanDTO.getInterestRate());
        existing.setDueDate(loanDTO.getDueDate());
        existing.setLender(loanDTO.getLender());
        existing.setStatus(loanDTO.getStatus());
        existing.setLoanType(loanDTO.getLoanType());
        existing.setInstallments(loanDTO.getInstallments());
        existing.setInstallmentAmount(loanDTO.getInstallmentAmount());
        existing.setTotalPaid(loanDTO.getTotalPaid());

        Loan updated = loanRepository.save(existing);

        return "Loan updated successfully";
    }

    @Override
    public LoanDTO getLoanById(Long loanId) {
        return loanRepository.findById(loanId)
                .map(this::mapToDTO)
                .orElseThrow(() -> new RuntimeException("Loan not found"));
    }

    @Override
    public List<LoanDTO> getAllLoans() {
        return loanRepository.findAll().stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<LoanDTO> getLoansByBusiness(Long businessId) {
        return loanRepository.findByBusiness_BusinessId(businessId).stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public String deleteLoan(Long loanId) {
        if (!loanRepository.existsById(loanId)) {
            throw new RuntimeException("Loan not found");
        }
        loanRepository.deleteById(loanId);
        return "Loan deleted successfully";
    }

    private LoanDTO mapToDTO(Loan loan) {
        return LoanDTO.builder()
                .loanId(loan.getLoanId())
                .totalAmount(loan.getTotalAmount())
                .paidAmount(loan.getPaidAmount())
                .remainingAmount(loan.getRemainingAmount())
                .date(loan.getDate())
                .description(loan.getDescription())
                .paymentMethod(loan.getPaymentMethod())
                .chequeNumber(loan.getChequeNumber())
                .bankName(loan.getBankName())
                .lastPaymentDate(loan.getLastPaymentDate())
                .recurring(loan.isRecurring())
                .referenceNumber(loan.getReferenceNumber())
                .interestRate(loan.getInterestRate())
                .dueDate(loan.getDueDate())
                .lender(loan.getLender())
                .status(loan.getStatus())
                .loanType(loan.getLoanType())
                .installments(loan.getInstallments())
                .installmentAmount(loan.getInstallmentAmount())
                .totalPaid(loan.getTotalPaid())
                .businessId(loan.getBusiness() != null ? loan.getBusiness().getBusinessId() : null)
                .build();
    }

    private Loan mapToEntity(LoanDTO dto) {
        return Loan.builder()
                .totalAmount(dto.getTotalAmount())
                .paidAmount(dto.getPaidAmount())
                .remainingAmount(dto.getRemainingAmount())
                .date(dto.getDate())
                .description(dto.getDescription())
                .paymentMethod(dto.getPaymentMethod())
                .chequeNumber(dto.getChequeNumber())
                .bankName(dto.getBankName())
                .lastPaymentDate(dto.getLastPaymentDate())
                .recurring(dto.isRecurring())
                .referenceNumber(dto.getReferenceNumber())
                .interestRate(dto.getInterestRate())
                .dueDate(dto.getDueDate())
                .lender(dto.getLender())
                .status(dto.getStatus())
                .loanType(dto.getLoanType())
                .installments(dto.getInstallments())
                .installmentAmount(dto.getInstallmentAmount())
                .totalPaid(dto.getTotalPaid())
                .build();
    }
}
