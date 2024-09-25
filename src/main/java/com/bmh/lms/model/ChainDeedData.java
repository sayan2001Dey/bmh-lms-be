package com.bmh.lms.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChainDeedData {
    private String deedId;

    private String deedType;

    private Integer order;
}