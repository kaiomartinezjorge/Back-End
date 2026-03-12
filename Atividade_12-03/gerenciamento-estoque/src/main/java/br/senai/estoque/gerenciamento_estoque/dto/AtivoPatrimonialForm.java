package br.senai.estoque.gerenciamento_estoque.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class AtivoPatrimonialForm {

	@NotBlank(message = "informe o nome do ativo")
	@Size(max = 120, message = "o nome pode ter no maximo 120 caracteres")
	private String nome;

	@NotBlank(message = "informe a descricao do ativo")
	@Size(max = 255, message = "a descricao pode ter no maximo 255 caracteres")
	private String descricao;

	@NotBlank(message = "informe o numero patrimonial")
	@Size(max = 40, message = "o numero patrimonial pode ter no maximo 40 caracteres")
	private String numeroPatrimonio;

	@NotBlank(message = "informe a localizacao")
	@Size(max = 120, message = "a localizacao pode ter no maximo 120 caracteres")
	private String localizacao;

	@NotBlank(message = "informe o status do ativo")
	@Size(max = 40, message = "o status pode ter no maximo 40 caracteres")
	private String status;

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

	public String getNumeroPatrimonio() {
		return numeroPatrimonio;
	}

	public void setNumeroPatrimonio(String numeroPatrimonio) {
		this.numeroPatrimonio = numeroPatrimonio;
	}

	public String getLocalizacao() {
		return localizacao;
	}

	public void setLocalizacao(String localizacao) {
		this.localizacao = localizacao;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Long getCategoriaId() {
		return categoriaId;
	}

	public void setCategoriaId(Long categoriaId) {
		this.categoriaId = categoriaId;
	}
}
