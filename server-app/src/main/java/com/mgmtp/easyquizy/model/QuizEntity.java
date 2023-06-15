package com.mgmtp.easyquizy.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import java.util.List;
import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "quizzes")
@Builder
public class QuizEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "title", nullable = false)
    private String title;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "quizzes_id", foreignKey = @ForeignKey(name = "fk_quizzes_events"), referencedColumnName = "id")
    @JsonIgnore
    private EventEntity eventEntity;

    @ManyToMany(fetch = FetchType.LAZY, mappedBy = "quizEntities")
    private List<QuestionEntity> questionEntities;
}
