package com.bmh.lms.repository;

import com.bmh.lms.model.Company;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CompanyRepository extends JpaRepository<Company, Long> {
    @Query(value="SELECT * FROM company WHERE modified_type='INSERTED'", nativeQuery = true)
    List<Company> findAllActiveByCompanyId();

    @Query(value="SELECT * FROM company WHERE company_id=? AND modified_type='INSERTED'", nativeQuery = true)
    Optional<Company> findByCompanyId(String id);
}
