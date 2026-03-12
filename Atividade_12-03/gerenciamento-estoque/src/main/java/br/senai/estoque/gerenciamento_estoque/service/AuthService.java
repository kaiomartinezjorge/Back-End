package br.senai.estoque.gerenciamento_estoque.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.senai.estoque.gerenciamento_estoque.dto.FuncionarioCadastroForm;
import br.senai.estoque.gerenciamento_estoque.model.Funcionario;
import br.senai.estoque.gerenciamento_estoque.repository.FuncionarioAutenticadoRepository;
import br.senai.estoque.gerenciamento_estoque.repository.FuncionarioRepository;

@Service
public class AuthService {

	@Autowired
	private FuncionarioRepository funcionarioRepository;

	@Autowired
	private FuncionarioAutenticadoRepository funcionarioAutenticadoRepository;

	@Transactional
	public Funcionario cadastrar(FuncionarioCadastroForm form) {
		String nome = limparTexto(form.getNome());
		String nif = limparTexto(form.getNif());
		String senha = limparTexto(form.getSenha());
		String confirmarSenha = limparTexto(form.getConfirmarSenha());

		if (!senha.equals(confirmarSenha)) {
			throw new IllegalArgumentException("as senhas nao conferem");
		}

		if (!funcionarioAutenticadoRepository.existsByNifAndNomeIgnoreCaseAndAtivoTrue(nif, nome)) {
			throw new IllegalArgumentException("cadastro nao autorizado para este funcionario");
		}

		if (funcionarioRepository.existsByNif(nif)) {
			throw new IllegalArgumentException("ja existe um cadastro com este nif");
		}

		Funcionario funcionario = new Funcionario();
		funcionario.setNome(nome);
		funcionario.setNif(nif);
		funcionario.setSenha(senha);
		funcionario.setAtivo(true);

		return funcionarioRepository.save(funcionario);
	}

	public Funcionario autenticar(String nif, String senha) {
		String nifLimpo = limparTexto(nif);
		String senhaLimpa = limparTexto(senha);

		Funcionario funcionario = funcionarioRepository.findByNifAndAtivoTrue(nifLimpo)
				.orElseThrow(() -> new IllegalArgumentException("nif ou senha invalidos"));

		if (!funcionario.getSenha().equals(senhaLimpa)) {
			throw new IllegalArgumentException("nif ou senha invalidos");
		}

		return funcionario;
	}

	private String limparTexto(String texto) {
		return texto == null ? "" : texto.trim();
	}
}
