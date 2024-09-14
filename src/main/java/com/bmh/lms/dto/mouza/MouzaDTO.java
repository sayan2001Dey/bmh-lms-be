package com.bmh.lms.dto.mouza;

import com.bmh.lms.model.MouzaLandSpecifics;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MouzaDTO {
    private String mouzaId;
    private String groupId;
    private String mouza;
    private String block;
    private Long jlno;
    private List<MouzaLandSpecifics> landSpecifics;
}
