package br.senai.estoque.gerenciamento_estoque.model;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "materiais")
public class Materiais {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@NotBlank(message = "informe o nome do material")
	@Size(max = 120, message = "o nome pode ter no maximo 120 caracteres")
	@Column(nullable = false, length = 120)
	private String nome;

	@NotBlank(message = "informe a descricao do material")
	@Size(max = 255, message = "a descricao pode ter no maximo 255 caracteres")
	@Column(nullable = false, length = 255)
	private String descricao;

	@PositiveOrZero(message = "a quantidade nao pode ser negativa")
	@Column(nullable = false)
	private int quantidade = 0;

	@NotNull(message = "selecione uma categoria")
	@ManyToOne
	@JoinColumn(name = "categoria_id", nullable = false)
	private Categoria categoria;

	@NotNull(message = "o material precisa de um funcionario responsavel")
	@ManyToOne
	@JoinColumn(name = "criado_por_id", nullable = false)
	private Funcionario criadoPor;

	@JsonIgnore
	@OneToMany(mappedBy = "material", cascade = CascadeType.ALL)
	private List<Movimentacao> movimentacoes = new ArrayList<>();

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

	public int getQuantidade() {
		return quantidade;
	}

	public void setQuantidade(int quantidade) {
		this.quantidade = quantidade;
	}

	public Categoria getCategoria() {
		return categoria;
	}

	public void setCategoria(Categoria categoria) {
		this.categoria = categoria;
	}

	public Funcionario getCriadoPor() {
		return criadoPor;
	}

	public void setCriadoPor(Funcionario criadoPor) {
		this.criadoPor = criadoPor;
	}

	public List<Movimentacao> getMovimentacoes() {
		return movimentacoes;
	}

	public void setMovimentacoes(List<Movimentacao> movimentacoes) {
		this.movimentacoes = movimentacoes;
	}
}
