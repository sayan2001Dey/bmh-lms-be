package com.bmh.lms.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DeedMouza {
    private String mouzaId;
    private List<DeedLandSpecifics> landSpecifics;
}
