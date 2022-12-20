package com.sideproject.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@Builder
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name="Project")
public class Project {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "project_id")
    private Long id;

    private String name;

    private String topic;

    private String goal;

    private LocalDateTime startDate;

    private LocalDateTime endDate;

    // TODO: ONE-TO-MANY 로 양방향 해야되는가? 여기서 구해올 필요가 없다면 단방향이 맞는 것 같긴하다.
}