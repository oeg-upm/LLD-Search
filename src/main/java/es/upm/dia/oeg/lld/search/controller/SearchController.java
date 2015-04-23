package es.upm.dia.oeg.lld.search.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import es.upm.dia.oeg.lld.search.model.SearchQuery;
import es.upm.dia.oeg.lld.search.model.Translation;
import es.upm.dia.oeg.lld.search.service.TranslationService;

@Controller
public class SearchController {

    @Autowired
    TranslationService translationService;

    @RequestMapping(value = "/search", method = RequestMethod.GET)
    public String searchForm(Model model) {
        final List<String> languages = this.translationService.getLanguages();
        model.addAttribute("languages", languages);
        model.addAttribute("searchQuery", new SearchQuery());
        return "search-form";
    }

    @RequestMapping(value = "/search", method = RequestMethod.POST)
    public String search(@ModelAttribute SearchQuery searchQuery, Model model) {
        final List<Translation> translations = this.translationService.getTranslations(searchQuery);
        model.addAttribute("searchQuery", searchQuery);
        model.addAttribute("translations", translations);

        if (searchQuery.isIndirect()) {
            return "results-indirect";
        }

        return "results-direct";
    }
}
