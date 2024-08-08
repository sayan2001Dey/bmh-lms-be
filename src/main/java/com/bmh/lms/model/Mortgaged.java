package com.bmh.lms.model;

import javax.persistence.*;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
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
	
	@Column(name="mort_Date")
	private String mortDate;
}
