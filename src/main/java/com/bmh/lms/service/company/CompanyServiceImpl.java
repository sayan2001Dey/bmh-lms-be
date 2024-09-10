package com.bmh.lms.service.company;

import com.bmh.lms.model.Company;
import com.bmh.lms.repository.CompanyRepository;
import com.bmh.lms.service.utils.CommonUtils;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class CompanyServiceImpl implements CompanyService {

    @Autowired
    private CompanyRepository repository;

    @Autowired
    private CommonUtils commonUtils;

    @Override
    public Company createCompanyMaster(Company company, String username) {
        LocalDateTime ldt = LocalDateTime.now();

        company.setCompanyId(commonUtils.generateUID("CompanyMaster", "CM"));
        company.setModified_type("INSERTED");
        company.setInserted_by(username);
        company.setInserted_on(ldt);
        company.setUpdated_by("NA");
        company.setUpdated_on(null);
        company.setDeleted_by("NA");
        company.setDeleted_on(null);

        return repository.save(company);
    }

    @Override
    public List<Company> getAllCompanyMasters() {
        return repository.findAllActiveByCompanyId();
    }

    @Override
    public Optional<Company> getCompanyMasterById(String company_id) {

        return repository.findByCompanyId(company_id);
    }

    @Override
    @Transactional
    public Company updateCompanyMaster(String company_id, Company updatedCompany, String username) {
        LocalDateTime ldt = LocalDateTime.now();

        Company oldCompany = repository.findByCompanyId(company_id).orElse(null);

        if(oldCompany == null) return null;

        updatedCompany.setCompanyId(company_id);
        updatedCompany.setModified_type("INSERTED");
        updatedCompany.setInserted_by(oldCompany.getInserted_by());
        updatedCompany.setInserted_on(oldCompany.getInserted_on());
        updatedCompany.setUpdated_by(username);
        updatedCompany.setUpdated_on(ldt);
        updatedCompany.setDeleted_by("NA");
        updatedCompany.setDeleted_on(null);

        oldCompany.setModified_type("UPDATED");
        oldCompany.setUpdated_by(username);
        oldCompany.setUpdated_on(ldt);

        repository.save(oldCompany);

        return repository.save(updatedCompany);
    }

    @Override
    public Boolean deleteCompanyMaster(String company_id, String username) {
        LocalDateTime ldt = LocalDateTime.now();
        Company company = repository.findByCompanyId(company_id).orElse(null);

        if(company == null) return false;

        company.setModified_type("DELETED");
        company.setDeleted_by(username);
        company.setDeleted_on(ldt);

        repository.save(company);

        return true;
    }
}
