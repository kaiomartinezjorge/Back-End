package br.senai.estoque.gerenciamento_estoque.model;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name = "movimentacoes")
public class Movimentacao {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false)
	private LocalDateTime data;

	@ManyToOne
	@JoinColumn(name = "material_id", nullable = false)
	private Materiais material;

	@ManyToOne
	@JoinColumn(name = "funcionario_id", nullable = false)
	private Funcionario funcionario;

	@NotNull(message = "selecione o tipo de movimentacao")
	@Enumerated(EnumType.STRING)
	@Column(nullable = false, length = 20)
	private TipoMovimentacao tipo;

	@Min(value = 1, message = "a quantidade deve ser maior que zero")
	@Column(nullable = false)
	private int quantidade;

	@Size(max = 255, message = "a observacao pode ter no maximo 255 caracteres")
	@Column(length = 255)
	private String observacao;

	@Transient
	private Long materialId;

	@Transient
	private String materialNome;

	@PrePersist
	public void prepararData() {
		if (data == null) {
			data = LocalDateTime.now();
		}
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public LocalDateTime getData() {
		return data;
	}

	public void setData(LocalDateTime data) {
		this.data = data;
	}

	public Materiais getMaterial() {
		return material;
	}

	public void setMaterial(Materiais material) {
		this.material = material;
	}

	public Funcionario getFuncionario() {
		return funcionario;
	}

	public void setFuncionario(Funcionario funcionario) {
		this.funcionario = funcionario;
	}

	public TipoMovimentacao getTipo() {
		return tipo;
	}

	public void setTipo(TipoMovimentacao tipo) {
		this.tipo = tipo;
	}

	public int getQuantidade() {
		return quantidade;
	}

	public void setQuantidade(int quantidade) {
		this.quantidade = quantidade;
	}

	public String getObservacao() {
		return observacao;
	}

	public void setObservacao(String observacao) {
		this.observacao = observacao;
	}

	public Long getMaterialId() {
		return materialId;
	}

	public void setMaterialId(Long materialId) {
		this.materialId = materialId;
	}

	public String getMaterialNome() {
		return materialNome;
	}

	public void setMaterialNome(String materialNome) {
		this.materialNome = materialNome;
	}
}
