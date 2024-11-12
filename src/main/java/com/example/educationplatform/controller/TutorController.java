package com.example.educationplatform.controller;

import com.example.educationplatform.DTO.EnglishLevelWrapper;
import com.example.educationplatform.DTO.TimeSlotDTO;
import com.example.educationplatform.component.ListPaginationData;
import com.example.educationplatform.model.EnglishLevel;
import com.example.educationplatform.model.TimeSlot;
import com.example.educationplatform.model.User;
import com.example.educationplatform.service.IEnglishLevelService;
import com.example.educationplatform.service.ITimeSlotService;
import com.example.educationplatform.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Controller
@RequestMapping("/tutor")
public class TutorController {

    private final ITimeSlotService timeSlotService;
    private final IEnglishLevelService englishLevelService;
    private final IUserService userService;
    private final ListPaginationData listPaginationData;

    @Autowired
    public TutorController(ITimeSlotService timeSlotService, IEnglishLevelService englishLevelService, IUserService userService, ListPaginationData listPaginationData) {
        this.timeSlotService = timeSlotService;
        this.englishLevelService = englishLevelService;
        this.userService = userService;
        this.listPaginationData = listPaginationData;
    }

    @GetMapping("/openAddTimeSlotPage")
    public String openAddTimeSlotPage(Model model){
        TimeSlotDTO timeSlotDTO = new TimeSlotDTO();
        LocalDate tomorrow = LocalDate.now().plusDays(1);
        model.addAttribute("timeSlotDTO", timeSlotDTO);
        model.addAttribute("tomorrow", tomorrow);
        return "tutor/add-time-slot";
    }

    @PostMapping("/addTimeSlot")
    public String addTimeSlot(@ModelAttribute("timeSlotDTO") @Valid TimeSlotDTO timeSlotDTO, BindingResult bindingResult, @AuthenticationPrincipal User tutor) {
        if(bindingResult.hasErrors()){
            return "tutor/add-time-slot";
        }
        TimeSlot timeSlot1 = TimeSlotDTO.mapToTimeSlot(timeSlotDTO);

        if(timeSlotDTO.isBookForMonth()){
            LocalDateTime startDateTime = TimeSlotDTO.convertToLocalDateTimeViaInstant(timeSlot1.getStartTime());
            LocalDateTime endDateTime = TimeSlotDTO.convertToLocalDateTimeViaInstant(timeSlot1.getEndTime());
            ZoneId zoneId = ZoneId.systemDefault();

            for(int i = 0; i <= 30; i++) {
                TimeSlot tempTimeSlot = TimeSlotDTO.mapToTimeSlot(timeSlotDTO);
                tempTimeSlot.setTutor(tutor);

                LocalDateTime adjustedStartDateTime = startDateTime.plusDays(i);
                LocalDateTime adjustedEndDateTime = endDateTime.plusDays(i);

                Date startDate = Date.from(adjustedStartDateTime.atZone(zoneId).toInstant());
                Date endDate = Date.from(adjustedEndDateTime.atZone(zoneId).toInstant());

                tempTimeSlot.setStartTime(startDate);
                tempTimeSlot.setEndTime(endDate);

                if (!TimeSlotDTO.isTimeForTimeSlotIntersectWithAnotherSlots(tempTimeSlot, timeSlotService.getTimeSlotsByTutor(tutor))) {
                    timeSlotService.addTimeSlot(tempTimeSlot);
                }
            }
        } else {
            TimeSlot timeSlot = TimeSlotDTO.mapToTimeSlot(timeSlotDTO);
            timeSlot.setTutor(tutor);

            if (!TimeSlotDTO.isTimeForTimeSlotIntersectWithAnotherSlots(timeSlot, timeSlotService.getTimeSlotsByTutor(tutor))) {
                timeSlotService.addTimeSlot(timeSlot);
            } else {
                String message = "У вас уже є запланований урок на даний проміжок часу";
                message = URLEncoder.encode(message, StandardCharsets.UTF_8);
                return "redirect:/tutor/openAddTimeSlotPage?message=" + message;
            }
        }

        return "redirect:/tutor/openAddTimeSlotPage";
    }


    @GetMapping("/showTutorTimeSlot")
    public String openTutorTimeSlotPage(Model model, @AuthenticationPrincipal User tutor){
        LocalDate today = LocalDate.now();
        listPaginationData.setTotalRecords(timeSlotService.getTimeSlotsCountByTutor(tutor, TimeSlotDTO.localDateToDate(today)));
        List<TimeSlot> timeSlots = timeSlotService.getTimeSlotsByTutorAndStartTimeAfterDate(tutor, TimeSlotDTO.localDateToDate(today), listPaginationData.getPage(), listPaginationData.getPageSize());
        model.addAttribute("timeSlots", timeSlots);
        return "tutor/time-slot-list";
    }

    @PostMapping("/deleteTimeSlot/{id}")
    public String cancelOffer(@PathVariable Long id, @AuthenticationPrincipal User tutor) {
        TimeSlot timeSlot = timeSlotService.getTimeSlotById(id);
        if(timeSlot!=null && timeSlot.getTutor().equals(tutor)){
            if(!timeSlot.isBookedStatus()) {
                timeSlotService.deleteTimeSlot(timeSlot);
            } else {
                String message = "Ви не можете скасувати цей урок, оскільки він уже заброньований";
                message = URLEncoder.encode(message, StandardCharsets.UTF_8);
                return "redirect:/tutor/showTutorTimeSlot?message=" + message;
            }
        }

        return "redirect:/tutor/showTutorTimeSlot";
    }

    @GetMapping("/showTutorPreferenceLevel")
    public String getAllEnglishLevelsPage(Model model, @AuthenticationPrincipal User user){
        List<EnglishLevel> englishLevels = englishLevelService.getAllEnglishLevels();
        List<EnglishLevel> userEnglishLevels = englishLevelService.findEnglishLevelsByUser(user);
        model.addAttribute("englishLevelWrapper", new EnglishLevelWrapper());
        model.addAttribute("englishLevels", englishLevels);
        model.addAttribute("userEnglishLevels", userEnglishLevels);
        return "tutor/english-preference-level-page";
    }

    @PostMapping("/submitEnglishLevels")
    public String submitEnglishLevels(@RequestParam(name = "selectedLevels", required = false) String[] selectedLevels, @AuthenticationPrincipal User user) {
        List<EnglishLevel> allEnglishLevels = englishLevelService.getAllEnglishLevels();
        List<EnglishLevel> userEnglishLevels = new ArrayList<>();
        if(selectedLevels != null) {
            for (String levelName : selectedLevels) {
                EnglishLevel englishLevel = englishLevelService.findByName(levelName);
                userEnglishLevels.add(englishLevel);
            }
        }
        user.setEnglishLevels(userEnglishLevels);

        for (EnglishLevel level : allEnglishLevels){
            if(user.getEnglishLevels().contains(level)){
                List<User> users = new ArrayList<>();
                users.add(user);
                level.setUsers(users);
                englishLevelService.addEnglishLevel(level);
            } else {
                if(level.getUsers().contains(user)){
                    List<User> users = level.getUsers();
                    users.remove(user);
                    level.setUsers(users);
                    englishLevelService.addEnglishLevel(level);
                }
            }
        }

        userService.registerUser(user);

        return "redirect:/tutor/showTutorPreferenceLevel";
    }
}
