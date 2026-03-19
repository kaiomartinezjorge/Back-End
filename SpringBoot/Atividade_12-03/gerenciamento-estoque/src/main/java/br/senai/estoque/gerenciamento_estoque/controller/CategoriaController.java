package br.senai.estoque.gerenciamento_estoque.controller;

import java.util.Optional;

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
import br.senai.estoque.gerenciamento_estoque.repository.AtivoPatrimonialRepository;
import br.senai.estoque.gerenciamento_estoque.repository.CategoriaRepository;
import br.senai.estoque.gerenciamento_estoque.repository.MateriaisRepository;
import jakarta.servlet.http.HttpSession;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;

@Controller
public class CategoriaController extends ControllerBase {

	@Autowired
	private CategoriaRepository categoriaRepository;

	@Autowired
	private MateriaisRepository materiaisRepository;

	@Autowired
	private AtivoPatrimonialRepository ativoPatrimonialRepository;

	@GetMapping("/categorias")
	public String listarCategorias(Model model, HttpSession session) {
		if (!estaLogado(session)) {
			return "redirect:/login";
		}
		model.addAttribute("categorias", categoriaRepository.findAllByOrderByNomeAsc());
		return "categoria/lista";
	}

	@GetMapping("/categorias/nova")
	public String novaCategoria(Model model, HttpSession session) {
		if (!estaLogado(session)) {
			return "redirect:/login";
		}
		prepararFormulario(model, new Categoria(), "nova categoria", "/categorias/salvar");
		return "categoria/form";
	}

	@PostMapping("/categorias/salvar")
	@Transactional
	public String salvarCategoria(@Valid @ModelAttribute("categoria") Categoria categoria, BindingResult result,
			Model model, RedirectAttributes attributes, HttpSession session) {
		if (!estaLogado(session)) {
			return "redirect:/login";
		}

		prepararCategoria(categoria);
		validarCategoria(categoria, result);
		if (result.hasErrors()) {
			prepararFormulario(model, categoria, "nova categoria", "/categorias/salvar");
			return "categoria/form";
		}

		categoriaRepository.save(categoria);
		attributes.addFlashAttribute("mensagem", "categoria cadastrada com sucesso");
		attributes.addFlashAttribute("tipoMensagem", "sucesso");
		return "redirect:/categorias";
	}

	@GetMapping("/categorias/editar/{id}")
	public String editarCategoria(@PathVariable Long id, Model model, HttpSession session) {
		if (!estaLogado(session)) {
			return "redirect:/login";
		}
		Categoria categoria = buscarCategoria(id);
		prepararFormulario(model, categoria, "editar categoria", "/categorias/atualizar/" + id);
		return "categoria/form";
	}

	@PostMapping("/categorias/atualizar/{id}")
	@Transactional
	public String atualizarCategoria(@PathVariable Long id, @Valid @ModelAttribute("categoria") Categoria categoria,
			BindingResult result, Model model, RedirectAttributes attributes, HttpSession session) {
		if (!estaLogado(session)) {
			return "redirect:/login";
		}

		Categoria categoriaAtual = buscarCategoria(id);
		categoriaAtual.setNome(limparTexto(categoria.getNome()));
		categoriaAtual.setDescricao(limparTextoOpcional(categoria.getDescricao()));
		validarCategoria(categoriaAtual, result);
		if (result.hasErrors()) {
			prepararFormulario(model, categoriaAtual, "editar categoria", "/categorias/atualizar/" + id);
			return "categoria/form";
		}

		categoriaRepository.save(categoriaAtual);
		attributes.addFlashAttribute("mensagem", "categoria atualizada com sucesso");
		attributes.addFlashAttribute("tipoMensagem", "sucesso");
		return "redirect:/categorias";
	}

	@PostMapping("/categorias/excluir/{id}")
	@Transactional
	public String excluirCategoria(@PathVariable Long id, RedirectAttributes attributes, HttpSession session) {
		if (!estaLogado(session)) {
			return "redirect:/login";
		}

		if (materiaisRepository.existsByCategoria_Id(id) || ativoPatrimonialRepository.existsByCategoria_Id(id)) {
			attributes.addFlashAttribute("mensagem", "a categoria esta em uso e nao pode ser excluida");
			attributes.addFlashAttribute("tipoMensagem", "erro");
			return "redirect:/categorias";
		}

		categoriaRepository.delete(buscarCategoria(id));
		attributes.addFlashAttribute("mensagem", "categoria excluida com sucesso");
		attributes.addFlashAttribute("tipoMensagem", "sucesso");
		return "redirect:/categorias";
	}

	private Categoria buscarCategoria(Long id) {
		return categoriaRepository.findById(id)
				.orElseThrow(() -> new IllegalArgumentException("categoria nao encontrada"));
	}

	private void prepararCategoria(Categoria categoria) {
		categoria.setNome(limparTexto(categoria.getNome()));
		categoria.setDescricao(limparTextoOpcional(categoria.getDescricao()));
	}

	private void validarCategoria(Categoria categoria, BindingResult result) {
		Optional<Categoria> categoriaExistente = categoriaRepository.findByNomeIgnoreCase(categoria.getNome());
		if (categoriaExistente.isPresent()) {
			Long idAtual = categoria.getId();
			Long idExistente = categoriaExistente.get().getId();
			if (idAtual == null || !idAtual.equals(idExistente)) {
				result.rejectValue("nome", "categoria.nome", "ja existe uma categoria com este nome");
			}
		}
	}

	private void prepararFormulario(Model model, Categoria categoria, String titulo, String acao) {
		model.addAttribute("categoria", categoria);
		model.addAttribute("titulo", titulo);
		model.addAttribute("acao", acao);
	}
}
