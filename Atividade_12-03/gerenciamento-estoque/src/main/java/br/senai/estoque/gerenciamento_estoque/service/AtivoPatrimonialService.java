package br.senai.estoque.gerenciamento_estoque.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.senai.estoque.gerenciamento_estoque.dto.AtivoPatrimonialForm;
import br.senai.estoque.gerenciamento_estoque.model.AtivoPatrimonial;
import br.senai.estoque.gerenciamento_estoque.model.Categoria;
import br.senai.estoque.gerenciamento_estoque.model.Funcionario;
import br.senai.estoque.gerenciamento_estoque.repository.AtivoPatrimonialRepository;

@Service
public class AtivoPatrimonialService {

	@Autowired
	private AtivoPatrimonialRepository ativoPatrimonialRepository;

	@Autowired
	private CategoriaService categoriaService;

	public List<AtivoPatrimonial> listarTodos() {
		return ativoPatrimonialRepository.findAllByOrderByNomeAsc();
	}

	public AtivoPatrimonial buscarPorId(Long id) {
		return ativoPatrimonialRepository.findById(id)
				.orElseThrow(() -> new IllegalArgumentException("ativo patrimonial nao encontrado"));
	}

	@Transactional
	public AtivoPatrimonial salvar(AtivoPatrimonialForm form, Funcionario funcionario) {
		validarNumeroPatrimonial(form.getNumeroPatrimonio(), null);
		Categoria categoria = categoriaService.buscarPorId(form.getCategoriaId());

		AtivoPatrimonial ativo = new AtivoPatrimonial();
		ativo.setNome(limparTexto(form.getNome()));
		ativo.setDescricao(limparTexto(form.getDescricao()));
		ativo.setNumeroPatrimonio(limparTexto(form.getNumeroPatrimonio()));
		ativo.setLocalizacao(limparTexto(form.getLocalizacao()));
		ativo.setStatus(limparTexto(form.getStatus()));
		ativo.setCategoria(categoria);
		ativo.setCadastradoPor(funcionario);

		return ativoPatrimonialRepository.save(ativo);
	}

	@Transactional
	public AtivoPatrimonial atualizar(Long id, AtivoPatrimonialForm form) {
		validarNumeroPatrimonial(form.getNumeroPatrimonio(), id);
		Categoria categoria = categoriaService.buscarPorId(form.getCategoriaId());
		AtivoPatrimonial ativo = buscarPorId(id);

		ativo.setNome(limparTexto(form.getNome()));
		ativo.setDescricao(limparTexto(form.getDescricao()));
		ativo.setNumeroPatrimonio(limparTexto(form.getNumeroPatrimonio()));
		ativo.setLocalizacao(limparTexto(form.getLocalizacao()));
		ativo.setStatus(limparTexto(form.getStatus()));
		ativo.setCategoria(categoria);

		return ativoPatrimonialRepository.save(ativo);
	}

	@Transactional
	public void excluir(Long id) {
		AtivoPatrimonial ativo = buscarPorId(id);
		ativoPatrimonialRepository.delete(ativo);
	}

	public long contar() {
		return ativoPatrimonialRepository.count();
	}

	private void validarNumeroPatrimonial(String numeroPatrimonio, Long idAtual) {
		String numeroLimpo = limparTexto(numeroPatrimonio);
		Optional<AtivoPatrimonial> ativoExistente = ativoPatrimonialRepository
				.findByNumeroPatrimonioIgnoreCase(numeroLimpo);

		if (ativoExistente.isPresent()) {
			Long idExistente = ativoExistente.get().getId();

			if (idAtual == null || !idAtual.equals(idExistente)) {
				throw new IllegalArgumentException("ja existe um ativo com este numero patrimonial");
			}
		}
	}

	private String limparTexto(String texto) {
		return texto == null ? "" : texto.trim();
	}
}
