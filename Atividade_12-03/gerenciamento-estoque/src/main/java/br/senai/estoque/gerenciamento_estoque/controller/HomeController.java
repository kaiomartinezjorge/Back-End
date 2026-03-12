package br.senai.estoque.gerenciamento_estoque.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import br.senai.estoque.gerenciamento_estoque.service.AtivoPatrimonialService;
import br.senai.estoque.gerenciamento_estoque.service.CategoriaService;
import br.senai.estoque.gerenciamento_estoque.service.MaterialService;
import br.senai.estoque.gerenciamento_estoque.service.MovimentacaoService;

@Controller
public class HomeController {

	@Autowired
	private CategoriaService categoriaService;

	@Autowired
	private MaterialService materialService;

	@Autowired
	private AtivoPatrimonialService ativoPatrimonialService;

	@Autowired
	private MovimentacaoService movimentacaoService;

	@GetMapping({ "/", "/home" })
	public String acessarHome(Model model) {
		model.addAttribute("totalCategorias", categoriaService.contar());
		model.addAttribute("totalMateriais", materialService.contar());
		model.addAttribute("totalAtivos", ativoPatrimonialService.contar());
		model.addAttribute("totalMovimentacoes", movimentacaoService.contar());
		model.addAttribute("movimentacoesRecentes", movimentacaoService.listarRecentes(5));
		return "index";
	}
}
