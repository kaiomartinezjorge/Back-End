package br.senai.estoque.gerenciamento_estoque.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import br.senai.estoque.gerenciamento_estoque.dto.FuncionarioCadastroForm;
import br.senai.estoque.gerenciamento_estoque.dto.LoginForm;
import br.senai.estoque.gerenciamento_estoque.model.Funcionario;
import br.senai.estoque.gerenciamento_estoque.service.AuthService;
import br.senai.estoque.gerenciamento_estoque.service.SessaoService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

@Controller
public class AuthController {

	@Autowired
	private AuthService authService;

	@Autowired
	private SessaoService sessaoService;

	@GetMapping("/login")
	public String acessarLogin(Model model, HttpSession session) {
		if (sessaoService.estaLogado(session)) {
			return "redirect:/interna";
		}

		model.addAttribute("loginForm", new LoginForm());
		return "auth/login";
	}

	@PostMapping("/login")
	public String fazerLogin(@Valid @ModelAttribute("loginForm") LoginForm loginForm, BindingResult result,
			RedirectAttributes attributes, HttpSession session) {
		if (result.hasErrors()) {
			return "auth/login";
		}

		try {
			Funcionario funcionario = authService.autenticar(loginForm.getNif(), loginForm.getSenha());
			sessaoService.iniciarSessao(session, funcionario);
			attributes.addFlashAttribute("mensagem", "login realizado com sucesso");
			attributes.addFlashAttribute("tipoMensagem", "sucesso");
			return "redirect:/interna";
		} catch (IllegalArgumentException ex) {
			result.reject("login.invalido", ex.getMessage());
			return "auth/login";
		}
	}

	@GetMapping("/cadastro")
	public String acessarCadastro(Model model, HttpSession session) {
		if (sessaoService.estaLogado(session)) {
			return "redirect:/interna";
		}

		model.addAttribute("funcionarioCadastroForm", new FuncionarioCadastroForm());
		return "auth/cadastro";
	}

	@PostMapping("/cadastro")
	public String fazerCadastro(@Valid @ModelAttribute("funcionarioCadastroForm") FuncionarioCadastroForm cadastroForm,
			BindingResult result, RedirectAttributes attributes) {
		if (result.hasErrors()) {
			return "auth/cadastro";
		}

		try {
			authService.cadastrar(cadastroForm);
			attributes.addFlashAttribute("mensagem", "cadastro realizado com sucesso");
			attributes.addFlashAttribute("tipoMensagem", "sucesso");
			return "redirect:/login";
		} catch (IllegalArgumentException ex) {
			result.reject("cadastro.invalido", ex.getMessage());
			return "auth/cadastro";
		}
	}

	@GetMapping("/logout")
	public String logout(HttpSession session, RedirectAttributes attributes) {
		sessaoService.encerrarSessao(session);
		attributes.addFlashAttribute("mensagem", "sessao encerrada com sucesso");
		attributes.addFlashAttribute("tipoMensagem", "sucesso");
		return "redirect:/login";
	}
}
