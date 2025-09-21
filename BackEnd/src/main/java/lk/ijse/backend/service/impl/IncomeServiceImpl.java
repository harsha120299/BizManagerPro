package lk.ijse.backend.service.impl;

import lk.ijse.backend.dto.IncomeDTO;
import lk.ijse.backend.service.IncomeService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class IncomeServiceImpl implements IncomeService {
    @Override
    public String saveIncome(IncomeDTO incomeDTO) {
        return "";
    }

    @Override
    public IncomeDTO updateIncome(Long incomeId, IncomeDTO incomeDTO) {
        return null;
    }

    @Override
    public IncomeDTO getIncomeById(Long incomeId) {
        return null;
    }

    @Override
    public List<IncomeDTO> getIncomesByBusiness(Long businessId) {
        return List.of();
    }

    @Override
    public String deleteIncome(Long incomeId) {
        return "";
    }
}
