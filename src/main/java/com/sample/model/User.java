package com.sample.model;

import lombok.Data;

import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Data
@Table(name = "user")
@NoArgsConstructor
public class User extends CommonProperties{
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Column
    private String name;

    @Column(unique = true, nullable = false)
    private String username;

    @Column
    private Boolean admin;

    @Column
    private String password;
} 
