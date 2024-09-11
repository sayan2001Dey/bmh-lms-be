package com.bmh.lms.model;

import lombok.*;

import jakarta.persistence.*;

@Entity
@Data
@Table(name = "user")
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class User extends CommonProperties{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column
    private String name;

    @Column(nullable = false)
    private String username;

    @Column
    private Boolean admin;

    @Column
    private String password;
} 
