package br.senai.estoque.gerenciamento_estoque.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import br.senai.estoque.gerenciamento_estoque.model.Movimentacao;
import br.senai.estoque.gerenciamento_estoque.repository.AtivoPatrimonialRepository;
import br.senai.estoque.gerenciamento_estoque.repository.CategoriaRepository;
import br.senai.estoque.gerenciamento_estoque.repository.MateriaisRepository;
import br.senai.estoque.gerenciamento_estoque.repository.MovimentacaoRepository;

@Controller
public class HomeController extends ControllerBase {

	@Autowired
	private CategoriaRepository categoriaRepository;

	@Autowired
	private MateriaisRepository materiaisRepository;

	@Autowired
	private AtivoPatrimonialRepository ativoPatrimonialRepository;

	@Autowired
	private MovimentacaoRepository movimentacaoRepository;

	@GetMapping({ "/", "/home" })
	public String acessarHome(Model model) {
		List<Movimentacao> movimentacoesRecentes = movimentacaoRepository.findAllByOrderByDataDesc()
				.stream()
				.limit(5)
				.toList();

		model.addAttribute("totalCategorias", categoriaRepository.count());
		model.addAttribute("totalMateriais", materiaisRepository.count());
		model.addAttribute("totalAtivos", ativoPatrimonialRepository.count());
		model.addAttribute("totalMovimentacoes", movimentacaoRepository.count());
		model.addAttribute("movimentacoesRecentes", movimentacoesRecentes);
		return "index";
	}
}
