package com.bmh.lms.dto.deed;

import com.bmh.lms.model.DeedMouza;
import com.bmh.lms.model.PartlySold;
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
public class DeedCommon {

    private String deedId;
    private String deedNo;
    private String deedDate;
    private String groupId;
    private String companyId;
    private String khatianId;
    private String website;
    private String sellerType;
    private List<String> sellers;
    private String totalQty;
    private String purQty;
    private String mutedQty;
    private String unMutedQty;
    private String landStatus;
    private String conversionLandStatus;
    private String deedLoc;
    private String photoLoc;
    private String govtRec;
    private String remarks;
    private String khazanaStatus;
    private Float tax;
    private String lastUpDate;
    private String taxDueDate;
    private String legalMatters;
    private String ledueDate;
    private String lelastDate;
    private String leDescription;
    private Boolean mortgaged;
    private Boolean partlySold;
    private String recId;
    private Set<PartlySold> partlySoldData;
    private List<DeedMouza> mouza;
}
