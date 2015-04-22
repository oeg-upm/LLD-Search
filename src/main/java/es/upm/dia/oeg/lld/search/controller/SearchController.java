package es.upm.dia.oeg.lld.search.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import es.upm.dia.oeg.lld.search.model.SearchElement;
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
        model.addAttribute("searchElement", new SearchElement());
        return "search";
    }

    @RequestMapping(value = "/search", method = RequestMethod.POST)
    public String search(@ModelAttribute SearchElement searchElement,
            Model model) {
        final List<Translation> translations = this.translationService.getAllTranslations(
                searchElement.getTerm(), searchElement.getLangSource(),
                searchElement.getLangTarget());
        model.addAttribute("searchElement", searchElement);
        model.addAttribute("translations", translations);
        return "result";
    }
}
