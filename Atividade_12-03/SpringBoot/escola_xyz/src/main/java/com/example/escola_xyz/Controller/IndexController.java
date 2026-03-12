package com.example.escola_xyz.Controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;


//Anotação da classe Controller (interagir view e model)
@Controller
public class IndexController {

    //Método mais completo de buscar a pagina
    @GetMapping("/home")
    public ModelAndView acessoHomePage() {
        ModelAndView mv = new ModelAndView("index");
        return mv;
    }

    //Método mais simples de buscar uma pagina
    @GetMapping("/sobre")
    public String acessosobre() {
        return "sobre";
    }

     @GetMapping("/contato")
    public String acessocontato() {
        return "contato";
    }
    
    
}
     