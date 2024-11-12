package com.example.educationplatform.controller;

import com.example.educationplatform.DTO.AnswerDTO;
import com.example.educationplatform.DTO.ExamAnswerDTO;
import com.example.educationplatform.DTO.ExamAnswersWrapper;
import com.example.educationplatform.component.ListPaginationData;
import com.example.educationplatform.model.*;
import com.example.educationplatform.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Controller
@RequestMapping("/test")
public class TestController {
    private final IUserService userService;
    private final IRoleService roleService;
    private final ITestService testService;
    private final IQuestionService questionService;
    private final ListPaginationData listPaginationData;
    private final IExamAnswerService examAnswerService;
    private final IExamService examService;

    @Autowired
    public TestController(IUserService userService, IRoleService roleService, ITestService testService, IQuestionService questionService, ListPaginationData listPaginationData, IExamAnswerService examAnswerService, IExamService examService) {
        this.userService = userService;
        this.roleService = roleService;
        this.testService = testService;
        this.questionService = questionService;
        this.listPaginationData = listPaginationData;
        this.examAnswerService = examAnswerService;
        this.examService = examService;
    }


    @GetMapping("/showAvailableTestsPage")
    public String showAvailableTests(@AuthenticationPrincipal User user, Model model){
        List<Test> tests = testService.findAllTestByCompleted(true, listPaginationData.getPage(), listPaginationData.getPageSize());
        model.addAttribute("tests", tests);
        return "/test/available-tests";
    }

    @GetMapping("/openTestPage/{testId}")
    public String openTestPage(@PathVariable Long testId, @AuthenticationPrincipal User user, Model model){
        Test test = testService.getTestById(testId);
        List<Question> questions = questionService.getQuestionByTest(test);

        model.addAttribute("testId", testId);
        model.addAttribute("questions", questions);
        model.addAttribute("examAnswersWrapper", new ExamAnswersWrapper());

        return "/test/test-page";
    }

    @GetMapping("/openTestInfo/{testId}")
    public String openTestInfo(@PathVariable Long testId, Model model){
        Test test = testService.getTestById(testId);
        model.addAttribute("test", test);

        return "/test/test-result-info";
    }

    @PostMapping("/submitTestAnswers")
    public String submitTestAnswers(@ModelAttribute("examAnswers") ExamAnswersWrapper examAnswersWrapper, @ModelAttribute("testId") Long testId,
                                    @AuthenticationPrincipal User user){

        List<ExamAnswerDTO> examAnswersDTO = examAnswersWrapper.getExamAnswers();
        List<ExamAnswer> examAnswers = new ArrayList<>();
        Test test = testService.getTestById(testId);
        Exam exam = createExamForUser(user);
        exam.setTest(test);
        exam = examService.addExam(exam);

        for (ExamAnswerDTO examAnswerDTO : examAnswersDTO){
            try {
                ExamAnswer examAnswer = new ExamAnswer();
                Question question = questionService.findQuestionById(examAnswerDTO.getQuestionId());

                examAnswer.setQuestion(question);
                try {
                    examAnswer.setOption(question.getOptions().get(examAnswerDTO.getAnswerIndex() - 1));
                } catch (IndexOutOfBoundsException | NullPointerException e){
                    examAnswer.setOption(null);
                }
                examAnswer.setExam(exam);
                examAnswer = examAnswerService.addAnswer(examAnswer);

                examAnswers.add(examAnswer);
                if(examAnswer.getOption() != null && question.getCorrectOption().equals(examAnswer.getOption())){
                    exam.setResult(exam.getResult()+1);
                    exam.setMaxResult(exam.getMaxResult()+1);
                } else {
                    exam.setMaxResult(exam.getMaxResult()+1);
                }
            } catch (NullPointerException e){
                exam.setMaxResult(exam.getMaxResult()+1);
            }
        }

        exam.setExamAnswer(examAnswers);
        examService.addExam(exam);

        return "redirect:/test/showAvailableTestsPage";
    }

    private Exam createExamForUser(User user){
        Exam exam = new Exam();
        LocalDateTime currentDateTime = LocalDateTime.now();
        Date examDate = java.sql.Timestamp.valueOf(currentDateTime);
        exam.setDate(examDate);
        exam.setStudent(user);
        exam.setResult(0);
        exam.setMaxResult(0);
        return exam;
    }

    @GetMapping("/showFinishedExamPage")
    public String showFinishedExamPage(@AuthenticationPrincipal User user, Model model){
        List<Exam> exams =examService.getExamsByUser(user, listPaginationData.getPage(), listPaginationData.getPageSize());
        model.addAttribute("exams", exams);
        return "/test/finished-exams-page";
    }

    @GetMapping("/showTestResultInfoPage/{examId}")
    public String showExamInfoPage(@PathVariable Long examId, @AuthenticationPrincipal User user, Model model){
        Exam exam = examService.getExamById(examId);
        if(exam != null && exam.getStudent().equals(user)){
            List<ExamAnswer> examAnswers = exam.getExamAnswer();
            List<AnswerDTO> answerDTOList = new ArrayList<>();
            for(ExamAnswer examAnswer : examAnswers) {
                AnswerDTO answerDTO = new AnswerDTO();
                answerDTO.setTextQuestion(examAnswer.getQuestion().getText());
                if (examAnswer.getOption() != null){
                    answerDTO.setChosenAnswer(examAnswer.getOption().getText());
                } else {
                    answerDTO.setChosenAnswer("Без відповіді");
                }
                answerDTO.setCorrectAnswer(examAnswer.getQuestion().getCorrectOption().getText());
                answerDTO.setOptions(examAnswer.getQuestion().getOptions());

                answerDTOList.add(answerDTO);
            }
            model.addAttribute("answerDTOList", answerDTOList);
        } else {
            return "redirect:/test/showFinishedExamPage";
        }

        return "/test/exam-result-info";
    }
}
