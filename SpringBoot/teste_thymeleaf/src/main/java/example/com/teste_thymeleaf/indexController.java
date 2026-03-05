package example.com.teste_thymeleaf;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class indexController {

    // Método de Requisição do tipo get
    @RequestMapping(value = "/", method = RequestMethod.GET)
    public ModelAndView abrirIndex() {
        // Model para manipulação de html na página index
        ModelAndView mv = new ModelAndView("index");
        String mensagem = "Olá, Visitante";
        mv.addObject("msg", mensagem);
        return mv;
    }

    // Criar as rotas de navegação
    // SOBRE
    @GetMapping("/sobre")
    public String abrirSobre() {
        return "sobre";
    }

    // PRODUTOS
    @GetMapping("/produtos")
    public String abrirProdutos() {
        return "produtos";
    }

    // CONTATOS
    @GetMapping("/contato")
    public String abrirContato() {
        return "contato";
    }

    // Método para enviar o formulário com o nome do usuário
    @PostMapping("/home")
    public ModelAndView postHome(@RequestParam ("nome") String nome) {
        ModelAndView mv = new ModelAndView("index");
        String mensagem = "Olá, "+nome;
        mv.addObject("msg", mensagem);
        mv.addObject("nome","");
        
        return mv;

    }

}
