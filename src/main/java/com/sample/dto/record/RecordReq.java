package com.sample.dto.record;

import com.sample.model.Mortgaged;
import lombok.*;

import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RecordReq extends RecordCommon {
    private Set<Mortgaged> mortgagedData;
}
