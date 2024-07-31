package com.sample.repository;

import com.sample.model.FileUpload;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface FileUploadRepository extends JpaRepository<FileUpload, Long> {
    @Query(value = "SELECT * FROM file_uploads  WHERE rec_id=? AND modified_type='INSERTED'", nativeQuery = true)
    List<FileUpload> findFilesById(String id);

    @Query(value = "SELECT * FROM file_uploads  WHERE file_name=? AND modified_type='INSERTED'", nativeQuery = true)
    Optional<FileUpload> findFilesByFileName(String fileName);

    @Query(value = "SELECT * FROM file_uploads  WHERE rec_id=? AND field_name=? AND modified_type='INSERTED'", nativeQuery = true)
    List<FileUpload> findFilesByIdNFieldName(String id, String fieldName);
}
