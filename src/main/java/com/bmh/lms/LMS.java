package com.bmh.lms;

import org.springframework.boot.SpringApplication;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;

@EntityScan
@SpringBootApplication
public class LMS {

	public static void main(String[] args) {
		SpringApplication.run(LMS.class, args);
	}

}
