package br.senai.estoque.gerenciamento_estoque.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "funcionarios_autenticados")
public class FuncionarioAutenticado {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@NotBlank(message = "informe o nome do funcionario autorizado")
	@Size(max = 120, message = "o nome pode ter no maximo 120 caracteres")
	@Column(nullable = false, length = 120)
	private String nome;

	@NotBlank(message = "informe o nif autorizado")
	@Size(max = 20, message = "o nif pode ter no maximo 20 caracteres")
	@Column(nullable = false, unique = true, length = 20)
	private String nif;

	@Column(nullable = false)
	private boolean ativo = true;

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

	public boolean isAtivo() {
		return ativo;
	}

	public void setAtivo(boolean ativo) {
		this.ativo = ativo;
	}
}
