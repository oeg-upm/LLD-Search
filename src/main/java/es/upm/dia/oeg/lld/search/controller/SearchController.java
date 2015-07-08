package es.upm.dia.oeg.lld.search.controller;

import java.util.ArrayList;
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
        initLists(model);
        model.addAttribute("searchQuery", new SearchQuery());
        return "search-form";
    }

    @RequestMapping(value = "/search", method = RequestMethod.POST)
    public String search(@ModelAttribute @Valid SearchQuery searchQuery,
            BindingResult bindingResult, Model model) {

        model.addAttribute("searchQuery", searchQuery);

        this.sqValidator.validate(searchQuery, bindingResult);

        if (bindingResult.hasErrors()) {
            initLists(model);
            return "search-form";
        }

        List<Translation> translations = this.translationService.getTranslations(searchQuery);
        
        // if is direct and has no values - only is possible if you have a target lang
        if((translations.isEmpty()) && (!searchQuery.isIndirect()) && (!searchQuery.getLangTarget().equalsIgnoreCase("All")) ){
        	
        	searchQuery.setIndirect(true);
        	translations = this.translationService.getTranslations(searchQuery);
        	model.addAttribute("translations", translations);
        	return "results-indirect";
        	
        }
        
        
        model.addAttribute("translations", translations);

        if (searchQuery.isIndirect()) {
            return "results-indirect";
        }
        
        
        

        return "results-direct";
    }
    
    private void initLists(Model model) {
        final List<String> languages = this.translationService.getLanguages();
        final List<String> languagesTo = new ArrayList<String>();

        // // First option is all languages (no restriction)
        languagesTo.add("All");
        languagesTo.addAll(languages);
        
        model.addAttribute("languagesFrom", languages);
        model.addAttribute("languagesTo", languagesTo);
    }
    
}
