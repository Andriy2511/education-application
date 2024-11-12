package com.example.educationplatform.controller;

import com.example.educationplatform.DTO.TimeSlotDTO;
import com.example.educationplatform.component.ListPaginationData;
import com.example.educationplatform.model.*;
import com.example.educationplatform.service.ILessonService;
import com.example.educationplatform.service.IReviewService;
import com.example.educationplatform.service.ITimeSlotService;
import com.example.educationplatform.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/student")
public class StudentController {

    private final IUserService userService;
    private final ITimeSlotService timeSlotService;
    private final ILessonService lessonService;
    private final IReviewService reviewService;
    private final ListPaginationData listPaginationData;

    @Autowired
    public StudentController(IUserService userService, ITimeSlotService timeSlotService, ILessonService lessonService, IReviewService reviewService, ListPaginationData listPaginationData) {
        this.userService = userService;
        this.timeSlotService = timeSlotService;
        this.lessonService = lessonService;
        this.reviewService = reviewService;
        this.listPaginationData = listPaginationData;
    }

    @PostMapping("/bookLesson/{timeSlotId}")
    public String bookLesson(@PathVariable Long timeSlotId, @AuthenticationPrincipal User student){
        TimeSlot timeSlot = timeSlotService.getTimeSlotById(timeSlotId);
        User tutor = timeSlot.getTutor();

        List<TimeSlot> studentTimeSlots = new ArrayList<>();
        List<Lesson> studentLessons = lessonService.getLessonsByStudent(student);
        for (Lesson lesson : studentLessons){
            studentTimeSlots.add(lesson.getTimeSlot());
        }

        if(TimeSlotDTO.isTimeForTimeSlotIntersectWithAnotherSlots(timeSlot, studentTimeSlots) || timeSlot.isBookedStatus()){
            String message = "У вас уже є запланований урок на даний проміжок часу";
            message = URLEncoder.encode(message, StandardCharsets.UTF_8);
            return "redirect:/catalog/showTutorSchedule/" + tutor.getId() + "?message=" + message;
        }

        Lesson lesson = new Lesson(student, tutor, timeSlot);
        lessonService.addNewLesson(lesson);
        timeSlot.setBookedStatus(true);
        timeSlotService.addTimeSlot(timeSlot);

        String message = "Урок заплановано успішно";
        message = URLEncoder.encode(message, StandardCharsets.UTF_8);
        return "redirect:/catalog/showTutorSchedule/" + tutor.getId() + "?message=" + message;
    }

    @GetMapping("/showStudentLessons")
    public String openStudentLessonsPage(@AuthenticationPrincipal User student, Model model){
        List<Lesson> studentLessons = lessonService.getLessonsByStudent(student);
        List<Lesson> futureLessons = new ArrayList<>();
        for (Lesson lesson : studentLessons){
            if(TimeSlotDTO.convertDateToLocalDateTime(lesson.getTimeSlot().getStartTime()).isAfter(LocalDateTime.now())) {
                futureLessons.add(lesson);
            }
        }
        listPaginationData.setTotalRecords((long) futureLessons.size());

        List<Lesson> lessonsWithPageable = new ArrayList<>();
        for (int i = listPaginationData.getPage(); i<listPaginationData.getPage() + listPaginationData.getPageSize(); i++) {
            try {
                lessonsWithPageable.add(futureLessons.get(i));
            } catch (IndexOutOfBoundsException e){
                break;
            }
        }

        model.addAttribute("studentLessons", lessonsWithPageable);
        return "student/student-lessons";
    }

    @PostMapping("/writeReview")
    public String writeReview(@ModelAttribute("review") @Valid Review review, BindingResult bindingResult, @RequestParam Long tutorId, @AuthenticationPrincipal User student, Model model) {
        review.setStudent(student);
        User tutor = userService.getUserById(tutorId);
        review.setTutor(tutor);
        if(bindingResult.hasErrors()){
            Double averageRating = reviewService.getAverageRatingByTutor(tutor);
            model.addAttribute("tutor", userService.getUserById(tutorId));
            model.addAttribute("review", review);
            model.addAttribute("averageRating", averageRating);
            String rateErrorMessage = getErrorMessageForField(bindingResult, "rate");
            String commentErrorMessage = getErrorMessageForField(bindingResult, "comment");
            rateErrorMessage = URLEncoder.encode(rateErrorMessage, StandardCharsets.UTF_8);
            commentErrorMessage = URLEncoder.encode(commentErrorMessage, StandardCharsets.UTF_8);

            return "redirect:/catalog/showTutorInfo/" + tutorId + "?rateErrorMessage=" + rateErrorMessage + "&commentErrorMessage=" + commentErrorMessage;
        }
        reviewService.addReview(review);
        return "redirect:/catalog/showTutorInfo/" + tutorId;
    }

    private String getErrorMessageForField(BindingResult bindingResult, String fieldName) {
        String errorMessage = "";
        for (FieldError error : bindingResult.getFieldErrors()) {
            if (error.getField().equals(fieldName)) {
                errorMessage = error.getDefaultMessage();
                break;
            }
        }
        return errorMessage;
    }
}
