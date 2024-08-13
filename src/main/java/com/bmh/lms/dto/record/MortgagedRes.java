package com.bmh.lms.dto.record;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MortgagedRes {
    private String mortId;
    private String party;
    private String mortDate;
    private Double mortQty;
    private String mortDocFile;
}
