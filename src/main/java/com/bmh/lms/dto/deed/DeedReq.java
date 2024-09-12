package com.bmh.lms.dto.deed;

import com.bmh.lms.model.Mortgaged;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DeedReq extends DeedCommon{
    private Set<Mortgaged> mortgagedData;
}
