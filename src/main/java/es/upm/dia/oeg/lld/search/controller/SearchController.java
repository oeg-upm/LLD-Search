package es.upm.dia.oeg.lld.search.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import es.upm.dia.oeg.lld.search.model.SearchQuery;
import es.upm.dia.oeg.lld.search.model.Translation;
import es.upm.dia.oeg.lld.search.service.TranslationService;
import es.upm.dia.oeg.lld.search.validator.SearchQueryValidator;

@Controller
public class SearchController {

    @Autowired
    TranslationService translationService;

    @Autowired
    SearchQueryValidator sqValidator;

    @RequestMapping(value = "/search", method = RequestMethod.GET)
    public String searchForm(Model model) {
        final List<String> languages = this.translationService.getLanguages();
        // final List<Dictionary> dictionaries =
        // this.translationService.getDictionaries();
        model.addAttribute("languages", languages);
        // model.addAttribute("dictionaries", dictionaries);
        model.addAttribute("searchQuery", new SearchQuery());
        return "search-form";
    }

    @RequestMapping(value = "/search", method = RequestMethod.POST)
    public String search(@ModelAttribute @Valid SearchQuery searchQuery,
            BindingResult bindingResult, Model model) {

        model.addAttribute("searchQuery", searchQuery);

        this.sqValidator.validate(searchQuery, bindingResult);

        if (bindingResult.hasErrors()) {
            model.addAttribute("languages",
                    this.translationService.getLanguages());
            return "search-form";
        }

        final List<Translation> translations = this.translationService.getTranslations(searchQuery);
        model.addAttribute("translations", translations);

        if (searchQuery.isIndirect()) {
            return "results-indirect";
        }

        return "results-direct";
    }
}
