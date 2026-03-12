package br.senai.estoque.gerenciamento_estoque.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class LoginForm {

	@NotBlank(message = "informe o nif")
	@Size(max = 20, message = "o nif pode ter no maximo 20 caracteres")
	private String nif;

	@NotBlank(message = "informe a senha")
	@Size(max = 120, message = "a senha pode ter no maximo 120 caracteres")
	private String senha;

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
}
