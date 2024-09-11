package com.bmh.lms.dto.deed;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MortgagedDeedRes {
    private String mortId;
    private String party;
    private String mortDate;
    private Double mortQty;
    private String mortDocFile;
}