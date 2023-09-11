package com.chatlord.meeting;

import com.chatlord.meeting.repository.MeetingRepository;
import com.chatlord.meeting.model.Meeting;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import java.util.Collection;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class MeetingApplicationTests {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private MeetingRepository meetingRepository;

	@Test
	void contextLoads() {
		assertThat(meetingRepository).isNotNull();
	}

	@Test
	void testSave() throws Exception {

		meetingRepository.deleteAll();

		Collection<Meeting> meetings = meetingRepository.findAll();
		assertThat(meetings).isEmpty();

		this.mockMvc.perform(
				post("/meeting")
						.contentType("application/json")
						.content(
								"{\"name\": \"Joe\", \"date\": \"2023-12-12\", \"timeFrom\": \"11:30\", \"timeTo\": \"12:30\"}"))
				.andExpect(status().isCreated());

		meetings = meetingRepository.findAll();
		assertThat(meetings.size()).isEqualTo(1);
		assertThat(meetings.stream().findFirst().get().getName()).isEqualTo("Joe");

	}

	@Test
	void testValidationCollision() throws Exception {
		meetingRepository.deleteAll();

		this.mockMvc.perform(
				post("/meeting")
						.contentType("application/json")
						.content(
								"{\"name\": \"Joe\", \"date\": \"2023-09-12\", \"timeFrom\": \"10:00\", \"timeTo\": \"10:30\"}"))
				.andExpect(status().isCreated());

		this.mockMvc.perform(
				post("/meeting")
						.contentType("application/json")
						.content(
								"{\"name\": \"John\", \"date\": \"2023-09-12\", \"timeFrom\": \"09:00\", \"timeTo\": \"11:00\"}"))
				.andExpect(status().isBadRequest())
				.andExpect(content().string("{\"errors\":[\"Collision with other meetings\"]}"));

		Collection<Meeting> meetings = meetingRepository.findAll();
		assertThat(meetings.size()).isEqualTo(1);
		assertThat(meetings.stream().findFirst().get().getName()).isEqualTo("Joe");
	}

	@Test
	void testValidationBigInterval() throws Exception {
		meetingRepository.deleteAll();

		this.mockMvc.perform(
				post("/meeting")
						.contentType("application/json")
						.content(
								"{\"name\": \"Joe\", \"date\": \"2023-09-12\", \"timeFrom\": \"10:00\", \"timeTo\": \"17:00\"}"))
				.andExpect(status().isBadRequest())
				.andExpect(content().string("{\"errors\":[\"The interval is more than 3 hours\"]}"));

		Collection<Meeting> meetings = meetingRepository.findAll();
		assertThat(meetings.size()).isEqualTo(0);
	}

	@Test
	void testValidationWeekend() throws Exception {
		meetingRepository.deleteAll();

		this.mockMvc.perform(
				post("/meeting")
						.contentType("application/json")
						.content(
								"{\"name\": \"Joe\", \"date\": \"2023-09-16\", \"timeFrom\": \"11:00\", \"timeTo\": \"12:00\"}"))
				.andExpect(status().isBadRequest())
				.andExpect(content().string("{\"errors\":[\"Weekend is not available for booking\"]}"));

		Collection<Meeting> meetings = meetingRepository.findAll();
		assertThat(meetings.size()).isEqualTo(0);
	}



	@Test
	void testValidationMorning() throws Exception {
		meetingRepository.deleteAll();

		this.mockMvc.perform(
				post("/meeting")
						.contentType("application/json")
						.content(
								"{\"name\": \"Joe\", \"date\": \"2023-09-16\", \"timeFrom\": \"08:00\", \"timeTo\": \"10:00\"}"))
				.andExpect(status().isBadRequest())
				.andExpect(content().string("{\"errors\":[\"TimeFrom hour should be between 09 and 17\"]}"));

		Collection<Meeting> meetings = meetingRepository.findAll();
		assertThat(meetings.size()).isEqualTo(0);
	}


	@Test
	void test4BookingsOnOneDay() throws Exception {
		meetingRepository.deleteAll();

		Collection<Meeting> meetings = meetingRepository.findAll();
		assertThat(meetings).isEmpty();

		this.mockMvc.perform(
				post("/meeting")
						.contentType("application/json")
						.content(
								"{\"name\": \"Person1\", \"date\": \"2023-12-12\", \"timeFrom\": \"09:00\", \"timeTo\": \"10:00\"}"))
				.andExpect(status().isCreated());

		this.mockMvc.perform(
				post("/meeting")
						.contentType("application/json")
						.content(
								"{\"name\": \"Person2\", \"date\": \"2023-12-12\", \"timeFrom\": \"10:00\", \"timeTo\": \"11:30\"}"))
				.andExpect(status().isCreated());

		this.mockMvc.perform(
				post("/meeting")
						.contentType("application/json")
						.content(
								"{\"name\": \"Person3\", \"date\": \"2023-12-12\", \"timeFrom\": \"11:30\", \"timeTo\": \"12:30\"}"))
				.andExpect(status().isCreated());

		this.mockMvc.perform(
				post("/meeting")
						.contentType("application/json")
						.content(
								"{\"name\": \"Person4\", \"date\": \"2023-12-12\", \"timeFrom\": \"13:00\", \"timeTo\": \"15:00\"}"))
				.andExpect(status().isCreated());

		meetings = meetingRepository.findAll();
		assertThat(meetings.size()).isEqualTo(4);
	}

}
