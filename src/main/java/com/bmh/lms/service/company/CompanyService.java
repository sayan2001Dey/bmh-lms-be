package com.bmh.lms.service.company;

import com.bmh.lms.model.Company;

import java.util.List;
import java.util.Optional;

public interface CompanyService {
    Company createCompanyMaster(Company company, String username);

    List<Company> getAllCompanyMasters();

    Optional<Company> getCompanyMasterById(String company_id);

    Company updateCompanyMaster(String company_id, Company updatedCompany, String username);

    Boolean deleteCompanyMaster(String company_id, String username);
}
