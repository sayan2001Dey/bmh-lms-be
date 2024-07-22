package com.sample.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.sample.model.Mortgaged;

@Repository
public interface MortgagedRepository extends JpaRepository<Mortgaged, Long> {

}
