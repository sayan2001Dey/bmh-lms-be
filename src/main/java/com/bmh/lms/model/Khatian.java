package com.bmh.lms.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table( name = "khatian")
public class Khatian extends CommonProperties{
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String khatianId;
    private String remarks;
    private String website; // "Bihar" or "Banglar" to distinguish between the two
    private String jila; // Bihar - Jila | Banglar - District
    private String anchal; // Bihar only
    private String block; // Banglar only
    private String halka; // Bihar only
    private String mouza; // Common to both
    private String khatianType; // Banglar only
    private String nameWithKhatian; // common to both
    private String district; // Banglar only
    private String assesseeNo; // KMC only
    private String reKmc; // KMC only
}
