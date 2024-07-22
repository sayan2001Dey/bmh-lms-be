package com.sample.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import com.sample.model.Record;
@Repository
public interface RecordRepository extends JpaRepository<Record, Long> {
//	 List<Record> findByUser(User user);
	
//	@Query(value = "DELETE FROM :tableName WHERE :columnName = :fileName; SELECT * FROM record;", nativeQuery = true)
//	List<Record> deleteDocs(String tableName, String columnName, String fileName);
}
