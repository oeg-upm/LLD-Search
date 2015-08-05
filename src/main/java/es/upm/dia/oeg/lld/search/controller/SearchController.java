package es.upm.dia.oeg.lld.search.controller;


import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import es.upm.dia.oeg.lld.search.model.Language;
import es.upm.dia.oeg.lld.search.model.LanguageTranslationMap;
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
    
    @Autowired
    LanguageTranslationMap LangMap;
    

    // Default main page
    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String searchForm(HttpServletRequest request,Model model) {
        initLists(model); 
        model.addAttribute("searchQuery", new SearchQuery());
        request.getSession().setAttribute("status", null);
        
        return "search-form";
    }
    
    // If you try to go to /search by get you are redirect
    @RequestMapping(value = "/search", method = RequestMethod.GET)
    public String searchForm2(HttpServletRequest request,Model model) {
        initLists(model);
        model.addAttribute("searchQuery", new SearchQuery());
        request.getSession().setAttribute("status", null);
        
        return "redirect:/";
    }
    

    @RequestMapping(value = "/search", method = RequestMethod.POST)
    public String search(HttpServletRequest request,@ModelAttribute @Valid SearchQuery searchQuery,
            BindingResult bindingResult, Model model, final RedirectAttributes redirectAttributes) {
    	
    	 HttpSession  session= request.getSession();
    	
    	// validate if errors     
        this.sqValidator.validate(searchQuery, bindingResult);
        
        Language LangSource= LangMap.getSourceLanguage(searchQuery.getLangSource());
        // ADD ATTRIBUTES FOR LANG TARGET (Navigation - view)
        initLists(model);
        model.addAttribute("languagesTargetDirect", LangSource.translationLang);
        model.addAttribute("languagesTargetIndirect", LangSource.indirectLang);
        model.addAttribute("languagesPivot", LangSource.pivotLang);
       
        if (bindingResult.hasErrors()) {
        	
            
            model.addAttribute("searchQuery", searchQuery);
            
            if(session.getAttribute("status")!=null){
            	if(session.getAttribute("status").equals("direct")){
            		return "results-direct";
            	}else{
            		return "results-indirect";
            	}
            	
            } else{
            	return "search-form";
            	
            }
        }
        
        // set code languages
    	searchQuery.setLangSourceCode(LangMap.getLangCode(searchQuery.getLangSource()));
    	searchQuery.setLangTargetCode(LangMap.getLangCode(searchQuery.getLangTarget()));
    	searchQuery.setPivotLanguageCode(LangMap.getLangCode(searchQuery.getPivotLanguage()));
    	
    	 // IS INDIRECT TRANSLATION?? 
        searchQuery.setIndirect(LangSource.checkIsIndirect(searchQuery.getLangTarget()));
        
       
        
        List<Translation> translations = this.translationService.getTranslations(searchQuery,LangSource);
        model.addAttribute("translations", translations);
        model.addAttribute("searchQuery", searchQuery); 
        
        if (searchQuery.isIndirect()) {
        	session.setAttribute("status", "indirect");
            return "results-indirect";
        }

        session.setAttribute("status", "direct");
        return "results-direct";
    }
    
   
    
    
    private void initLists(Model model) {
        final List<String> languages = this.LangMap.getAllLanguagesLabels();

        model.addAttribute("languagesFrom", languages);
        model.addAttribute("languagesMapArray", LangMap.getLanguageArray());
       
    }
    
}
