package es.upm.dia.oeg.lld.search.validator;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import es.upm.dia.oeg.lld.search.model.SearchQuery;

@Component
public class SearchQueryValidator implements Validator {

    @Override
    public boolean supports(Class<?> cls) {
        return SearchQuery.class.isAssignableFrom(cls);
    }

    @Override
    public void validate(Object target, Errors errors) {
        if (target != null && target instanceof SearchQuery) {
            final SearchQuery sq = (SearchQuery) target;

            ValidationUtils.rejectIfEmptyOrWhitespace(errors, "term",
                    "required.term", "Search term is required.");

            if (sq.isIndirect() && sq.getLangTarget().equalsIgnoreCase("All")) {
                errors.rejectValue(
                        "langTarget",
                        "error.langTarget",
                        "Indirect translations need a specific target language, 'All' option is not available");
            }
        }
    }

}
