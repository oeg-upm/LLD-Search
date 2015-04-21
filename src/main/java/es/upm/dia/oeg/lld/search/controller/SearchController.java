package es.upm.dia.oeg.lld.search.controller;

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
        model.addAttribute("searchElement", new SearchElement());
        return "search";
    }

    @RequestMapping(value = "/search", method = RequestMethod.POST)
    public String search(@ModelAttribute SearchElement searchElement,
            Model model) {
        final Translation translation = this.translationService.getTranslationByID(searchElement.getQuery());
        model.addAttribute("translation", translation);
        return "result";
    }
}
