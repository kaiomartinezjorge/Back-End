package br.senai.estoque.gerenciamento_estoque.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class MaterialForm {

	@NotBlank(message = "informe o nome do material")
	@Size(max = 120, message = "o nome pode ter no maximo 120 caracteres")
	private String nome;

	@NotBlank(message = "informe a descricao do material")
	@Size(max = 255, message = "a descricao pode ter no maximo 255 caracteres")
	private String descricao;

	@NotNull(message = "selecione uma categoria")
	private Long categoriaId;

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public Long getCategoriaId() {
		return categoriaId;
	}

	public void setCategoriaId(Long categoriaId) {
		this.categoriaId = categoriaId;
	}
}
