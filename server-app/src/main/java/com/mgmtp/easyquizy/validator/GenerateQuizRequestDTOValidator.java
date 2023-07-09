package com.mgmtp.easyquizy.validator;

import com.mgmtp.easyquizy.dto.GenerateQuizRequestDTO;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
public class GenerateQuizRequestDTOValidator  implements Validator {
    @Override
    public boolean supports(Class<?> clazz) {
        return GenerateQuizRequestDTO.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        GenerateQuizRequestDTO generateQuizRequestDTO = (GenerateQuizRequestDTO) target;

        if (generateQuizRequestDTO.getCategoryPercentages().values().stream().mapToDouble(Double::doubleValue).sum() != 1.0) {
            errors.rejectValue("categoryPercentages", "categoryPercentages.sum", "The sum of category percentages must be equal to 1!");
        }
    }
}