package com.example.educationplatform.controller;

import com.example.educationplatform.DTO.QuestionDTO;
import com.example.educationplatform.component.ListPaginationData;
import com.example.educationplatform.constants.RoleConstants;
import com.example.educationplatform.model.*;
import com.example.educationplatform.service.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/admin")
@Slf4j
public class AdminController {
    private final IUserService userService;
    private final IRoleService roleService;
    private final ITestService testService;
    private final IOptionService optionService;
    private final ListPaginationData listPaginationData;
    private final IQuestionService questionService;

    @Autowired
    public AdminController(IUserService userService, IRoleService roleService, ListPaginationData listPaginationData, PasswordEncoder passwordEncoder, ITestService testService, IOptionService optionService, IQuestionService questionService) {
        this.userService = userService;
        this.roleService = roleService;
        this.listPaginationData = listPaginationData;
        this.testService = testService;
        this.optionService = optionService;
        this.questionService = questionService;
    }

    @GetMapping("/showStudentsList")
    public String showStudentsList(Model model){
        Role role = roleService.getRoleByName(RoleConstants.STUDENT_ROLE);
        List<User> users = userService.findAllUsersByRole(role, listPaginationData.getPage(), listPaginationData.getPageSize());
        model.addAttribute("users", users);
        return "/admin/student-list";
    }


    @GetMapping("/showTutorsList")
    public String showTutorsList(Model model){
        Role role = roleService.getRoleByName(RoleConstants.TUTOR_ROLE);
        List<User> users = userService.findAllUsersByRole(role, listPaginationData.getPage(), listPaginationData.getPageSize());
        model.addAttribute("users", users);
        return "/admin/tutor-list";
    }

    @GetMapping("/showAddNewTestForm")
    public String showAddNewTestForm(Model model){
        Test test = new Test();
        model.addAttribute("test", test);
        return "admin/add-test-page";
    }

    @PostMapping("/addTest")
    public String addTest(@ModelAttribute("test") Test test, @AuthenticationPrincipal User user){
        if (test.getName() == null || test.getName().isBlank()){
            String message = "Назва тесту не може бути пустою";
            message = URLEncoder.encode(message, StandardCharsets.UTF_8);
            return "redirect:/admin/showAddNewTestForm?message=" + message;
        }

        if(user.getRole().getName().equals(RoleConstants.ADMIN_ROLE)){
            test.setCompletedStatus(false);
            testService.addTest(test);
        }
        return "redirect:/admin/showUncompletedTests";
    }

    @GetMapping("/showUncompletedTests")
    public String showUncompletedTests(Model model){
        List<Test> tests = testService.findAllTestByCompleted(false, listPaginationData.getPage(), listPaginationData.getPageSize());
        model.addAttribute("tests", tests);
        return "/admin/uncompleted-tests-list";
    }

    @GetMapping("/showCompletedTests")
    public String showCompletedTests(Model model){
        List<Test> tests = testService.findAllTestByCompleted(true, listPaginationData.getPage(), listPaginationData.getPageSize());
        model.addAttribute("tests", tests);
        return "/admin/completed-tests-list";
    }

    @GetMapping("/showTestInfo/{testId}")
    public String showTestInfo(@PathVariable Long testId, Model model){
        Test test = testService.getTestById(testId);
        List<Question> questions = questionService.getQuestionByTest(test);
        model.addAttribute("questions", questions);

        return "/admin/test-info-page";
    }

    @GetMapping("/showAddQuestionForm/{testId}")
    public String showAddQuestionForm(@PathVariable Long testId, Model model){
        QuestionDTO questionDTO = new QuestionDTO();
        questionDTO.setTestId(testId);
        model.addAttribute("questionDTO", questionDTO);
        return "/admin/add-question-form";
    }

    @PostMapping("/addQuestion")
    public String addQuestion(@ModelAttribute("questionDTO") @Valid QuestionDTO questionDTO, BindingResult bindingResult, Model model){
        if(bindingResult.hasErrors()){
            String message = "Заповніть усі поля";
            message = URLEncoder.encode(message, StandardCharsets.UTF_8);
            return "redirect:/admin/showAddQuestionForm/" + questionDTO.getTestId() + "?message=" + message;
        }
        Test test = testService.getTestById(questionDTO.getTestId());
        if(!test.isCompletedStatus()){
            for (String optionText : questionDTO.getOptions()){
                if (optionText == null || optionText.isBlank()){
                    String message = "Заповніть усі поля";
                    message = URLEncoder.encode(message, StandardCharsets.UTF_8);
                    return "redirect:/admin/showAddQuestionForm/" + test.getId() + "?message=" + message;
                }
            }

            List<Option> optionList = new ArrayList<>();
            for (String optionText : questionDTO.getOptions()){
                Option option = new Option();
                option.setText(optionText);
                optionList.add(option);
                optionService.addOption(option);
            }

            Question question = new Question();
            question.setText(questionDTO.getText());
            question.setOptions(optionList);
            question.setTest(test);

            Question savedQuestion = questionService.addQuestion(question);
            Option rightAnswer = optionList.get(questionDTO.getCorrectOptionIndex()-1);
            savedQuestion.setCorrectOption(rightAnswer);
            questionService.addQuestion(savedQuestion);

            String message = "Запитання додано успішно";
            message = URLEncoder.encode(message, StandardCharsets.UTF_8);
            model.addAttribute("questionDTO", questionDTO);
            return "redirect:/admin/showAddQuestionForm/" + test.getId() + "?message=" + message;
        }

        model.addAttribute("questionDTO", questionDTO);
        return "redirect:/admin/showAddQuestionForm/" + test.getId();
    }

    @PostMapping("/deleteTest/{testId}")
    public String deleteTest(@PathVariable Long testId, @AuthenticationPrincipal User user){
        Test test = testService.getTestById(testId);
        if(!test.isCompletedStatus()) {
            if(test.getQuestions() == null) {
                testService.deleteTest(test);
            } else {
                List<Question> questions = questionService.getQuestionByTest(test);
                for (Question question : questions){
                    question.setCorrectOption(null);
                    questionService.addQuestion(question);
                    for (Option option : optionService.getOptionsByQuestion(question)){
                        optionService.deleteOption(option);
                    }
                    questionService.deleteQuestion(question);
                }
                test = testService.getTestById(testId);
                testService.deleteTest(test);
            }
        }else {
            String message = "Ви не можете видалити цей тест";
            message = URLEncoder.encode(message, StandardCharsets.UTF_8);
            return "redirect:/admin/showUncompletedTests?message=" + message;
        }

        return "redirect:/admin/showUncompletedTests";
    }

    @PostMapping("/setStatusCompleted/{testId}")
    public String setStatusCompletedFotTest(@PathVariable Long testId, @AuthenticationPrincipal User user){
        Test test = testService.getTestById(testId);
        List<Question> questions = questionService.getQuestionByTest(test);
        if(!test.isCompletedStatus() && questions != null && questions.size() > 0) {
            test.setCompletedStatus(true);
            testService.addTest(test);
        }else {
            String message = "Додайте принаймні одне питання у тест";
            message = URLEncoder.encode(message, StandardCharsets.UTF_8);
            return "redirect:/admin/showUncompletedTests?message=" + message;
        }

        return "redirect:/admin/showUncompletedTests";
    }
}
