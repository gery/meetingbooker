package com.chatlord.meeting.form;

import com.chatlord.meeting.model.Meeting;
import lombok.Data;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import javax.validation.constraints.*;

@Data
public class MeetingForm {
    @NotEmpty(message = "Invalid Name: Name is empty")
    String name;

    @NotEmpty(message = "Invalid Date: Date is empty")
    String date;

    @NotEmpty(message = "Invalid TimeFrom: timeFrom is empty")
    @Pattern(regexp = ".*(00|30)$", message = "Invalid TimeFrom, 00 or 30 minutes ending required")
    @Pattern(regexp = "^(09|10|11|12|13|14|15|16|17).*", message = "TimeFrom hour should be between 09 and 17")
    String timeFrom;

    @NotEmpty(message = "Invalid TimeTo: timeTo is empty")
    @Pattern(regexp = ".*(00|30)$", message = "Invalid TimeTo, 00 or 30 minutes ending required")
    @Pattern(regexp = "^(09|10|11|12|13|14|15|16|17).*", message = "TimeTo hour should be between 09 and 17")
    String timeTo;

    static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    public Meeting toEntity() {
        LocalDateTime fromDate = LocalDateTime.parse(date + " " + timeFrom, formatter);                                                                                        
        LocalDateTime toDate = LocalDateTime.parse(date + " " + timeTo, formatter);                                                                                    
        return Meeting.builder()
                .name(name)
                .timeFrom(fromDate)
                .timeTo(toDate)
                .build();
    }
}
