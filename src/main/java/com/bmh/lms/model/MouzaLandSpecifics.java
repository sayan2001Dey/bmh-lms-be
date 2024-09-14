package com.bmh.lms.model;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Data;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MouzaLandSpecifics {
    private String oldRsDag;
    private String newLrDag;
    private Integer qty;
}
