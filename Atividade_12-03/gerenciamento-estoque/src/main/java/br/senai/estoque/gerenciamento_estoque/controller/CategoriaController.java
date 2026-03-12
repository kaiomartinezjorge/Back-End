package br.senai.estoque.gerenciamento_estoque.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import br.senai.estoque.gerenciamento_estoque.model.Categoria;
import br.senai.estoque.gerenciamento_estoque.service.CategoriaService;
import br.senai.estoque.gerenciamento_estoque.service.SessaoService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

@Controller
public class CategoriaController {

	@Autowired
	private CategoriaService categoriaService;

	@Autowired
	private SessaoService sessaoService;

	@GetMapping("/categorias")
	public String listarCategorias(Model model, HttpSession session) {
		if (!sessaoService.estaLogado(session)) {
			return "redirect:/login";
		}
		model.addAttribute("categorias", categoriaService.listarTodas());
		return "categoria/lista";
	}

	@GetMapping("/categorias/nova")
	public String novaCategoria(Model model, HttpSession session) {
		if (!sessaoService.estaLogado(session)) {
			return "redirect:/login";
		}
		prepararFormulario(model, new Categoria(), "nova categoria", "/categorias/salvar");
		return "categoria/form";
	}

	@PostMapping("/categorias/salvar")
	public String salvarCategoria(@Valid @ModelAttribute("categoria") Categoria categoria, BindingResult result,
			Model model, RedirectAttributes attributes, HttpSession session) {
		if (!sessaoService.estaLogado(session)) {
			return "redirect:/login";
		}
		if (result.hasErrors()) {
			prepararFormulario(model, categoria, "nova categoria", "/categorias/salvar");
			return "categoria/form";
		}

		try {
			categoriaService.salvar(categoria);
			attributes.addFlashAttribute("mensagem", "categoria cadastrada com sucesso");
			attributes.addFlashAttribute("tipoMensagem", "sucesso");
			return "redirect:/categorias";
		} catch (IllegalArgumentException ex) {
			result.rejectValue("nome", "categoria.nome", ex.getMessage());
			prepararFormulario(model, categoria, "nova categoria", "/categorias/salvar");
			return "categoria/form";
		}
	}

	@GetMapping("/categorias/editar/{id}")
	public String editarCategoria(@PathVariable Long id, Model model, HttpSession session) {
		if (!sessaoService.estaLogado(session)) {
			return "redirect:/login";
		}
		Categoria categoria = categoriaService.buscarPorId(id);
		prepararFormulario(model, categoria, "editar categoria", "/categorias/atualizar/" + id);
		return "categoria/form";
	}

	@PostMapping("/categorias/atualizar/{id}")
	public String atualizarCategoria(@PathVariable Long id, @Valid @ModelAttribute("categoria") Categoria categoria,
			BindingResult result, Model model, RedirectAttributes attributes, HttpSession session) {
		if (!sessaoService.estaLogado(session)) {
			return "redirect:/login";
		}
		if (result.hasErrors()) {
			prepararFormulario(model, categoria, "editar categoria", "/categorias/atualizar/" + id);
			return "categoria/form";
		}

		try {
			categoriaService.atualizar(id, categoria);
			attributes.addFlashAttribute("mensagem", "categoria atualizada com sucesso");
			attributes.addFlashAttribute("tipoMensagem", "sucesso");
			return "redirect:/categorias";
		} catch (IllegalArgumentException ex) {
			result.rejectValue("nome", "categoria.nome", ex.getMessage());
			prepararFormulario(model, categoria, "editar categoria", "/categorias/atualizar/" + id);
			return "categoria/form";
		}
	}

	@PostMapping("/categorias/excluir/{id}")
	public String excluirCategoria(@PathVariable Long id, RedirectAttributes attributes, HttpSession session) {
		if (!sessaoService.estaLogado(session)) {
			return "redirect:/login";
		}
		try {
			categoriaService.excluir(id);
			attributes.addFlashAttribute("mensagem", "categoria excluida com sucesso");
			attributes.addFlashAttribute("tipoMensagem", "sucesso");
		} catch (IllegalArgumentException ex) {
			attributes.addFlashAttribute("mensagem", ex.getMessage());
			attributes.addFlashAttribute("tipoMensagem", "erro");
		}

		return "redirect:/categorias";
	}

	private void prepararFormulario(Model model, Categoria categoria, String titulo, String acao) {
		model.addAttribute("categoria", categoria);
		model.addAttribute("titulo", titulo);
		model.addAttribute("acao", acao);
	}
}
