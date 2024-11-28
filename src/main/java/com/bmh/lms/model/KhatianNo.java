package com.bmh.lms.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table
public class KhatianNo {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String khatianNoId;

    private String newKhatianNo;
    private String oldKhatianNo;

    @Column(columnDefinition="tinyint(1) default true")
    private Boolean current;

    private Long KhatianId;
}
