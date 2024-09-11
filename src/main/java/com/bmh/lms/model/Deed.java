package com.bmh.lms.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name="deed_master")
public class Deed extends CommonProperties {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String deedId;

    @Column(length = 50)
    private String deedNo;

    @Column(length = 50)
    private String deedDate;

    @Column(length = 20)
    private String totalQty;

    @Column(length = 20)
    private String purQty;

    @Column(length = 20)
    private String mutedQty;

    @Column(name = "unmuted_qty", length = 20)
    private String unMutedQty;

    @Column(length = 50)
    private String landType;

    @Column(length = 50)
    private String landStatus;

    @Column(name = "converted_unconverted")
    private String conversionLandStatus;

    @Column
    private String deedLoc;

    @Column
    private String photoLoc;

    @Column
    private String govtRec;

    @Column
    private String remarks;

    @Column(length = 50)
    private String khazanaStatus;

    @Column
    private Float tax;

    @Column(length = 50)
    private String dueDate;

    @Column
    private String legalMatters;

    @Column(name = "le_due_date", length = 50)
    private String ledueDate;

    @Column(name = "le_last_date", length = 50)
    private String lelastDate;

//    @Column
//    private Boolean mortgaged;
//
//    @Column
//    private Boolean partlySold;
}
