package com.sample.dto.record;

import com.sample.model.PartlySold;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RecordCommon {
    private String recId;
    private String groupName;
    private String state;
    private String city;
    private String mouza;
    private String block;
    private String JLno;
    private String buyerOwner;
    private List<String> sellers;
    private String deedName;
    private String deedNo;
    private String deedDate;
    private String oldRsDag;
    private String newLrDag;
    private String oldKhatian;
    private String newKhatian;
    private String currKhatian;
    private String totalQty;
    private String purQty;
    private String mutedQty;
    private String unMutedQty;
    private String landStatus;
    private String conversionLandStus;
    private String deedLoc;
    private String photoLoc;
    private String govtRec;
    private String khazanaStatus;
    private String dueDate;
    private String legalMatters;
    private String ledueDate;
    private String historyChain;
    private String mortgaged;
    private String partlySold;
    private Set<PartlySold> partlySoldData;
}
