package com.bmh.lms.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChainDeedData {
    private String deedId;

    private String deedType;

    private Integer order;

    private List<String> parentDeedIds;
}