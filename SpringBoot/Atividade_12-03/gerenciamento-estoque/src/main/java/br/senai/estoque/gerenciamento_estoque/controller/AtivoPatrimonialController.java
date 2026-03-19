package br.senai.estoque.gerenciamento_estoque.controller;

import java.util.List;
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

import br.senai.estoque.gerenciamento_estoque.model.AtivoPatrimonial;
import br.senai.estoque.gerenciamento_estoque.model.Categoria;
import br.senai.estoque.gerenciamento_estoque.repository.AtivoPatrimonialRepository;
import br.senai.estoque.gerenciamento_estoque.repository.CategoriaRepository;
import jakarta.servlet.http.HttpSession;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;

@Controller
public class AtivoPatrimonialController extends ControllerBase {

	@Autowired
	private AtivoPatrimonialRepository ativoPatrimonialRepository;

	@Autowired
	private CategoriaRepository categoriaRepository;

	@GetMapping("/ativos")
	public String listarAtivos(Model model, HttpSession session) {
		if (!estaLogado(session)) {
			return "redirect:/login";
		}
		model.addAttribute("ativos", ativoPatrimonialRepository.findAllByOrderByNomeAsc());
		return "ativos/lista";
	}

	@GetMapping("/ativos/novo")
	public String novoAtivo(Model model, HttpSession session) {
		if (!estaLogado(session)) {
			return "redirect:/login";
		}
		prepararFormulario(model, new AtivoPatrimonial(), "novo ativo patrimonial", "/ativos/salvar");
		return "ativos/form";
	}

	@PostMapping("/ativos/salvar")
	@Transactional
	public String salvarAtivo(@Valid @ModelAttribute("ativoPatrimonial") AtivoPatrimonial ativoPatrimonial,
			BindingResult result, Model model, RedirectAttributes attributes, HttpSession session) {
		if (!estaLogado(session)) {
			return "redirect:/login";
		}

		prepararAtivo(ativoPatrimonial);
		validarNumeroPatrimonial(ativoPatrimonial, result);
		Categoria categoria = buscarCategoria(ativoPatrimonial.getCategoriaId(), result);
		if (result.hasErrors()) {
			prepararFormulario(model, ativoPatrimonial, "novo ativo patrimonial", "/ativos/salvar");
			return "ativos/form";
		}

		ativoPatrimonial.setCategoria(categoria);
		ativoPatrimonial.setCadastradoPor(buscarFuncionarioLogado(session));
		ativoPatrimonialRepository.save(ativoPatrimonial);

		attributes.addFlashAttribute("mensagem", "ativo patrimonial cadastrado com sucesso");
		attributes.addFlashAttribute("tipoMensagem", "sucesso");
		return "redirect:/ativos";
	}

	@GetMapping("/ativos/editar/{id}")
	public String editarAtivo(@PathVariable Long id, Model model, HttpSession session) {
		if (!estaLogado(session)) {
			return "redirect:/login";
		}
		AtivoPatrimonial ativo = buscarAtivo(id);
		ativo.setCategoriaId(ativo.getCategoria().getId());
		prepararFormulario(model, ativo, "editar ativo patrimonial", "/ativos/atualizar/" + id);
		return "ativos/form";
	}

	@PostMapping("/ativos/atualizar/{id}")
	@Transactional
	public String atualizarAtivo(@PathVariable Long id,
			@Valid @ModelAttribute("ativoPatrimonial") AtivoPatrimonial ativoForm, BindingResult result, Model model,
			RedirectAttributes attributes, HttpSession session) {
		if (!estaLogado(session)) {
			return "redirect:/login";
		}

		AtivoPatrimonial ativo = buscarAtivo(id);
		ativoForm.setId(id);
		prepararAtivo(ativoForm);
		validarNumeroPatrimonial(ativoForm, result);
		Categoria categoria = buscarCategoria(ativoForm.getCategoriaId(), result);
		if (result.hasErrors()) {
			prepararFormulario(model, ativoForm, "editar ativo patrimonial", "/ativos/atualizar/" + id);
			return "ativos/form";
		}

		ativo.setNome(ativoForm.getNome());
		ativo.setDescricao(ativoForm.getDescricao());
		ativo.setNumeroPatrimonio(ativoForm.getNumeroPatrimonio());
		ativo.setLocalizacao(ativoForm.getLocalizacao());
		ativo.setStatus(ativoForm.getStatus());
		ativo.setCategoria(categoria);
		ativoPatrimonialRepository.save(ativo);

		attributes.addFlashAttribute("mensagem", "ativo patrimonial atualizado com sucesso");
		attributes.addFlashAttribute("tipoMensagem", "sucesso");
		return "redirect:/ativos";
	}

	@PostMapping("/ativos/excluir/{id}")
	@Transactional
	public String excluirAtivo(@PathVariable Long id, RedirectAttributes attributes, HttpSession session) {
		if (!estaLogado(session)) {
			return "redirect:/login";
		}

		ativoPatrimonialRepository.delete(buscarAtivo(id));
		attributes.addFlashAttribute("mensagem", "ativo patrimonial excluido com sucesso");
		attributes.addFlashAttribute("tipoMensagem", "sucesso");
		return "redirect:/ativos";
	}

	private AtivoPatrimonial buscarAtivo(Long id) {
		return ativoPatrimonialRepository.findById(id)
				.orElseThrow(() -> new IllegalArgumentException("ativo patrimonial nao encontrado"));
	}

	private Categoria buscarCategoria(Long categoriaId, BindingResult result) {
		if (categoriaId == null) {
			result.rejectValue("categoriaId", "ativoPatrimonial.categoriaId", "selecione uma categoria");
			return null;
		}

		return categoriaRepository.findById(categoriaId)
				.orElseGet(() -> {
					result.rejectValue("categoriaId", "ativoPatrimonial.categoriaId", "categoria nao encontrada");
					return null;
				});
	}

	private void validarNumeroPatrimonial(AtivoPatrimonial ativoPatrimonial, BindingResult result) {
		Optional<AtivoPatrimonial> ativoExistente = ativoPatrimonialRepository
				.findByNumeroPatrimonioIgnoreCase(ativoPatrimonial.getNumeroPatrimonio());

		if (ativoExistente.isPresent()) {
			Long idAtual = ativoPatrimonial.getId();
			Long idExistente = ativoExistente.get().getId();
			if (idAtual == null || !idAtual.equals(idExistente)) {
				result.rejectValue("numeroPatrimonio", "ativoPatrimonial.numeroPatrimonio",
						"ja existe um ativo com este numero patrimonial");
			}
		}
	}

	private void prepararAtivo(AtivoPatrimonial ativoPatrimonial) {
		ativoPatrimonial.setNome(limparTexto(ativoPatrimonial.getNome()));
		ativoPatrimonial.setDescricao(limparTexto(ativoPatrimonial.getDescricao()));
		ativoPatrimonial.setNumeroPatrimonio(limparTexto(ativoPatrimonial.getNumeroPatrimonio()));
		ativoPatrimonial.setLocalizacao(limparTexto(ativoPatrimonial.getLocalizacao()));
		ativoPatrimonial.setStatus(limparTexto(ativoPatrimonial.getStatus()));
	}

	private void prepararFormulario(Model model, AtivoPatrimonial ativoPatrimonial, String titulo, String acao) {
		List<String> statusDisponiveis = List.of("em uso", "em estoque", "em manutencao", "baixado");
		model.addAttribute("ativoPatrimonial", ativoPatrimonial);
		model.addAttribute("titulo", titulo);
		model.addAttribute("acao", acao);
		model.addAttribute("categorias", categoriaRepository.findAllByOrderByNomeAsc());
		model.addAttribute("statusDisponiveis", statusDisponiveis);
	}
}
