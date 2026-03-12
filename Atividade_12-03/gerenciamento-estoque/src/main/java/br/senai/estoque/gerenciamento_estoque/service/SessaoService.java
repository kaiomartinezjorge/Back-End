package br.senai.estoque.gerenciamento_estoque.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.senai.estoque.gerenciamento_estoque.model.Funcionario;
import br.senai.estoque.gerenciamento_estoque.repository.FuncionarioRepository;
import jakarta.servlet.http.HttpSession;

@Service
public class SessaoService {

	@Autowired
	private FuncionarioRepository funcionarioRepository;

	public void iniciarSessao(HttpSession session, Funcionario funcionario) {
		session.setAttribute("funcionarioLogadoId", funcionario.getId());
		session.setAttribute("funcionarioLogadoNome", funcionario.getNome());
		session.setAttribute("funcionarioLogadoNif", funcionario.getNif());
	}

	public boolean estaLogado(HttpSession session) {
		return session != null && session.getAttribute("funcionarioLogadoId") != null;
	}

	public Funcionario buscarFuncionarioLogado(HttpSession session) {
		if (!estaLogado(session)) {
			return null;
		}

		Object id = session.getAttribute("funcionarioLogadoId");
		Long funcionarioId = Long.valueOf(id.toString());

		return funcionarioRepository.findById(funcionarioId)
				.orElse(null);
	}

	public void encerrarSessao(HttpSession session) {
		if (session != null) {
			session.invalidate();
		}
	}
}
