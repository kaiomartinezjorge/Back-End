package br.senai.estoque.gerenciamento_estoque.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import br.senai.estoque.gerenciamento_estoque.model.Funcionario;
import br.senai.estoque.gerenciamento_estoque.model.Materiais;
import br.senai.estoque.gerenciamento_estoque.model.Movimentacao;
import br.senai.estoque.gerenciamento_estoque.repository.AtivoPatrimonialRepository;
import br.senai.estoque.gerenciamento_estoque.repository.CategoriaRepository;
import br.senai.estoque.gerenciamento_estoque.repository.MateriaisRepository;
import br.senai.estoque.gerenciamento_estoque.repository.MovimentacaoRepository;
import jakarta.servlet.http.HttpSession;

@Controller
public class DashboardController extends ControllerBase {

	@Autowired
	private CategoriaRepository categoriaRepository;

	@Autowired
	private MateriaisRepository materiaisRepository;

	@Autowired
	private AtivoPatrimonialRepository ativoPatrimonialRepository;

	@Autowired
	private MovimentacaoRepository movimentacaoRepository;

	@GetMapping("/interna")
	public String acessarInterna(Model model, HttpSession session) {
		Funcionario funcionario = buscarFuncionarioLogado(session);
		if (funcionario == null) {
			return "redirect:/login";
		}

		List<Movimentacao> movimentacoesRecentes = movimentacaoRepository.findAllByOrderByDataDesc()
				.stream()
				.limit(6)
				.toList();

		List<Materiais> materiaisSemEstoque = materiaisRepository.findAllByOrderByNomeAsc()
				.stream()
				.filter(material -> material.getQuantidade() == 0)
				.toList();

		model.addAttribute("funcionarioLogado", funcionario);
		model.addAttribute("totalCategorias", categoriaRepository.count());
		model.addAttribute("totalMateriais", materiaisRepository.count());
		model.addAttribute("totalAtivos", ativoPatrimonialRepository.count());
		model.addAttribute("totalMovimentacoes", movimentacaoRepository.count());
		model.addAttribute("movimentacoesRecentes", movimentacoesRecentes);
		model.addAttribute("materiaisSemEstoque", materiaisSemEstoque);

		return "dashboard/index";
	}
}
