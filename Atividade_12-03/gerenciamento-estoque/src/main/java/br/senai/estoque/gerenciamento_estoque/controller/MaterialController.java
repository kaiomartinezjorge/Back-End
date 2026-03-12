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

import br.senai.estoque.gerenciamento_estoque.dto.MaterialForm;
import br.senai.estoque.gerenciamento_estoque.model.Funcionario;
import br.senai.estoque.gerenciamento_estoque.model.Materiais;
import br.senai.estoque.gerenciamento_estoque.service.CategoriaService;
import br.senai.estoque.gerenciamento_estoque.service.MaterialService;
import br.senai.estoque.gerenciamento_estoque.service.SessaoService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

@Controller
public class MaterialController {

	@Autowired
	private MaterialService materialService;

	@Autowired
	private CategoriaService categoriaService;

	@Autowired
	private SessaoService sessaoService;

	@GetMapping("/materiais")
	public String listarMateriais(Model model, HttpSession session) {
		if (!sessaoService.estaLogado(session)) {
			return "redirect:/login";
		}
		model.addAttribute("materiais", materialService.listarTodos());
		return "materiais/lista";
	}

	@GetMapping("/materiais/novo")
	public String novoMaterial(Model model, HttpSession session) {
		if (!sessaoService.estaLogado(session)) {
			return "redirect:/login";
		}
		prepararFormulario(model, new MaterialForm(), "novo material", "/materiais/salvar", 0);
		return "materiais/form";
	}

	@PostMapping("/materiais/salvar")
	public String salvarMaterial(@Valid @ModelAttribute("materialForm") MaterialForm materialForm,
			BindingResult result, Model model, RedirectAttributes attributes, HttpSession session) {
		if (!sessaoService.estaLogado(session)) {
			return "redirect:/login";
		}
		if (result.hasErrors()) {
			prepararFormulario(model, materialForm, "novo material", "/materiais/salvar", 0);
			return "materiais/form";
		}

		try {
			Funcionario funcionario = sessaoService.buscarFuncionarioLogado(session);
			materialService.salvar(materialForm, funcionario);
			attributes.addFlashAttribute("mensagem", "material cadastrado com sucesso");
			attributes.addFlashAttribute("tipoMensagem", "sucesso");
			return "redirect:/materiais";
		} catch (IllegalArgumentException ex) {
			result.reject("material.invalido", ex.getMessage());
			prepararFormulario(model, materialForm, "novo material", "/materiais/salvar", 0);
			return "materiais/form";
		}
	}

	@GetMapping("/materiais/editar/{id}")
	public String editarMaterial(@PathVariable Long id, Model model, HttpSession session) {
		if (!sessaoService.estaLogado(session)) {
			return "redirect:/login";
		}
		Materiais material = materialService.buscarPorId(id);
		MaterialForm materialForm = new MaterialForm();
		materialForm.setNome(material.getNome());
		materialForm.setDescricao(material.getDescricao());
		materialForm.setCategoriaId(material.getCategoria().getId());

		prepararFormulario(model, materialForm, "editar material", "/materiais/atualizar/" + id,
				material.getQuantidade());
		return "materiais/form";
	}

	@PostMapping("/materiais/atualizar/{id}")
	public String atualizarMaterial(@PathVariable Long id, @Valid @ModelAttribute("materialForm") MaterialForm materialForm,
			BindingResult result, Model model, RedirectAttributes attributes, HttpSession session) {
		if (!sessaoService.estaLogado(session)) {
			return "redirect:/login";
		}
		Materiais material = materialService.buscarPorId(id);

		if (result.hasErrors()) {
			prepararFormulario(model, materialForm, "editar material", "/materiais/atualizar/" + id,
					material.getQuantidade());
			return "materiais/form";
		}

		try {
			materialService.atualizar(id, materialForm);
			attributes.addFlashAttribute("mensagem", "material atualizado com sucesso");
			attributes.addFlashAttribute("tipoMensagem", "sucesso");
			return "redirect:/materiais";
		} catch (IllegalArgumentException ex) {
			result.reject("material.invalido", ex.getMessage());
			prepararFormulario(model, materialForm, "editar material", "/materiais/atualizar/" + id,
					material.getQuantidade());
			return "materiais/form";
		}
	}

	@PostMapping("/materiais/excluir/{id}")
	public String excluirMaterial(@PathVariable Long id, RedirectAttributes attributes, HttpSession session) {
		if (!sessaoService.estaLogado(session)) {
			return "redirect:/login";
		}
		try {
			materialService.excluir(id);
			attributes.addFlashAttribute("mensagem", "material excluido com sucesso");
			attributes.addFlashAttribute("tipoMensagem", "sucesso");
		} catch (IllegalArgumentException ex) {
			attributes.addFlashAttribute("mensagem", ex.getMessage());
			attributes.addFlashAttribute("tipoMensagem", "erro");
		}

		return "redirect:/materiais";
	}

	private void prepararFormulario(Model model, MaterialForm materialForm, String titulo, String acao,
			int quantidadeAtual) {
		model.addAttribute("materialForm", materialForm);
		model.addAttribute("titulo", titulo);
		model.addAttribute("acao", acao);
		model.addAttribute("quantidadeAtual", quantidadeAtual);
		model.addAttribute("categorias", categoriaService.listarTodas());
	}
}
