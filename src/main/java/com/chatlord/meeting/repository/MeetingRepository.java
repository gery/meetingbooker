package com.chatlord.meeting.repository;

import org.springframework.data.repository.CrudRepository;
import com.chatlord.meeting.model.Meeting;
import java.time.LocalDateTime;
import java.util.Collection;
import org.springframework.data.jpa.repository.Query;

public interface MeetingRepository extends CrudRepository<Meeting, Integer>{

  @Query("SELECT m FROM Meeting m WHERE m.timeFrom >= ?1 and m.timeTo < ?2")
  Collection<Meeting> findAllBetweenDates(LocalDateTime timeFrom, LocalDateTime timeTo);

  @Query("SELECT m FROM Meeting m WHERE " +
      "( ?1 = m.timeFrom and m.timeTo = ?2 ) " +
      "or ( ?1 > m.timeFrom and m.timeTo > ?1 ) " +
      "or ( ?2 > m.timeFrom and m.timeTo >= ?2 ) " +
      "or ( ?1 < m.timeFrom and m.timeTo < ?2 ) " + // outer
      "or ( ?1 >= m.timeFrom and m.timeTo >= ?2 ) ") // inner
  Collection<Meeting> findCollision(LocalDateTime timeFrom, LocalDateTime dateFrom);

  @Override
  Collection<Meeting> findAll();

}
