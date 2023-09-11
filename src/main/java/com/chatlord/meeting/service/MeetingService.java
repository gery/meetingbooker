package com.chatlord.meeting.service;

import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.chatlord.meeting.exception.ValidationException;
import com.chatlord.meeting.model.Meeting;
import com.chatlord.meeting.repository.MeetingRepository;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Service
public class MeetingService {
      
   private static final Logger LOGGER=LoggerFactory.getLogger(MeetingService.class);
   static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

   @Autowired
   MeetingRepository meetingRepository;

   // getting all meeting records
   public List<Meeting> getAllMeeting() {
      List<Meeting> meetings = new ArrayList<Meeting>();
      meetingRepository.findAll().forEach(meeting -> meetings.add(meeting));
      return meetings;
   }

   public Meeting getMeetingById(int id) {
      return meetingRepository.findById(id).get();
   }

   public  Collection<Meeting> getAllByDay(String day) throws java.text.ParseException{
         LOGGER.info("getAllByDay day:{}",day);
         LocalDateTime dateFrom = LocalDate.parse(day, formatter).atStartOfDay();; //dateFormat.parse(day); 
         LocalDateTime dateTo = dateFrom.plusDays(1);  
         return meetingRepository.findAllBetweenDates( dateFrom,  dateTo);
   }

   public int saveOrUpdate(Meeting meeting) throws ValidationException{
      LOGGER.info("saveOrUpdate meeting:{}",meeting);
      validate(meeting);
      meetingRepository.save(meeting);
      return 1;
   }

   private void validate(Meeting meeting) throws ValidationException{
      long minutes = ChronoUnit.MINUTES.between(meeting.getTimeFrom(), meeting.getTimeTo()); 
      if (minutes>180){
         throw new ValidationException("The interval is more than 3 hours");
      }

      if (minutes<=0){
         throw new ValidationException("Time from should be earlier than time To");
      }

      if("SUNDAY".equals(meeting.getTimeFrom().getDayOfWeek().toString()) || "SATURDAY".equals(meeting.getTimeFrom().getDayOfWeek().toString())) {
         throw new ValidationException("Weekend is not available for booking");
      }

      Collection<Meeting>  meetings = meetingRepository.findCollision(meeting.getTimeFrom(), meeting.getTimeTo());
      LOGGER.info("Meeting collisions:{}",meetings.size());
      if (meetings.size()>0 ){
         throw new ValidationException("Collision with other meetings");
      }
   }

   public void delete(int id) {
      meetingRepository.deleteById(id);
   }
}