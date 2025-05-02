package com.bmh.lms.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table
public class Uom extends CommonProperties{
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column
    private String uomId;

    @Column
    private String uomName;

    @Column
    private String classification;

    @Column
    private Double multiplier;

    @Column(nullable = false, columnDefinition = "tinyint default false")
    private Boolean baseUnit;
}
