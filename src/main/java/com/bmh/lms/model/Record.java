package com.bmh.lms.model;


import java.util.List;

import jakarta.persistence.*;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "record")
public class Record extends CommonProperties {

    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column
    private  String recId;

    @Column(length = 100)
    private String groupName;

    @Column(length = 50)
    private String state;

    @Column(length = 50)
    private String city;

    @Column(length = 50)
    private String mouza;

    @Column(length = 50)
    private String block;

    @Column(name = "jl_no", length = 20)
    private String JLno;

    @Column(length = 7)
    private Integer pincode;

    @Column(length = 100)
    private String buyerOwner;
    
    @Column(length = 1000)
    @ElementCollection
    private List<String> sellers;
    
    @Column(length = 50)
    private String deedName;
    
    @Column(length = 50)
    private String deedNo;
    
    @Column(length = 50)
    private String deedDate;
 
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
    private String conversionLandStus;
    
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
    
    @Column
    private String historyChain;
    
    @Column
    private Boolean mortgaged;
    
    @Column
    private Boolean partlySold;
}

