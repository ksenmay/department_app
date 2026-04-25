package by.may.department.model;

import lombok.*;

@Builder
@Getter
@Setter
public class Discipline {

    private int id;
    private String name;
    private int lectureHours;
    private int practicalHours;
    private int labHours;
    private int totalHours;
    private boolean exam;
    private boolean test;

    public int getTotalHours() {
        return lectureHours + practicalHours + labHours;
    }
}
