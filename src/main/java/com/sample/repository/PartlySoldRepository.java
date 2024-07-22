package com.sample.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.sample.model.PartlySold;

@Repository
public interface PartlySoldRepository extends JpaRepository<PartlySold, Long> {

}
