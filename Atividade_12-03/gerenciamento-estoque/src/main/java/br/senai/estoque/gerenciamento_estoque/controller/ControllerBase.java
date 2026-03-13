package br.senai.estoque.gerenciamento_estoque.controller;

import org.springframework.beans.factory.annotation.Autowired;

import br.senai.estoque.gerenciamento_estoque.model.Funcionario;
import br.senai.estoque.gerenciamento_estoque.repository.FuncionarioRepository;
import jakarta.servlet.http.HttpSession;

public abstract class ControllerBase {

	@Autowired
	private FuncionarioRepository funcionarioRepository;

	protected void iniciarSessao(HttpSession session, Funcionario funcionario) {
		session.setAttribute("funcionarioLogadoId", funcionario.getId());
		session.setAttribute("funcionarioLogadoNome", funcionario.getNome());
		session.setAttribute("funcionarioLogadoNif", funcionario.getNif());
	}

	protected boolean estaLogado(HttpSession session) {
		return session != null && session.getAttribute("funcionarioLogadoId") != null;
	}

	protected Funcionario buscarFuncionarioLogado(HttpSession session) {
		if (!estaLogado(session)) {
			return null;
		}

		Object id = session.getAttribute("funcionarioLogadoId");
		Long funcionarioId = Long.valueOf(id.toString());

		return funcionarioRepository.findById(funcionarioId).orElse(null);
	}

	protected void encerrarSessao(HttpSession session) {
		if (session != null) {
			session.invalidate();
		}
	}

	protected String limparTexto(String texto) {
		return texto == null ? "" : texto.trim();
	}

	protected String limparTextoOpcional(String texto) {
		if (texto == null || texto.trim().isEmpty()) {
			return null;
		}

		return texto.trim();
	}
}
