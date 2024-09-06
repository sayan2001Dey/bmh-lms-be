package com.bmh.lms.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table( name ="mouza")
public class Mouza extends CommonProperties {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column
    private String mouzaId;

    @Column
    private String block;

    @Column
    private String JLno;

    @Column(length = 50)
    private String oldRsDag;

    @Column(length = 50)
    private String newLrDag;

    @Column(length = 50)
    private String oldKhatian;

    @Column(length = 50)
    private String newKhatian;

    @Column(length = 50)
    private String currKhatian;

}