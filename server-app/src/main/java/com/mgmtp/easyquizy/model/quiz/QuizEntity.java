package com.mgmtp.easyquizy.model.quiz;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.mgmtp.easyquizy.model.event.EventEntity;
import com.mgmtp.easyquizy.model.kahoot.KahootQuizExportStatus;
import com.mgmtp.easyquizy.model.question.QuestionEntity;
import lombok.*;

import javax.persistence.*;
import java.util.List;

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

    @Column(nullable = false)
    private String title;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "event_id", foreignKey = @ForeignKey(name = "fk_quizzes_events"), referencedColumnName = "id")
    @JsonIgnore
    private EventEntity eventEntity;

    @ManyToMany(fetch = FetchType.LAZY, mappedBy = "quizzes")
    private List<QuestionEntity> questions;

    @OneToMany(mappedBy = "quiz", cascade = CascadeType.REMOVE)
    @JsonIgnore
    private List<KahootQuizExportStatus> kahootQuizUsers;

    public void setQuestions(List<QuestionEntity> questions) {
        this.questions = questions;
        questions.forEach(questionEntity -> questionEntity.getQuizzes().add(this));
    }
}
