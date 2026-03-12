package br.senai.estoque.gerenciamento_estoque.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import br.senai.estoque.gerenciamento_estoque.model.Funcionario;
import br.senai.estoque.gerenciamento_estoque.service.AtivoPatrimonialService;
import br.senai.estoque.gerenciamento_estoque.service.CategoriaService;
import br.senai.estoque.gerenciamento_estoque.service.MaterialService;
import br.senai.estoque.gerenciamento_estoque.service.MovimentacaoService;
import br.senai.estoque.gerenciamento_estoque.service.SessaoService;
import jakarta.servlet.http.HttpSession;

@Controller
public class DashboardController {

	@Autowired
	private SessaoService sessaoService;

	@Autowired
	private CategoriaService categoriaService;

	@Autowired
	private MaterialService materialService;

	@Autowired
	private AtivoPatrimonialService ativoPatrimonialService;

	@Autowired
	private MovimentacaoService movimentacaoService;

	@GetMapping("/interna")
	public String acessarInterna(Model model, HttpSession session) {
		Funcionario funcionario = sessaoService.buscarFuncionarioLogado(session);
		if (funcionario == null) {
			return "redirect:/login";
		}

		model.addAttribute("funcionarioLogado", funcionario);
		model.addAttribute("totalCategorias", categoriaService.contar());
		model.addAttribute("totalMateriais", materialService.contar());
		model.addAttribute("totalAtivos", ativoPatrimonialService.contar());
		model.addAttribute("totalMovimentacoes", movimentacaoService.contar());
		model.addAttribute("movimentacoesRecentes", movimentacaoService.listarRecentes(6));
		model.addAttribute("materiaisSemEstoque", materialService.listarSemEstoque());

		return "dashboard/index";
	}
}
