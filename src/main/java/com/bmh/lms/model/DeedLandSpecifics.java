package com.bmh.lms.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
public class DeedLandSpecifics extends MouzaLandSpecifics {
    private Double qty;
    private Double mutedQty;
    private Double unMutedQty;
    private String newKhNoBefore;
    private String oldKhNoBefore;
}
