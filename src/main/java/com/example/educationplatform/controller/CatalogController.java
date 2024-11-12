package com.example.educationplatform.controller;

import com.example.educationplatform.DTO.TimeSlotDTO;
import com.example.educationplatform.component.CatalogPaginationData;
import com.example.educationplatform.component.ListPaginationData;
import com.example.educationplatform.constants.RoleConstants;
import com.example.educationplatform.model.*;
import com.example.educationplatform.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/catalog")
public class CatalogController {
    private final IUserService userService;
    private final IRoleService roleService;
    private final ITimeSlotService timeSlotService;
    private final IReviewService reviewService;
    private final IEnglishLevelService englishLevelService;
    private final CatalogPaginationData paginationData;
    private final ListPaginationData listPaginationData;

    @Autowired
    public CatalogController(IUserService userService, IRoleService roleService, CatalogPaginationData paginationData, ListPaginationData listPaginationData, ITimeSlotService timeSlotService, IReviewService reviewService, IEnglishLevelService englishLevelService) {
        this.userService = userService;
        this.roleService = roleService;
        this.paginationData = paginationData;
        this.listPaginationData = listPaginationData;
        this.timeSlotService = timeSlotService;
        this.reviewService = reviewService;
        this.englishLevelService = englishLevelService;
    }

    @GetMapping("/showTutorsCatalog")
    public String getComponentsWithPaginationAndSorting(Model model,
                                                        @RequestParam(name = "selectedLevels", required = false) String[] selectedLevels,
                                                        @RequestParam(name = "page",  required = false) Integer page,
                                                        @RequestParam(name = "pageSize", required = false) Integer recordPerPage) {

        List<EnglishLevel> allEnglishLevels = englishLevelService.getAllEnglishLevels();
        configurePaginationAndSorting(selectedLevels, page, recordPerPage);
        Role role = roleService.getRoleByName(RoleConstants.TUTOR_ROLE);
        List<User> tutors = new ArrayList<>();
        if(selectedLevels == null) {
            tutors = userService.
                    findUsersByRoleWithPagination(role, paginationData.getPage(), paginationData.getPageSize());
        } else {
            for (String levelName : selectedLevels) {
                List<EnglishLevel> englishLevels = new ArrayList<>();
                EnglishLevel englishLevel = englishLevelService.findByName(levelName);
                englishLevels.add(englishLevel);
                tutors = userService.findUsersByRoleAndEnglishLevels(role, englishLevels, page, recordPerPage);
            }
        }

        model.addAttribute("englishLevels", allEnglishLevels);
        model.addAttribute("tutors", tutors);
        model.addAttribute("paginationData", paginationData);

        return "catalog/tutor-catalog";
    }

    @GetMapping("/showTutorSchedule/{tutorId}")
    public String showTutorSchedule(@PathVariable Long tutorId, Model model){
        User tutor = userService.getUserById(tutorId);
        LocalDate today = LocalDate.now();
        listPaginationData.setTotalRecords(timeSlotService.getTimeSlotsCountByTutor(tutor, TimeSlotDTO.localDateToDate(today)));
        List<TimeSlot> timeSlots = timeSlotService.getTimeSlotsByTutorAndStartTimeAfterDateAndBookedStatus(tutor, TimeSlotDTO.localDateToDate(today), false, listPaginationData.getPage(), listPaginationData.getPageSize());
        model.addAttribute("timeSlots", timeSlots);
        model.addAttribute("tutor", tutor);
        return "catalog/tutor-schedule";
    }

    @GetMapping("/showTutorInfo/{tutorId}")
    public String shotTutorInfoPage(@PathVariable Long tutorId, Model model){
        User tutor = userService.getUserById(tutorId);
        Double averageRating = reviewService.getAverageRatingByTutor(tutor);
        if (averageRating != null) {
            averageRating = Math.round(averageRating * 10.0) / 10.0;
        }
        model.addAttribute("averageRating", averageRating);
        model.addAttribute("tutor", tutor);
        model.addAttribute("review", new Review());
        return "catalog/tutor-info";
    }

    private void configurePaginationAndSorting(String[] selectedLevels, Integer page, Integer recordPerPage) {
        if (selectedLevels != null) {
            paginationData.setSelectedLevels(selectedLevels);
        }
        if (page != null && page >= 0) {
            paginationData.setPage(page);
        }
        if (recordPerPage != null && recordPerPage > 0) {
            paginationData.setPageSize(recordPerPage);
            paginationData.setTotalPages(countTotalPages(recordPerPage));
        }
    }

    private Integer countTotalPages(Integer recordPerPage){
        Role role = roleService.getRoleByName(RoleConstants.TUTOR_ROLE);
        Long totalRecords = userService.getCountOfUsersByRole(role);
        return (int) Math.ceil((double) totalRecords/recordPerPage);
    }
}
