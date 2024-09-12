package com.bmh.lms.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@Entity
@Table(name = "fileUploads")
public class FileUpload {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String fieldName;

    @Column
    private String fileName;

    @Column
    private String deedId;

    @Column
    private String insideId;

    @Column
    private String modified_type;

    @Column
    private LocalDateTime inserted_on;

    @Column
    private String inserted_by;

    @Column
    private LocalDateTime deleted_on;

    @Column
    private String deleted_by;

}
