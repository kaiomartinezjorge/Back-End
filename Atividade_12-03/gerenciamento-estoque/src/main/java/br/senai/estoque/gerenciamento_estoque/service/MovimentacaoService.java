package br.senai.estoque.gerenciamento_estoque.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.senai.estoque.gerenciamento_estoque.dto.MovimentacaoForm;
import br.senai.estoque.gerenciamento_estoque.model.Funcionario;
import br.senai.estoque.gerenciamento_estoque.model.Materiais;
import br.senai.estoque.gerenciamento_estoque.model.Movimentacao;
import br.senai.estoque.gerenciamento_estoque.model.TipoMovimentacao;
import br.senai.estoque.gerenciamento_estoque.repository.MateriaisRepository;
import br.senai.estoque.gerenciamento_estoque.repository.MovimentacaoRepository;

@Service
public class MovimentacaoService {

	@Autowired
	private MovimentacaoRepository movimentacaoRepository;

	@Autowired
	private MateriaisRepository materiaisRepository;

	public List<Movimentacao> listarTodas() {
		return movimentacaoRepository.findAllByOrderByDataDesc();
	}

	public List<Movimentacao> listarRecentes(int limite) {
		return movimentacaoRepository.findAllByOrderByDataDesc()
				.stream()
				.limit(limite)
				.toList();
	}

	@Transactional
	public Movimentacao registrar(MovimentacaoForm form, Funcionario funcionario) {
		Materiais material = materiaisRepository.findById(form.getMaterialId())
				.orElseThrow(() -> new IllegalArgumentException("material nao encontrado"));

		if (form.getTipo() == TipoMovimentacao.SAIDA && material.getQuantidade() < form.getQuantidade()) {
			throw new IllegalArgumentException("estoque insuficiente para a saida informada");
		}

		if (form.getTipo() == TipoMovimentacao.ENTRADA) {
			material.setQuantidade(material.getQuantidade() + form.getQuantidade());
		} else {
			material.setQuantidade(material.getQuantidade() - form.getQuantidade());
		}

		materiaisRepository.save(material);

		Movimentacao movimentacao = new Movimentacao();
		movimentacao.setMaterial(material);
		movimentacao.setFuncionario(funcionario);
		movimentacao.setTipo(form.getTipo());
		movimentacao.setQuantidade(form.getQuantidade());
		movimentacao.setObservacao(limparTextoOpcional(form.getObservacao()));

		return movimentacaoRepository.save(movimentacao);
	}

	public long contar() {
		return movimentacaoRepository.count();
	}

	private String limparTextoOpcional(String texto) {
		if (texto == null || texto.trim().isEmpty()) {
			return null;
		}

		return texto.trim();
	}
}
