package com.sample.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@NoArgsConstructor
@Entity
@Table(name = "scanCopyFile")
public class ScanCopyFile extends CommonProperties{
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column
    private String fileName;

    @Column(name="rec_id")
    private String recId;
}
