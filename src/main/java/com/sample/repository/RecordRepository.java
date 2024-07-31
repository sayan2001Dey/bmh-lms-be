package com.sample.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import com.sample.model.Record;
@Repository
public interface RecordRepository extends JpaRepository<Record, Long> {
//	 List<Record> findByUser(User user);

    @Query(value="SELECT * FROM record WHERE modified_type='INSERTED'", nativeQuery = true)
    List<Record> findAllActive();

    @Query(value="SELECT * FROM record WHERE rec_id=? AND modified_type='INSERTED'", nativeQuery = true)
    Optional<Record> findByRecId(String id);

//	@Query(value = "DELETE FROM :tableName WHERE :columnName = :fileName; SELECT * FROM record;", nativeQuery = true)
//	List<Record> deleteDocs(String tableName, String columnName, String fileName);
}
