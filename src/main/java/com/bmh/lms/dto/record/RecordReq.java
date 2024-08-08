package com.bmh.lms.dto.record;

import com.bmh.lms.model.Mortgaged;
import lombok.*;

import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RecordReq extends RecordCommon {
    private Set<Mortgaged> mortgagedData;
}
