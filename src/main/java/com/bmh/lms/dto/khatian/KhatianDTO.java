package com.bmh.lms.dto.khatian;

import com.bmh.lms.model.Khatian;
import com.bmh.lms.model.KhatianNo;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class KhatianDTO extends Khatian {
    private List<KhatianNo> khatianNos;
}
