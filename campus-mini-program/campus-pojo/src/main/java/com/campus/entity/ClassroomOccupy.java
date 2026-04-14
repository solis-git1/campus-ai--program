package com.campus.entity;

import lombok.Data;
import java.time.LocalDate;
import java.time.LocalTime;

@Data
public class ClassroomOccupy {
    private Long occupyId;
    private Long classroomId;
    private LocalDate date;
    private LocalTime startTime;
    private LocalTime endTime;
    private String source;
}
