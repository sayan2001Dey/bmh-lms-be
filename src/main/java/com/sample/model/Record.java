package com.sample.model;


import java.util.List;
import java.util.Set;

import javax.persistence.*;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Entity
@Table(name = "record")
public class Record extends CommonProperties {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name="rec_id")
    private  String recId;

    @Column(name = "group_name", length = 100)
    private String groupName;

    @Column(name = "state", length = 50)
    private String state;

    @Column(name = "city", length = 50)
    private String city;

    @Column(name = "mouza", length = 50)
    private String mouza;

    @Column(name = "block", length = 50)
    private String block;

    @Column(name = "jl_no", length = 20)
    private String JLno;

    @Column(name = "buyer_owner", length = 100)
    private String buyerOwner;
    
    @Column(name = "sellers", length = 1000)
    @ElementCollection
    private List<String> sellers;
    
    @Column(name = "deed_name", length = 50)
    private String deedName;
    
    @Column(name = "deed_no", length = 50)
    private String deedNo;
    
    @Column(name = "deed_date", length = 50)
    private String deedDate;
    
    @Column(name = "scan_copy", length = 255) 
    @ElementCollection
    private List<String> scanCopyFile;
 
    @Column(name = "old_rs_dag", length = 50)
    private String oldRsDag;

    @Column(name = "new_lr_dag", length = 50)
    private String newLrDag;

    @Column(name = "old_khatian", length = 50)
    private String oldKhatian;

    @Column(name = "new_khatian", length = 50)
    private String newKhatian;

    @Column(name = "curr_khatian", length = 50)
    private String currKhatian;


    @Column(name = "total_qty", length = 20)
    private String totalQty;

    @Column(name = "pur_qty", length = 20)
    private String purQty;

    @Column(name = "muted_qty", length = 20)
    private String mutedQty;

    @Column(name = "unmuted_qty", length = 20)
    private String unMutedQty;

    @Column(name = "mutation_file", length = 255)
    @ElementCollection
    private List<String> mutationFile;

    @Column(name = "land_status", length = 50)
    private String landStatus;
    
    @Column(name = "converted_unconverted")
    private String conversionLandStus;
    
    @Column(name = "conversion_file", length = 255)
    @ElementCollection
    private List<String> conversionFile;
    
    
    @Column(name = "deed_loc", length = 255)
    private String deedLoc;
    
    @Column(name = "photo_loc", length = 255)
    private String photoLoc;
    
    @Column(name = "govt_rec", length = 255)
    private String govtRec;
    
    @Column(name = "khazana_status", length = 50)
    private String khazanaStatus;
    
    @Column(name = "due_date", length = 50)
    private String dueDate;
    
    @Column(name = "document_file", length = 255)
    @ElementCollection
    private List<String> documentFile;
    
    
    @Column(name = "area_map_file", length = 255)
    @ElementCollection
    private List<String> areaMapFile;
    
    @Column(name = "legal_matters", length = 255)
    private String legalMatters;
    
    @Column(name = "le_due_date", length = 50)
    private String ledueDate;
    
    @Column(name = "history_chain", length = 255)
    private String historyChain;
    
    @Column(name = "hc_document_file", length = 255)
    @ElementCollection
    private List<String> hcdocumentFile;
    
    @Column(name = "mortgaged", length = 50)
    private String mortgaged;
    
    @Column(name = "partly_sold", length = 50)
    private String partlySold;

    @OneToMany(fetch = FetchType.LAZY,mappedBy="record",cascade = CascadeType.ALL)
    private Set<Mortgaged> mortgagedData;

    @OneToMany(fetch = FetchType.LAZY,mappedBy="record",cascade = CascadeType.ALL)
    private Set<PartlySold> partlySoldData;
    
//    @OneToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "user_id", nullable = false)
//    private User user;
}

