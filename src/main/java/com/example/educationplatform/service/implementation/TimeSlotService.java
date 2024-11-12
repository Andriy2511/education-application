package com.example.educationplatform.service.implementation;

import com.example.educationplatform.model.TimeSlot;
import com.example.educationplatform.model.User;
import com.example.educationplatform.repository.TimeSlotRepository;
import com.example.educationplatform.service.ITimeSlotService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class TimeSlotService implements ITimeSlotService {

    private final TimeSlotRepository timeSlotRepository;

    @Autowired
    public TimeSlotService(TimeSlotRepository timeSlotRepository) {
        this.timeSlotRepository = timeSlotRepository;
    }

    @Override
    public List<TimeSlot> getTimeSlotsByTutor(User tutor){
        return timeSlotRepository.getTimeSlotsByTutor(tutor);
    }

    @Override
    public TimeSlot getTimeSlotsById(Long id){
        return timeSlotRepository.getTimeSlotById(id);
    }

    @Override
    public TimeSlot addTimeSlot(TimeSlot timeSlot){
        return timeSlotRepository.save(timeSlot);
    }

    @Override
    public List<TimeSlot> getTimeSlotsByTutorAndStartTimeAfterDate(User tutor, Date startTime, int page, int pageSize){
        Pageable pageable = PageRequest.of(page, pageSize);
        return timeSlotRepository.getTimeSlotsByTutorAndStartTimeAfter(tutor, startTime, pageable);
    }

    @Override
    public List<TimeSlot> getTimeSlotsByTutorAndStartTimeAfterDateAndBookedStatus(User tutor, Date startTime, Boolean bookedStatus, int page, int pageSize){
        Pageable pageable = PageRequest.of(page, pageSize);
        return timeSlotRepository.getTimeSlotsByTutorAndStartTimeAfterAndBookedStatus(tutor, startTime, bookedStatus, pageable);
    }

    @Override
    public Long getTimeSlotsCountByTutor(User tutor, Date startTime) {
        return timeSlotRepository.countTimeSlotByTutorAndStartTimeAfter(tutor, startTime);
    }

    @Override
    public TimeSlot getTimeSlotById(Long id) {
        return timeSlotRepository.findById(id).get();
    }

    @Override
    public void deleteTimeSlot(TimeSlot timeSlot) {
        timeSlotRepository.delete(timeSlot);
    }
}
