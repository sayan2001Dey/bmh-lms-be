package com.sample.model;

import java.util.List;

import javax.persistence.*;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Entity
@Table(name="mortgaged")
public class Mortgaged extends CommonProperties{
	
	@Id
	@Column(name = "id")
    @GeneratedValue(strategy = GenerationType.AUTO)
	private long id;

	@Column(name="mort_id")
	private String mortId;

	@Column(columnDefinition="varchar(50) default 'NA'")
	private String slno;

	@Column(name="rec_id")
	private String recId;
	
	@Column(name="mort_party")
	private String party;
	
	@Column(name="mort_Date")
	private String mortDate;
    
	@Column(name="doc")
	@ElementCollection
	private List<String> docFile;

	@ManyToOne(cascade= CascadeType.ALL)
	@JoinColumn(name = "record_id")
	private Record record;
}
