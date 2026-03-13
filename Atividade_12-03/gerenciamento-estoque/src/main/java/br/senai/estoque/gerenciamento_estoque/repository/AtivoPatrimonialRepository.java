package br.senai.estoque.gerenciamento_estoque.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import br.senai.estoque.gerenciamento_estoque.model.AtivoPatrimonial;

public interface AtivoPatrimonialRepository extends JpaRepository<AtivoPatrimonial, Long> {

	List<AtivoPatrimonial> findAllByOrderByNomeAsc();

	boolean existsByCategoria_Id(Long categoriaId);

	boolean existsByNumeroPatrimonioIgnoreCase(String numeroPatrimonio);

	Optional<AtivoPatrimonial> findByNumeroPatrimonioIgnoreCase(String numeroPatrimonio);
}
