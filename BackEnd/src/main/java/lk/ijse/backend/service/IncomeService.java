package lk.ijse.backend.service;

import lk.ijse.backend.dto.IncomeDTO;

import java.util.List;

public interface IncomeService {

    String saveIncome(IncomeDTO incomeDTO);
    IncomeDTO updateIncome(Long incomeId, IncomeDTO incomeDTO);
    IncomeDTO getIncomeById(Long incomeId);
    List<IncomeDTO> getIncomesByBusiness(Long businessId);
    String deleteIncome(Long incomeId);
}
