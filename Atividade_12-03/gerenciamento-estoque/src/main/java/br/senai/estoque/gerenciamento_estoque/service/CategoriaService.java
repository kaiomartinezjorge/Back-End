package br.senai.estoque.gerenciamento_estoque.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.senai.estoque.gerenciamento_estoque.model.Categoria;
import br.senai.estoque.gerenciamento_estoque.repository.AtivoPatrimonialRepository;
import br.senai.estoque.gerenciamento_estoque.repository.CategoriaRepository;
import br.senai.estoque.gerenciamento_estoque.repository.MateriaisRepository;

@Service
public class CategoriaService {

	@Autowired
	private CategoriaRepository categoriaRepository;

	@Autowired
	private MateriaisRepository materiaisRepository;

	@Autowired
	private AtivoPatrimonialRepository ativoPatrimonialRepository;

	public List<Categoria> listarTodas() {
		return categoriaRepository.findAllByOrderByNomeAsc();
	}

	public Categoria buscarPorId(Long id) {
		return categoriaRepository.findById(id)
				.orElseThrow(() -> new IllegalArgumentException("categoria nao encontrada"));
	}

	@Transactional
	public Categoria salvar(Categoria categoria) {
		prepararCategoria(categoria);
		validarNomeDuplicado(categoria);
		return categoriaRepository.save(categoria);
	}

	@Transactional
	public Categoria atualizar(Long id, Categoria categoriaForm) {
		Categoria categoria = buscarPorId(id);
		categoria.setNome(limparTexto(categoriaForm.getNome()));
		categoria.setDescricao(limparTextoOpcional(categoriaForm.getDescricao()));
		validarNomeDuplicado(categoria);
		return categoriaRepository.save(categoria);
	}

	@Transactional
	public void excluir(Long id) {
		Categoria categoria = buscarPorId(id);

		if (materiaisRepository.existsByCategoriaId(id) || ativoPatrimonialRepository.existsByCategoriaId(id)) {
			throw new IllegalArgumentException("a categoria esta em uso e nao pode ser excluida");
		}

		categoriaRepository.delete(categoria);
	}

	public long contar() {
		return categoriaRepository.count();
	}

	private void prepararCategoria(Categoria categoria) {
		categoria.setNome(limparTexto(categoria.getNome()));
		categoria.setDescricao(limparTextoOpcional(categoria.getDescricao()));
	}

	private void validarNomeDuplicado(Categoria categoria) {
		Optional<Categoria> categoriaExistente = categoriaRepository.findByNomeIgnoreCase(categoria.getNome());

		if (categoriaExistente.isPresent()) {
			Long idAtual = categoria.getId();
			Long idExistente = categoriaExistente.get().getId();

			if (idAtual == null || !idAtual.equals(idExistente)) {
				throw new IllegalArgumentException("ja existe uma categoria com este nome");
			}
		}
	}

	private String limparTexto(String texto) {
		return texto == null ? "" : texto.trim();
	}

	private String limparTextoOpcional(String texto) {
		if (texto == null || texto.trim().isEmpty()) {
			return null;
		}

		return texto.trim();
	}
}
