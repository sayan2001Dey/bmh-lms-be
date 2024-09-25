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
@Document(value = "record")
@NoArgsConstructor
@AllArgsConstructor
public class ChainDeedDataCollection {
    @Id
    private String id;

    private List<ChainDeedData> chainDeedData;
}
