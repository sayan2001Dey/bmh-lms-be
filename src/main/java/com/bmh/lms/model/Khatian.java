package com.bmh.lms.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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

    private String website;

    private String name;

    private String oldKhatian;

    private String newKhatian;

}
