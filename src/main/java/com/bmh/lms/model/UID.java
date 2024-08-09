package com.bmh.lms.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.persistence.*;

@Data
@NoArgsConstructor
@Entity
@Table(name = "uid")
public class UID {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "model_name")
    private String modelName;

    @Column(name = "last_id")
    private int lastId;
}

