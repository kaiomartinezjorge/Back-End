package br.senai.estoque.gerenciamento_estoque.model;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "ativos_patrimoniais")
public class AtivoPatrimonial {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@NotBlank(message = "informe o nome do ativo")
	@Size(max = 120, message = "o nome pode ter no maximo 120 caracteres")
	@Column(nullable = false, length = 120)
	private String nome;

	@NotBlank(message = "informe a descricao do ativo")
	@Size(max = 255, message = "a descricao pode ter no maximo 255 caracteres")
	@Column(nullable = false, length = 255)
	private String descricao;

	@NotBlank(message = "informe o numero patrimonial")
	@Size(max = 40, message = "o numero patrimonial pode ter no maximo 40 caracteres")
	@Column(nullable = false, unique = true, length = 40)
	private String numeroPatrimonio;

	@NotBlank(message = "informe a localizacao")
	@Size(max = 120, message = "a localizacao pode ter no maximo 120 caracteres")
	@Column(nullable = false, length = 120)
	private String localizacao;

	@NotBlank(message = "informe o status do ativo")
	@Size(max = 40, message = "o status pode ter no maximo 40 caracteres")
	@Column(nullable = false, length = 40)
	private String status;

	@NotNull(message = "selecione uma categoria")
	@ManyToOne
	@JoinColumn(name = "categoria_id", nullable = false)
	private Categoria categoria;

	@NotNull(message = "o ativo precisa de um funcionario responsavel")
	@ManyToOne
	@JoinColumn(name = "cadastrado_por_id", nullable = false)
	private Funcionario cadastradoPor;

	@Column(nullable = false)
	private LocalDateTime dataCadastro;

	@PrePersist
	public void prepararDataCadastro() {
		if (dataCadastro == null) {
			dataCadastro = LocalDateTime.now();
		}
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

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

	public Categoria getCategoria() {
		return categoria;
	}

	public void setCategoria(Categoria categoria) {
		this.categoria = categoria;
	}

	public Funcionario getCadastradoPor() {
		return cadastradoPor;
	}

	public void setCadastradoPor(Funcionario cadastradoPor) {
		this.cadastradoPor = cadastradoPor;
	}

	public LocalDateTime getDataCadastro() {
		return dataCadastro;
	}

	public void setDataCadastro(LocalDateTime dataCadastro) {
		this.dataCadastro = dataCadastro;
	}
}
