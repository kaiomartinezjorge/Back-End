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

import br.senai.estoque.gerenciamento_estoque.model.Categoria;
import br.senai.estoque.gerenciamento_estoque.model.Funcionario;
import br.senai.estoque.gerenciamento_estoque.model.Materiais;
import br.senai.estoque.gerenciamento_estoque.repository.CategoriaRepository;
import br.senai.estoque.gerenciamento_estoque.repository.MateriaisRepository;
import br.senai.estoque.gerenciamento_estoque.repository.MovimentacaoRepository;
import jakarta.servlet.http.HttpSession;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;

@Controller
public class MaterialController extends ControllerBase {

	@Autowired
	private MateriaisRepository materiaisRepository;

	@Autowired
	private MovimentacaoRepository movimentacaoRepository;

	@Autowired
	private CategoriaRepository categoriaRepository;

	@GetMapping("/materiais")
	public String listarMateriais(Model model, HttpSession session) {
		if (!estaLogado(session)) {
			return "redirect:/login";
		}
		model.addAttribute("materiais", materiaisRepository.findAllByOrderByNomeAsc());
		return "materiais/lista";
	}

	@GetMapping("/materiais/novo")
	public String novoMaterial(Model model, HttpSession session) {
		if (!estaLogado(session)) {
			return "redirect:/login";
		}
		prepararFormulario(model, new Materiais(), "novo material", "/materiais/salvar", 0);
		return "materiais/form";
	}

	@PostMapping("/materiais/salvar")
	@Transactional
	public String salvarMaterial(@Valid @ModelAttribute("material") Materiais material, BindingResult result,
			Model model, RedirectAttributes attributes, HttpSession session) {
		if (!estaLogado(session)) {
			return "redirect:/login";
		}

		prepararMaterial(material);
		Categoria categoria = buscarCategoria(material.getCategoriaId(), result);
		if (result.hasErrors()) {
			prepararFormulario(model, material, "novo material", "/materiais/salvar", 0);
			return "materiais/form";
		}

		Funcionario funcionario = buscarFuncionarioLogado(session);
		material.setCategoria(categoria);
		material.setCriadoPor(funcionario);
		material.setQuantidade(0);
		materiaisRepository.save(material);

		attributes.addFlashAttribute("mensagem", "material cadastrado com sucesso");
		attributes.addFlashAttribute("tipoMensagem", "sucesso");
		return "redirect:/materiais";
	}

	@GetMapping("/materiais/editar/{id}")
	public String editarMaterial(@PathVariable Long id, Model model, HttpSession session) {
		if (!estaLogado(session)) {
			return "redirect:/login";
		}
		Materiais material = buscarMaterial(id);
		material.setCategoriaId(material.getCategoria().getId());
		prepararFormulario(model, material, "editar material", "/materiais/atualizar/" + id, material.getQuantidade());
		return "materiais/form";
	}

	@PostMapping("/materiais/atualizar/{id}")
	@Transactional
	public String atualizarMaterial(@PathVariable Long id, @Valid @ModelAttribute("material") Materiais materialForm,
			BindingResult result, Model model, RedirectAttributes attributes, HttpSession session) {
		if (!estaLogado(session)) {
			return "redirect:/login";
		}

		Materiais material = buscarMaterial(id);
		prepararMaterial(materialForm);
		Categoria categoria = buscarCategoria(materialForm.getCategoriaId(), result);
		if (result.hasErrors()) {
			materialForm.setQuantidade(material.getQuantidade());
			prepararFormulario(model, materialForm, "editar material", "/materiais/atualizar/" + id, material.getQuantidade());
			return "materiais/form";
		}

		material.setNome(materialForm.getNome());
		material.setDescricao(materialForm.getDescricao());
		material.setCategoria(categoria);
		materiaisRepository.save(material);

		attributes.addFlashAttribute("mensagem", "material atualizado com sucesso");
		attributes.addFlashAttribute("tipoMensagem", "sucesso");
		return "redirect:/materiais";
	}

	@PostMapping("/materiais/excluir/{id}")
	@Transactional
	public String excluirMaterial(@PathVariable Long id, RedirectAttributes attributes, HttpSession session) {
		if (!estaLogado(session)) {
			return "redirect:/login";
		}

		if (movimentacaoRepository.existsByMaterial_Id(id)) {
			attributes.addFlashAttribute("mensagem", "o material possui movimentacoes e nao pode ser excluido");
			attributes.addFlashAttribute("tipoMensagem", "erro");
			return "redirect:/materiais";
		}

		materiaisRepository.delete(buscarMaterial(id));
		attributes.addFlashAttribute("mensagem", "material excluido com sucesso");
		attributes.addFlashAttribute("tipoMensagem", "sucesso");
		return "redirect:/materiais";
	}

	private Materiais buscarMaterial(Long id) {
		return materiaisRepository.findById(id)
				.orElseThrow(() -> new IllegalArgumentException("material nao encontrado"));
	}

	private Categoria buscarCategoria(Long categoriaId, BindingResult result) {
		if (categoriaId == null) {
			result.rejectValue("categoriaId", "material.categoriaId", "selecione uma categoria");
			return null;
		}

		return categoriaRepository.findById(categoriaId)
				.orElseGet(() -> {
					result.rejectValue("categoriaId", "material.categoriaId", "categoria nao encontrada");
					return null;
				});
	}

	private void prepararMaterial(Materiais material) {
		material.setNome(limparTexto(material.getNome()));
		material.setDescricao(limparTexto(material.getDescricao()));
	}

	private void prepararFormulario(Model model, Materiais material, String titulo, String acao, int quantidadeAtual) {
		List<Categoria> categorias = categoriaRepository.findAllByOrderByNomeAsc();
		model.addAttribute("material", material);
		model.addAttribute("titulo", titulo);
		model.addAttribute("acao", acao);
		model.addAttribute("quantidadeAtual", quantidadeAtual);
		model.addAttribute("categorias", categorias);
	}
}
