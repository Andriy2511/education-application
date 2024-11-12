package com.example.educationplatform.service;

import com.example.educationplatform.model.TimeSlot;
import com.example.educationplatform.model.User;

import java.util.Date;
import java.util.List;

public interface ITimeSlotService {
    List<TimeSlot> getTimeSlotsByTutor(User tutor);

    TimeSlot getTimeSlotsById(Long id);

    TimeSlot addTimeSlot(TimeSlot timeSlot);

    List<TimeSlot> getTimeSlotsByTutorAndStartTimeAfterDate(User tutor, Date startTime, int page, int pageSize);

    List<TimeSlot> getTimeSlotsByTutorAndStartTimeAfterDateAndBookedStatus(User tutor, Date startTime, Boolean bookedStatus, int page, int pageSize);

    Long getTimeSlotsCountByTutor(User tutor, Date startTime);

    TimeSlot getTimeSlotById(Long id);

    void deleteTimeSlot(TimeSlot timeSlot);
}
