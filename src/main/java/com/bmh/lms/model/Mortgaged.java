package com.bmh.lms.model;

import jakarta.persistence.*;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name="mortgaged")
public class Mortgaged extends CommonProperties{
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	@Column(name="mort_id")
	private String mortId;

	@Column(name="rec_id")
	private String recId;
	
	@Column(name="mort_party")
	private String party;

	@Column
	private Double mortQty;
	
	@Column(name="mort_Date")
	private String mortDate;
}
