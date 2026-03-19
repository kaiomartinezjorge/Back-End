package br.senai.estoque.gerenciamento_estoque.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import br.senai.estoque.gerenciamento_estoque.model.Funcionario;
import br.senai.estoque.gerenciamento_estoque.repository.FuncionarioAutenticadoRepository;
import br.senai.estoque.gerenciamento_estoque.repository.FuncionarioRepository;
import jakarta.servlet.http.HttpSession;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;

@Controller
public class AuthController extends ControllerBase {

	@Autowired
	private FuncionarioRepository funcionarioRepository;

	@Autowired
	private FuncionarioAutenticadoRepository funcionarioAutenticadoRepository;

	@GetMapping("/login")
	public String acessarLogin(Model model, HttpSession session) {
		if (estaLogado(session)) {
			return "redirect:/interna";
		}

		model.addAttribute("funcionario", new Funcionario());
		return "auth/login";
	}

	@PostMapping("/login")
	public String fazerLogin(@ModelAttribute("funcionario") Funcionario funcionario, BindingResult result,
			RedirectAttributes attributes, HttpSession session) {
		String nif = limparTexto(funcionario.getNif());
		String senha = limparTexto(funcionario.getSenha());

		if (nif.isEmpty()) {
			result.rejectValue("nif", "funcionario.nif", "informe o nif");
		}

		if (senha.isEmpty()) {
			result.rejectValue("senha", "funcionario.senha", "informe a senha");
		}

		if (result.hasErrors()) {
			return "auth/login";
		}

		Funcionario funcionarioEncontrado = funcionarioRepository.findByNifAndAtivoTrue(nif)
				.orElse(null);

		if (funcionarioEncontrado == null || !funcionarioEncontrado.getSenha().equals(senha)) {
			result.reject("login.invalido", "nif ou senha invalidos");
			return "auth/login";
		}

		iniciarSessao(session, funcionarioEncontrado);
		attributes.addFlashAttribute("mensagem", "login realizado com sucesso");
		attributes.addFlashAttribute("tipoMensagem", "sucesso");
		return "redirect:/interna";
	}

	@GetMapping("/cadastro")
	public String acessarCadastro(Model model, HttpSession session) {
		if (estaLogado(session)) {
			return "redirect:/interna";
		}

		model.addAttribute("funcionario", new Funcionario());
		return "auth/cadastro";
	}

	@PostMapping("/cadastro")
	@Transactional
	public String fazerCadastro(@Valid @ModelAttribute("funcionario") Funcionario funcionario, BindingResult result,
			RedirectAttributes attributes) {
		funcionario.setNome(limparTexto(funcionario.getNome()));
		funcionario.setNif(limparTexto(funcionario.getNif()));
		funcionario.setSenha(limparTexto(funcionario.getSenha()));
		funcionario.setConfirmarSenha(limparTexto(funcionario.getConfirmarSenha()));

		if (funcionario.getConfirmarSenha().isEmpty()) {
			result.rejectValue("confirmarSenha", "funcionario.confirmarSenha", "confirme a senha");
		}

		if (!funcionario.getSenha().equals(funcionario.getConfirmarSenha())) {
			result.rejectValue("confirmarSenha", "funcionario.confirmarSenha", "as senhas nao conferem");
		}

		if (!funcionarioAutenticadoRepository.existsByNifAndNomeIgnoreCaseAndAtivoTrue(funcionario.getNif(),
				funcionario.getNome())) {
			result.reject("cadastro.invalido", "cadastro nao autorizado para este funcionario");
		}

		if (funcionarioRepository.existsByNif(funcionario.getNif())) {
			result.rejectValue("nif", "funcionario.nif", "ja existe um cadastro com este nif");
		}

		if (result.hasErrors()) {
			return "auth/cadastro";
		}

		funcionario.setAtivo(true);
		funcionarioRepository.save(funcionario);
		attributes.addFlashAttribute("mensagem", "cadastro realizado com sucesso");
		attributes.addFlashAttribute("tipoMensagem", "sucesso");
		return "redirect:/login";
	}

	@GetMapping("/logout")
	public String logout(HttpSession session, RedirectAttributes attributes) {
		encerrarSessao(session);
		attributes.addFlashAttribute("mensagem", "sessao encerrada com sucesso");
		attributes.addFlashAttribute("tipoMensagem", "sucesso");
		return "redirect:/login";
	}
}
