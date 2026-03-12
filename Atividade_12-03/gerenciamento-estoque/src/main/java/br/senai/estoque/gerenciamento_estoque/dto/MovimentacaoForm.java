package br.senai.estoque.gerenciamento_estoque.dto;

import br.senai.estoque.gerenciamento_estoque.model.TipoMovimentacao;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class MovimentacaoForm {

	@NotNull(message = "selecione um material")
	private Long materialId;

	@NotNull(message = "selecione o tipo de movimentacao")
	private TipoMovimentacao tipo;

	@Min(value = 1, message = "a quantidade deve ser maior que zero")
	private int quantidade;

	@Size(max = 255, message = "a observacao pode ter no maximo 255 caracteres")
	private String observacao;

	public Long getMaterialId() {
		return materialId;
	}

	public void setMaterialId(Long materialId) {
		this.materialId = materialId;
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
}
