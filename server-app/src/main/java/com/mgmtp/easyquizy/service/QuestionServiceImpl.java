package com.mgmtp.easyquizy.service;

import com.mgmtp.easyquizy.dto.AnswerDTO;
import com.mgmtp.easyquizy.dto.QuestionDTO;
import com.mgmtp.easyquizy.dto.QuestionListViewDTO;
import com.mgmtp.easyquizy.exception.RecordNotFoundException;
import com.mgmtp.easyquizy.mapper.AnswerMapper;
import com.mgmtp.easyquizy.mapper.QuestionMapper;
import com.mgmtp.easyquizy.model.answer.AnswerEntity;
import com.mgmtp.easyquizy.model.question.Difficulty;
import com.mgmtp.easyquizy.model.question.QuestionEntity;
import com.mgmtp.easyquizy.repository.AnswerRepository;
import com.mgmtp.easyquizy.repository.QuestionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class QuestionServiceImpl implements QuestionService {
    @Autowired
    private QuestionRepository questionRepository;
    @Autowired
    private AnswerRepository answerRepository;
    @Autowired
    private QuestionMapper questionMapper;
    @Autowired
    private AnswerMapper answerMapper;

    @Override
    public QuestionDTO getQuestionById(Long id) throws RecordNotFoundException {
        QuestionEntity question = questionRepository.findById(id)
                .orElseThrow(() -> new RecordNotFoundException("No Question record exists for the given id: " + id));
        return questionMapper.questionToQuestionDTO(question);
    }

    @Override
    @Transactional
    public QuestionDTO createQuestion(QuestionDTO questionDTO) throws RecordNotFoundException {
        List<AnswerDTO> answerDTOs = questionDTO.getAnswers();

        List<AnswerEntity> answers = answerDTOs.stream()
                .map(answerMapper::answerDTOtoAnswer)
                .collect(Collectors.toList());

        QuestionEntity question = questionMapper.questionDTOToQuestion(questionDTO);

        question.setAnswers(answers);

        QuestionEntity savedQuestion = questionRepository.save(question);

        return questionMapper.questionToQuestionDTO(savedQuestion);
    }

    @Override
    @Transactional
    public QuestionDTO updateQuestion(QuestionDTO questionDTO) throws RecordNotFoundException {
        if (questionDTO.getId() == null) {
            throw new IllegalArgumentException("Question ID is required");
        }

        QuestionEntity question = questionMapper.questionDTOToQuestion(questionDTO);

        answerRepository.deleteByQuestionId(questionDTO.getId());

        List<AnswerDTO> answerDTOs = questionDTO.getAnswers();

        List<AnswerEntity> answers = answerDTOs.stream()
                .map(answerMapper::answerDTOtoAnswer)
                .toList();

        question.setAnswers(answers);

        QuestionEntity savedQuestion = questionRepository.save(question);

        return questionMapper.questionToQuestionDTO(savedQuestion);
    }

    @Override
    @Transactional
    public void deleteQuestionById(Long id) throws RecordNotFoundException {
        if (questionRepository.existsById(id)){
            questionRepository.deleteById(id);
        } else {
            throw new RecordNotFoundException("No Question record exists for the given id: " + id);
        }
    }

    @Override
    public Page<QuestionListViewDTO> getAllQuestions(
            String keyword, Difficulty difficulty, Integer categoryId, int offset, int limit) {
        int pageNo = offset / limit;
        Specification<QuestionEntity> filterSpec = (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (StringUtils.hasText(keyword)) {
                predicates.add(cb.like(cb.lower(root.get("title")), "%" + keyword.toLowerCase() + "%"));
            }
            if (difficulty != null) {
                predicates.add(cb.equal(root.get("difficulty"), difficulty));
            }
            if (categoryId != null) {
                predicates.add(cb.equal(root.get("category").get("id"), categoryId));
            }
            return cb.and(predicates.toArray(new Predicate[0]));
        };

        Pageable pageable = PageRequest.of(pageNo, limit);

        Page<QuestionEntity> page = questionRepository.findAll(filterSpec, pageable);
        return page.map(questionMapper::questionToQuestionListViewDTO);
    }
}