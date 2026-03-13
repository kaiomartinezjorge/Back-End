package br.senai.estoque.gerenciamento_estoque.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import br.senai.estoque.gerenciamento_estoque.model.Movimentacao;

public interface MovimentacaoRepository extends JpaRepository<Movimentacao, Long> {

	List<Movimentacao> findAllByOrderByDataDesc();

	boolean existsByMaterial_Id(Long materialId);
}
