package br.senai.estoque.gerenciamento_estoque.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class FuncionarioCadastroForm {

	@NotBlank(message = "informe o nome")
	@Size(max = 120, message = "o nome pode ter no maximo 120 caracteres")
	private String nome;

	@NotBlank(message = "informe o nif")
	@Size(max = 20, message = "o nif pode ter no maximo 20 caracteres")
	private String nif;

	@NotBlank(message = "informe a senha")
	@Size(min = 4, max = 120, message = "a senha deve ter entre 4 e 120 caracteres")
	private String senha;

	@NotBlank(message = "confirme a senha")
	@Size(min = 4, max = 120, message = "a confirmacao deve ter entre 4 e 120 caracteres")
	private String confirmarSenha;

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getNif() {
		return nif;
	}

	public void setNif(String nif) {
		this.nif = nif;
	}

	public String getSenha() {
		return senha;
	}

	public void setSenha(String senha) {
		this.senha = senha;
	}

	public String getConfirmarSenha() {
		return confirmarSenha;
	}

	public void setConfirmarSenha(String confirmarSenha) {
		this.confirmarSenha = confirmarSenha;
	}
}
