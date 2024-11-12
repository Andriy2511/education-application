package com.example.educationplatform.repository;

import com.example.educationplatform.model.TimeSlot;
import com.example.educationplatform.model.User;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface TimeSlotRepository  extends JpaRepository<TimeSlot, Long> {
    List<TimeSlot> getTimeSlotsByTutor(User tutor);
    List<TimeSlot> getTimeSlotsByTutorAndStartTimeAfter(User tutor, Date startTime, Pageable pageable);
    List<TimeSlot> getTimeSlotsByTutorAndStartTimeAfterAndBookedStatus(User tutor, Date startTime, Boolean bookedStatus, Pageable pageable);
    Long countTimeSlotByTutorAndStartTimeAfter(User tutor, Date startTime);
    TimeSlot getTimeSlotById(Long id);

}
