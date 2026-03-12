package com.example.rh.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.example.rh.Model.Funcionario;
import com.example.rh.Repository.FuncionarioRepository;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.servlet.ModelAndView;


@Controller
public class FuncionarioController {
    //atributo
    @Autowired
    private FuncionarioRepository fr;

    //quando o endereço /funcionario for digitado na bara de endereço
    // o site será direcionado para a página de cadastro de funcinário
    @RequestMapping(value="/funcionario", method=RequestMethod.GET)
    public String abrirFuncionario() {
        return "funcionario/funcionario";
    }

    // requisição para cadastrar funcionário no banco
    @RequestMapping(value = "/funcionario", method = RequestMethod.POST)
    public String gravarFuncionario (Funcionario funcionario){
        fr.save(funcionario);
        return "redirect:/listar";
    }
    
    // listar todos os funcionários
    @RequestMapping(value="/listar", method=RequestMethod.GET)
    public ModelAndView listarFuncionarios(){
        ModelAndView mv = new ModelAndView("funcionario/listarfuncionarios");
        mv.addObject("funcionarios", fr.findAll());
        return mv;
    }
    
    // abrir página de edição de funcionário
    @RequestMapping(value="/editarfuncionario/{id}", method=RequestMethod.GET)
    public ModelAndView abrirEditarFuncionario(@PathVariable("id") long id){
        Funcionario funcionario = fr.findById(id);
        ModelAndView mv = new ModelAndView("funcionario/editarfuncionario");
        mv.addObject("funcionario", funcionario);
        return mv;
    }
    
    // salvar edição do funcionário
    @RequestMapping(value="/editarfuncionario/{id}", method=RequestMethod.POST)
    public String gravarEdicaoFuncionario(Funcionario funcionario, @PathVariable("id") long id){
        funcionario.setId(id);
        fr.save(funcionario);
        return "redirect:/listar";
    }
    
    // deletar funcionário
    @RequestMapping(value="/deletarfuncionario/{id}", method=RequestMethod.GET)
    public String deletarFuncionario(@PathVariable("id") long id){
        fr.deleteById(id);
        return "redirect:/listar";
    }
    

    
}
