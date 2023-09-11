package com.chatlord.meeting.model;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import com.fasterxml.jackson.annotation.JsonFormat;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Meeting 
{
    private static final String LOCAL_TIMEZONE="Europe/Budapest";
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
    private String name;
    @JsonFormat(pattern="yyyy-MM-dd HH:mm", timezone = LOCAL_TIMEZONE)    
    private LocalDateTime timeFrom;
    @JsonFormat(pattern="yyyy-MM-dd HH:mm", timezone = LOCAL_TIMEZONE)
    private LocalDateTime timeTo;    
}