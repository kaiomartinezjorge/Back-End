package br.senai.estoque.gerenciamento_estoque.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import br.senai.estoque.gerenciamento_estoque.model.FuncionarioAutenticado;

public interface FuncionarioAutenticadoRepository extends JpaRepository<FuncionarioAutenticado, Long> {

	List<FuncionarioAutenticado> findAllByAtivoTrueOrderByNomeAsc();

	Optional<FuncionarioAutenticado> findByNifAndAtivoTrue(String nif);

	boolean existsByNifAndNomeIgnoreCaseAndAtivoTrue(String nif, String nome);
}
