package com.bmh.lms.model;

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

//    KEEP THIS FOR FUTURE USE
//    @Column
//    private String deedType;
//
//    @Column
//    private String deedId;

    @Column
    private String chainDeedRefId;

    @Column
    private String remarks;
    
    @Column
    private String historyChain;
}

