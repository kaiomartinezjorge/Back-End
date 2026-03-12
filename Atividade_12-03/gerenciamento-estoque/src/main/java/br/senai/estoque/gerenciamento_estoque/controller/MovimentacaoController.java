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

import br.senai.estoque.gerenciamento_estoque.dto.MovimentacaoForm;
import br.senai.estoque.gerenciamento_estoque.model.Funcionario;
import br.senai.estoque.gerenciamento_estoque.model.TipoMovimentacao;
import br.senai.estoque.gerenciamento_estoque.service.MaterialService;
import br.senai.estoque.gerenciamento_estoque.service.MovimentacaoService;
import br.senai.estoque.gerenciamento_estoque.service.SessaoService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

@Controller
public class MovimentacaoController {

	@Autowired
	private MovimentacaoService movimentacaoService;

	@Autowired
	private MaterialService materialService;

	@Autowired
	private SessaoService sessaoService;

	@GetMapping("/movimentacoes")
	public String listarMovimentacoes(Model model, HttpSession session) {
		if (!sessaoService.estaLogado(session)) {
			return "redirect:/login";
		}
		model.addAttribute("movimentacoes", movimentacaoService.listarTodas());
		return "movimentacoes/lista";
	}

	@GetMapping("/movimentacoes/nova")
	public String novaMovimentacao(@RequestParam(required = false) Long materialId, Model model, HttpSession session) {
		if (!sessaoService.estaLogado(session)) {
			return "redirect:/login";
		}
		MovimentacaoForm movimentacaoForm = new MovimentacaoForm();

		if (materialId != null) {
			movimentacaoForm.setMaterialId(materialId);
		}

		prepararFormulario(model, movimentacaoForm);
		return "movimentacoes/form";
	}

	@PostMapping("/movimentacoes/salvar")
	public String salvarMovimentacao(@Valid @ModelAttribute("movimentacaoForm") MovimentacaoForm movimentacaoForm,
			BindingResult result, Model model, RedirectAttributes attributes, HttpSession session) {
		if (!sessaoService.estaLogado(session)) {
			return "redirect:/login";
		}
		if (result.hasErrors()) {
			prepararFormulario(model, movimentacaoForm);
			return "movimentacoes/form";
		}

		try {
			Funcionario funcionario = sessaoService.buscarFuncionarioLogado(session);
			movimentacaoService.registrar(movimentacaoForm, funcionario);
			attributes.addFlashAttribute("mensagem", "movimentacao registrada com sucesso");
			attributes.addFlashAttribute("tipoMensagem", "sucesso");
			return "redirect:/movimentacoes";
		} catch (IllegalArgumentException ex) {
			result.reject("movimentacao.invalida", ex.getMessage());
			prepararFormulario(model, movimentacaoForm);
			return "movimentacoes/form";
		}
	}

	private void prepararFormulario(Model model, MovimentacaoForm movimentacaoForm) {
		model.addAttribute("movimentacaoForm", movimentacaoForm);
		model.addAttribute("tiposMovimentacao", TipoMovimentacao.values());
		model.addAttribute("materiais", materialService.listarTodos());
	}
}
