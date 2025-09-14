package com.bt.cricketacademy.batch.model;

import jakarta.persistence.*;
import java.util.Set;

@Entity
public class Batch {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    // For simplicity, just store IDs for students/coaches
    @ElementCollection
    private Set<Long> studentIds;
    @ElementCollection
    private Set<Long> coachIds;
    // getters and setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public Set<Long> getStudentIds() { return studentIds; }
    public void setStudentIds(Set<Long> studentIds) { this.studentIds = studentIds; }
    public Set<Long> getCoachIds() { return coachIds; }
    public void setCoachIds(Set<Long> coachIds) { this.coachIds = coachIds; }
}
