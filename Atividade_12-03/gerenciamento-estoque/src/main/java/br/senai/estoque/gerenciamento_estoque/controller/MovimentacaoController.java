package br.senai.estoque.gerenciamento_estoque.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import br.senai.estoque.gerenciamento_estoque.model.Materiais;
import br.senai.estoque.gerenciamento_estoque.model.Movimentacao;
import br.senai.estoque.gerenciamento_estoque.model.TipoMovimentacao;
import br.senai.estoque.gerenciamento_estoque.repository.MateriaisRepository;
import br.senai.estoque.gerenciamento_estoque.repository.MovimentacaoRepository;
import jakarta.servlet.http.HttpSession;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;

@Controller
public class MovimentacaoController extends ControllerBase {

	@Autowired
	private MovimentacaoRepository movimentacaoRepository;

	@Autowired
	private MateriaisRepository materiaisRepository;

	@GetMapping("/movimentacoes")
	public String listarMovimentacoes(Model model, HttpSession session) {
		if (!estaLogado(session)) {
			return "redirect:/login";
		}
		model.addAttribute("movimentacoes", movimentacaoRepository.findAllByOrderByDataDesc());
		return "movimentacoes/lista";
	}

	@GetMapping("/movimentacoes/nova")
	public String novaMovimentacao(@RequestParam(required = false) Long materialId, Model model, HttpSession session) {
		if (!estaLogado(session)) {
			return "redirect:/login";
		}

		Movimentacao movimentacao = new Movimentacao();
		if (materialId != null) {
			materiaisRepository.findById(materialId)
					.ifPresent(material -> movimentacao.setMaterialNome(material.getNome()));
		}
		prepararFormulario(model, movimentacao);
		return "movimentacoes/form";
	}

	@PostMapping("/movimentacoes/salvar")
	@Transactional
	public String salvarMovimentacao(@Valid @ModelAttribute("movimentacao") Movimentacao movimentacao,
			BindingResult result, Model model, RedirectAttributes attributes, HttpSession session) {
		if (!estaLogado(session)) {
			return "redirect:/login";
		}

		movimentacao.setObservacao(limparTextoOpcional(movimentacao.getObservacao()));
		movimentacao.setMaterialNome(limparTexto(movimentacao.getMaterialNome()));
		Materiais material = buscarMaterial(movimentacao.getMaterialNome(), result);

		if (material != null && movimentacao.getTipo() == TipoMovimentacao.SAIDA
				&& material.getQuantidade() < movimentacao.getQuantidade()) {
			result.reject("movimentacao.invalida", "estoque insuficiente para a saida informada");
		}

		if (result.hasErrors()) {
			prepararFormulario(model, movimentacao);
			return "movimentacoes/form";
		}

		if (movimentacao.getTipo() == TipoMovimentacao.ENTRADA) {
			material.setQuantidade(material.getQuantidade() + movimentacao.getQuantidade());
		} else {
			material.setQuantidade(material.getQuantidade() - movimentacao.getQuantidade());
		}

		materiaisRepository.save(material);
		movimentacao.setMaterial(material);
		movimentacao.setFuncionario(buscarFuncionarioLogado(session));
		movimentacaoRepository.save(movimentacao);

		attributes.addFlashAttribute("mensagem", "movimentacao registrada com sucesso");
		attributes.addFlashAttribute("tipoMensagem", "sucesso");
		return "redirect:/movimentacoes";
	}

	private Materiais buscarMaterial(String materialNome, BindingResult result) {
		if (materialNome == null || materialNome.isBlank()) {
			result.rejectValue("materialNome", "movimentacao.materialNome", "digite o nome do material");
			return null;
		}

		return materiaisRepository.findByNomeIgnoreCase(materialNome)
				.orElseGet(() -> {
					result.rejectValue("materialNome", "movimentacao.materialNome",
							"material nao encontrado. cadastre o material primeiro");
					return null;
				});
	}

	private void prepararFormulario(Model model, Movimentacao movimentacao) {
		model.addAttribute("movimentacao", movimentacao);
		model.addAttribute("tiposMovimentacao", TipoMovimentacao.values());
	}
}
