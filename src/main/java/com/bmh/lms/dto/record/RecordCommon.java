package com.bmh.lms.dto.record;

import com.bmh.lms.model.ChainDeedData;
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
public class RecordCommon {
    private String recId;
    private String deedType;
    private List<ChainDeedData> chainDeedData;
    private String deedId;
    private String remarks;
    private String historyChain;
}
