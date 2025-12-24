package com.school.student.dto;

import jakarta.validation.constraints.NotNull;

public class AssignClassRequest {

    @NotNull
    public Long classId;

    @NotNull
    public Long sectionId;

    @NotNull
    public String academicYear;
}
