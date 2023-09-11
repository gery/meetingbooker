package com.chatlord.meeting;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class MeetingApplication {

	private static final Logger LOGGER=LoggerFactory.getLogger(MeetingApplication.class);

	public static void main(String[] args) {
		LOGGER.info("Starting MeetingApplication..\n");
		SpringApplication.run(MeetingApplication.class, args);
	}

}
