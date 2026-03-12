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
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "funcionarios")
public class Funcionario {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@NotBlank(message = "informe o nome do funcionario")
	@Size(max = 120, message = "o nome pode ter no maximo 120 caracteres")
	@Column(nullable = false, length = 120)
	private String nome;

	@NotBlank(message = "informe o nif")
	@Size(max = 20, message = "o nif pode ter no maximo 20 caracteres")
	@Column(nullable = false, unique = true, length = 20)
	private String nif;

	@NotBlank(message = "informe a senha")
	@Size(min = 4, max = 120, message = "a senha deve ter entre 4 e 120 caracteres")
	@Column(nullable = false)
	private String senha;

	@Column(nullable = false)
	private boolean ativo = true;

	@JsonIgnore
	@OneToMany(mappedBy = "criadoPor", cascade = CascadeType.ALL)
	private List<Materiais> materiaisCriados = new ArrayList<>();

	@JsonIgnore
	@OneToMany(mappedBy = "funcionario", cascade = CascadeType.ALL)
	private List<Movimentacao> movimentacoes = new ArrayList<>();

	@JsonIgnore
	@OneToMany(mappedBy = "cadastradoPor", cascade = CascadeType.ALL)
	private List<AtivoPatrimonial> ativosCadastrados = new ArrayList<>();

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

	public boolean isAtivo() {
		return ativo;
	}

	public void setAtivo(boolean ativo) {
		this.ativo = ativo;
	}

	public List<Materiais> getMateriaisCriados() {
		return materiaisCriados;
	}

	public void setMateriaisCriados(List<Materiais> materiaisCriados) {
		this.materiaisCriados = materiaisCriados;
	}

	public List<Movimentacao> getMovimentacoes() {
		return movimentacoes;
	}

	public void setMovimentacoes(List<Movimentacao> movimentacoes) {
		this.movimentacoes = movimentacoes;
	}

	public List<AtivoPatrimonial> getAtivosCadastrados() {
		return ativosCadastrados;
	}

	public void setAtivosCadastrados(List<AtivoPatrimonial> ativosCadastrados) {
		this.ativosCadastrados = ativosCadastrados;
	}
}
