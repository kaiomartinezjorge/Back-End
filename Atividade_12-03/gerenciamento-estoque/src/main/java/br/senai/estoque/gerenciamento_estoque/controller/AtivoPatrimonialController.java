package br.senai.estoque.gerenciamento_estoque.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import br.senai.estoque.gerenciamento_estoque.dto.AtivoPatrimonialForm;
import br.senai.estoque.gerenciamento_estoque.model.AtivoPatrimonial;
import br.senai.estoque.gerenciamento_estoque.model.Funcionario;
import br.senai.estoque.gerenciamento_estoque.service.AtivoPatrimonialService;
import br.senai.estoque.gerenciamento_estoque.service.CategoriaService;
import br.senai.estoque.gerenciamento_estoque.service.SessaoService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

@Controller
public class AtivoPatrimonialController {

	@Autowired
	private AtivoPatrimonialService ativoPatrimonialService;

	@Autowired
	private CategoriaService categoriaService;

	@Autowired
	private SessaoService sessaoService;

	@GetMapping("/ativos")
	public String listarAtivos(Model model, HttpSession session) {
		if (!sessaoService.estaLogado(session)) {
			return "redirect:/login";
		}
		model.addAttribute("ativos", ativoPatrimonialService.listarTodos());
		return "ativos/lista";
	}

	@GetMapping("/ativos/novo")
	public String novoAtivo(Model model, HttpSession session) {
		if (!sessaoService.estaLogado(session)) {
			return "redirect:/login";
		}
		prepararFormulario(model, new AtivoPatrimonialForm(), "novo ativo patrimonial", "/ativos/salvar");
		return "ativos/form";
	}

	@PostMapping("/ativos/salvar")
	public String salvarAtivo(@Valid @ModelAttribute("ativoPatrimonialForm") AtivoPatrimonialForm ativoPatrimonialForm,
			BindingResult result, Model model, RedirectAttributes attributes, HttpSession session) {
		if (!sessaoService.estaLogado(session)) {
			return "redirect:/login";
		}
		if (result.hasErrors()) {
			prepararFormulario(model, ativoPatrimonialForm, "novo ativo patrimonial", "/ativos/salvar");
			return "ativos/form";
		}

		try {
			Funcionario funcionario = sessaoService.buscarFuncionarioLogado(session);
			ativoPatrimonialService.salvar(ativoPatrimonialForm, funcionario);
			attributes.addFlashAttribute("mensagem", "ativo patrimonial cadastrado com sucesso");
			attributes.addFlashAttribute("tipoMensagem", "sucesso");
			return "redirect:/ativos";
		} catch (IllegalArgumentException ex) {
			result.reject("ativo.invalido", ex.getMessage());
			prepararFormulario(model, ativoPatrimonialForm, "novo ativo patrimonial", "/ativos/salvar");
			return "ativos/form";
		}
	}

	@GetMapping("/ativos/editar/{id}")
	public String editarAtivo(@PathVariable Long id, Model model, HttpSession session) {
		if (!sessaoService.estaLogado(session)) {
			return "redirect:/login";
		}
		AtivoPatrimonial ativo = ativoPatrimonialService.buscarPorId(id);
		AtivoPatrimonialForm ativoPatrimonialForm = new AtivoPatrimonialForm();
		ativoPatrimonialForm.setNome(ativo.getNome());
		ativoPatrimonialForm.setDescricao(ativo.getDescricao());
		ativoPatrimonialForm.setNumeroPatrimonio(ativo.getNumeroPatrimonio());
		ativoPatrimonialForm.setLocalizacao(ativo.getLocalizacao());
		ativoPatrimonialForm.setStatus(ativo.getStatus());
		ativoPatrimonialForm.setCategoriaId(ativo.getCategoria().getId());

		prepararFormulario(model, ativoPatrimonialForm, "editar ativo patrimonial", "/ativos/atualizar/" + id);
		return "ativos/form";
	}

	@PostMapping("/ativos/atualizar/{id}")
	public String atualizarAtivo(@PathVariable Long id,
			@Valid @ModelAttribute("ativoPatrimonialForm") AtivoPatrimonialForm ativoPatrimonialForm,
			BindingResult result, Model model, RedirectAttributes attributes, HttpSession session) {
		if (!sessaoService.estaLogado(session)) {
			return "redirect:/login";
		}
		if (result.hasErrors()) {
			prepararFormulario(model, ativoPatrimonialForm, "editar ativo patrimonial", "/ativos/atualizar/" + id);
			return "ativos/form";
		}

		try {
			ativoPatrimonialService.atualizar(id, ativoPatrimonialForm);
			attributes.addFlashAttribute("mensagem", "ativo patrimonial atualizado com sucesso");
			attributes.addFlashAttribute("tipoMensagem", "sucesso");
			return "redirect:/ativos";
		} catch (IllegalArgumentException ex) {
			result.reject("ativo.invalido", ex.getMessage());
			prepararFormulario(model, ativoPatrimonialForm, "editar ativo patrimonial", "/ativos/atualizar/" + id);
			return "ativos/form";
		}
	}

	@PostMapping("/ativos/excluir/{id}")
	public String excluirAtivo(@PathVariable Long id, RedirectAttributes attributes, HttpSession session) {
		if (!sessaoService.estaLogado(session)) {
			return "redirect:/login";
		}
		try {
			ativoPatrimonialService.excluir(id);
			attributes.addFlashAttribute("mensagem", "ativo patrimonial excluido com sucesso");
			attributes.addFlashAttribute("tipoMensagem", "sucesso");
		} catch (IllegalArgumentException ex) {
			attributes.addFlashAttribute("mensagem", ex.getMessage());
			attributes.addFlashAttribute("tipoMensagem", "erro");
		}

		return "redirect:/ativos";
	}

	private void prepararFormulario(Model model, AtivoPatrimonialForm ativoPatrimonialForm, String titulo, String acao) {
		List<String> statusDisponiveis = List.of("em uso", "em estoque", "em manutencao", "baixado");
		model.addAttribute("ativoPatrimonialForm", ativoPatrimonialForm);
		model.addAttribute("titulo", titulo);
		model.addAttribute("acao", acao);
		model.addAttribute("categorias", categoriaService.listarTodas());
		model.addAttribute("statusDisponiveis", statusDisponiveis);
	}
}
