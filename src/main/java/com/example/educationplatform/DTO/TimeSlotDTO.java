package com.example.educationplatform.DTO;

import com.example.educationplatform.model.TimeSlot;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.util.Date;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class TimeSlotDTO {
    @NotNull(message = "Дата не може бути порожньою")
    @Future(message = "Дата уроку повинна бути принаймні на наступний день")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate date;

    @Pattern(regexp = "^([8-9]|1[0-9]|20):(00|30)$", message = "Час початку має бути у форматі HH:mm та знаходитися в межах від 8 до 20 години")
    private String startTime;

    @NotNull(message = "Тривалість не може бути порожньою")
    @Min(value = 60, message = "Мінімальна тривалість 60 хвилин")
    @Max(value = 180, message = "Максимальна тривалість 180 хвилин")
    @DecimalMin(value = "60", message = "Тривалість має бути кратною 30 хвилинам")
    @DecimalMax(value = "180", message = "Тривалість має бути кратною 30 хвилинам")
    private Integer duration;

    @NotNull(message = "Ціна не може бути порожньою")
    @Min(value = 0, message = "Мінімальна ціна 0")
    private Long price;

    private boolean bookForMonth;

    public static TimeSlot mapToTimeSlot(TimeSlotDTO timeSlotDTO){
        TimeSlot timeSlot = new TimeSlot();
        LocalDateTime localDateTime = convertDateToLocalDateTime(localDateToDate(timeSlotDTO.getDate()));
        DateTimeFormatter formatter = new DateTimeFormatterBuilder()
                .appendPattern("[H:mm][HH:mm]")
                .toFormatter();
        LocalTime startTime = LocalTime.parse(timeSlotDTO.getStartTime(), formatter);
        LocalDateTime lessonStartTime = localDateTime.with(startTime);
        LocalDateTime lessonEndTime = lessonStartTime.plusMinutes(timeSlotDTO.getDuration());
        timeSlot.setStartTime(Date.from(lessonStartTime.atZone(ZoneId.systemDefault()).toInstant()));
        timeSlot.setEndTime(Date.from(lessonEndTime.atZone(ZoneId.systemDefault()).toInstant()));

        timeSlot.setPrice(timeSlotDTO.getPrice());

        return timeSlot;
    }

    public static LocalDateTime convertDateToLocalDateTime(Date dateToConvert) {
        return dateToConvert.toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDateTime();
    }

    public static LocalDate convertToLocalDateViaInstant(Date dateToConvert) {
        return dateToConvert.toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDate();
    }

    public static LocalDateTime convertToLocalDateTimeViaInstant(Date dateToConvert) {
        return dateToConvert.toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDateTime();
    }

    public static Date localDateToDate(LocalDate localDate){
        return Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
    }

    public static boolean isTimeForTimeSlotIntersectWithAnotherSlots(TimeSlot insertedTimeSlot, List<TimeSlot> timeSlots){
        LocalDateTime insertedStartTime = TimeSlotDTO.convertDateToLocalDateTime(insertedTimeSlot.getStartTime());
        LocalDateTime insertedEndTime = TimeSlotDTO.convertDateToLocalDateTime(insertedTimeSlot.getEndTime());

        for (TimeSlot timeSlot : timeSlots){
            LocalDateTime startTime = TimeSlotDTO.convertDateToLocalDateTime(timeSlot.getStartTime());
            LocalDateTime endTime = TimeSlotDTO.convertDateToLocalDateTime(timeSlot.getEndTime());
            if (insertedStartTime.isBefore(endTime) && insertedEndTime.isAfter(startTime)) {
                return true;
            }
        }

        return false;
    }
}
