package com.chatlord.meeting.controller;

import java.util.List;
import java.util.Collection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import com.chatlord.meeting.model.Meeting;
import com.chatlord.meeting.form.MeetingForm;
import com.chatlord.meeting.service.MeetingService;
import javax.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@RestController
public class MeetingController {

   private static final Logger LOGGER=LoggerFactory.getLogger(MeetingController.class);
   @Autowired
   MeetingService meetingService;

   @GetMapping("/meeting")
   private List<Meeting> getAllMeeting() {
      return meetingService.getAllMeeting();
   }

   @GetMapping("/meeting/{id}")
   private Meeting getMeeting(@PathVariable("id") int id) {
      return meetingService.getMeetingById(id);
   }

   @GetMapping("/meetingByDay/{day}")
   private Collection<Meeting> getMeetingByDay(@PathVariable("day") String day) throws java.text.ParseException {  
      return meetingService.getAllByDay(day);
   }

   @DeleteMapping("/meeting/{id}")
   private void deleteMeeting(@PathVariable("id") int id) {
      meetingService.delete(id);
   }

   @PostMapping("/meeting")
   private ResponseEntity<Integer> saveMeeting(@RequestBody @Valid MeetingForm form) {
      LOGGER.info("Saving meeting form: {}",form);
      Meeting meeting = form.toEntity();
      return new ResponseEntity<Integer>(meetingService.saveOrUpdate(meeting), HttpStatus.CREATED);
   }

}
