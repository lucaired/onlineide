package edu.tum.ase.validators;

import org.hibernate.validator.constraintvalidation.HibernateConstraintValidatorContext;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

// Adopted from: https://www.baeldung.com/javax-validations-enums

public class ValueOfEnumValidator implements ConstraintValidator<ValueOfEnum, CharSequence> {
    private List<String> acceptedValues;

    @Override
    public void initialize(ValueOfEnum annotation) {
        acceptedValues = Stream.of(annotation.enumClass().getEnumConstants()).map(Enum::name).collect(Collectors.toList());
    }

    @Override
    public boolean isValid(CharSequence value, ConstraintValidatorContext context) {
        if (value == null) {
            return true;
        }

        boolean isValid = acceptedValues.contains(value.toString());

        HibernateConstraintValidatorContext hibernateContext = context.unwrap(HibernateConstraintValidatorContext.class);

        if (!isValid) {
            hibernateContext.disableDefaultConstraintViolation();
            hibernateContext
                    .addExpressionVariable("allowedValues", acceptedValues)
                    .buildConstraintViolationWithTemplate("value must be one of ${allowedValues}")
                    .addConstraintViolation();
        }

        return isValid;
    }
}
