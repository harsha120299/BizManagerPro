package lk.ijse.backend.service;

import lk.ijse.backend.dto.LoanDTO;

import java.util.List;

public interface LoanService {

    String saveLoan(LoanDTO loanDTO);

    String updateLoan(Long loanId, LoanDTO loanDTO);

    LoanDTO getLoanById(Long loanId);

    List<LoanDTO> getAllLoans();

    List<LoanDTO> getLoansByBusiness(Long businessId);

    String deleteLoan(Long loanId);
}
