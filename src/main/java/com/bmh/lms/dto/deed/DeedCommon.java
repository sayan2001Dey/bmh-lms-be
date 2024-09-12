package com.bmh.lms.dto.deed;

import com.bmh.lms.model.PartlySold;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DeedCommon {

    private String deedId;
    private String deedNo;
    private String deedDate;
    private String totalQty;
    private String purQty;
    private String mutedQty;
    private String unMutedQty;
    private String landType;
    private String landStatus;
    private String conversionLandStatus;
    private String deedLoc;
    private String photoLoc;
    private String govtRec;
    private String remarks;
    private String khazanaStatus;
    private Float tax;
    private String dueDate;
    private String legalMatters;
    private String ledueDate;
    private String lelastDate;
    private Boolean mortgaged;
    private Boolean partlySold;
    private Set<PartlySold> partlySoldData;
}
