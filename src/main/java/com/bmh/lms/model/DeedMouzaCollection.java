package com.bmh.lms.model;

import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Data
@Builder
@Document(value = "deed")
@NoArgsConstructor
@AllArgsConstructor
public class DeedMouzaCollection {
    @Id
    private String id;

    private List<DeedMouza> mouza;
}
