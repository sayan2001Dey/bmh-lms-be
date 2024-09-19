package com.bmh.lms.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DeedLandSpecifics {
    private String oldRsDag;
    private String newLrDag;
    private String landType;
    private Double maxQty;
    private Double qty;
}
