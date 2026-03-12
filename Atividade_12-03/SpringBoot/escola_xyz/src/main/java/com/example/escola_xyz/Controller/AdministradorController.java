package com.example.escola_xyz.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.example.escola_xyz.model.Administrador;
import com.example.escola_xyz.repository.AdministradorRepository;
import com.example.escola_xyz.repository.VerificaCadastroAdmRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;




@Controller
public class AdministradorController {

    // Permite a Transfomração de um OBJ-Java em Entidade de BD
    // Cada Obj Criado Vira uma Linha do BD
    @Autowired
    AdministradorRepository ar;

    @Autowired
    VerificaCadastroAdmRepository vcar;

    // verificar acesso e cadastro
    boolean acessoAdm = false;

    // métodos
    //para navegar na página de cadastro
    @GetMapping("/cadastro-adm")
    public String acessoCadastroAdmPage() {
        return "cadastro/cadastro-adm";
    }

    // método para enviar o cadastro do Adm
    @PostMapping("/cadastro-adm")
    public ModelAndView cadastrarAdmBD(Administrador adm, RedirectAttributes attributes) {
        boolean verificarCpf = vcar.existsById(adm.getCpf()); //se existir retorna true, caso contrario retorna false
        ModelAndView mv =new ModelAndView("redirect:/login-adm");// retrona sempre para a tela de login
        if(verificarCpf){
            //obj adm -> pega as informações do formulário e cria um obj da classe adm
            ar.save(adm); // salva no bd
            //criar uma mensagem para o usuário
            String mensagem = "Cadastro Realizado com Sucesso";
            //log para o sistema
            System.out.println(mensagem);
            attributes.addFlashAttribute("msg", mensagem); //leva a mensagem para a tela de view
            attributes.addFlashAttribute("classe","verde");
        }else{ // deu errado , pessoa não pode se cadastrar ( caso o CPF não esteja no PRE-CADASTRO)
            String mensagem = "Cadastro não Permitido";
            System.out.println(mensagem);
            attributes.addFlashAttribute("msg",mensagem);
            attributes.addFlashAttribute("classe","vermelho");
        }
        
        return mv;
    }

    //método para acessar (get) página de login do Adm
    @GetMapping("/login-adm")
    public String acessoLoginPageAdm() {
        return "login/login-adm";
    }

    //método para carregar a página interna após o login
    @PostMapping("/acesso-adm")
    public ModelAndView acessoAdm(@RequestParam String cpf, @RequestParam String senha, RedirectAttributes attributes) {
        
        ModelAndView mv = new ModelAndView("redirect:/interna-adm");
        boolean verificaCpf = ar.existsById(cpf); //verifica se o cpf está cadasrtado
        boolean verificaSenha = ar.findByCpf(cpf).getSenha().equals(senha);//pego o cpf, solcito a senha e comparop com a senha digitada

        if(verificaCpf && verificaSenha){
            acessoAdm = true;
        }else{
            String mensagem = "CPF ou Senha Incorreto";
            System.out.println(mensagem);
            attributes.addFlashAttribute("msg", mensagem);
            attributes.addFlashAttribute("classe","vermelho");
            mv.setViewName("redirect:/login-adm");


        }
        
        return mv;
    }
    
    
    //Acessar a página interna
    @GetMapping("/interna-adm")
    public ModelAndView acessoInternaPageAdm (RedirectAttributes attributes) {
        ModelAndView mv = new ModelAndView("interna/interna-adm");
        return mv;
    }

    //método para logout
    @GetMapping("/logout-adm")
    public String logoutAdm() {
        acessoAdm = false;
        return "redirect:/home";
    }
    
}
