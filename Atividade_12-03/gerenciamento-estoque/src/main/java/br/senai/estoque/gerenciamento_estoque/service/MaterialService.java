package br.senai.estoque.gerenciamento_estoque.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.senai.estoque.gerenciamento_estoque.dto.MaterialForm;
import br.senai.estoque.gerenciamento_estoque.model.Categoria;
import br.senai.estoque.gerenciamento_estoque.model.Funcionario;
import br.senai.estoque.gerenciamento_estoque.model.Materiais;
import br.senai.estoque.gerenciamento_estoque.repository.MateriaisRepository;
import br.senai.estoque.gerenciamento_estoque.repository.MovimentacaoRepository;

@Service
public class MaterialService {

	@Autowired
	private MateriaisRepository materiaisRepository;

	@Autowired
	private MovimentacaoRepository movimentacaoRepository;

	@Autowired
	private CategoriaService categoriaService;

	public List<Materiais> listarTodos() {
		return materiaisRepository.findAllByOrderByNomeAsc();
	}

	public Materiais buscarPorId(Long id) {
		return materiaisRepository.findById(id)
				.orElseThrow(() -> new IllegalArgumentException("material nao encontrado"));
	}

	@Transactional
	public Materiais salvar(MaterialForm form, Funcionario funcionario) {
		Categoria categoria = categoriaService.buscarPorId(form.getCategoriaId());

		Materiais material = new Materiais();
		material.setNome(limparTexto(form.getNome()));
		material.setDescricao(limparTexto(form.getDescricao()));
		material.setCategoria(categoria);
		material.setCriadoPor(funcionario);
		material.setQuantidade(0);

		return materiaisRepository.save(material);
	}

	@Transactional
	public Materiais atualizar(Long id, MaterialForm form) {
		Categoria categoria = categoriaService.buscarPorId(form.getCategoriaId());
		Materiais material = buscarPorId(id);

		material.setNome(limparTexto(form.getNome()));
		material.setDescricao(limparTexto(form.getDescricao()));
		material.setCategoria(categoria);

		return materiaisRepository.save(material);
	}

	@Transactional
	public void excluir(Long id) {
		Materiais material = buscarPorId(id);

		if (movimentacaoRepository.existsByMaterialId(id)) {
			throw new IllegalArgumentException("o material possui movimentacoes e nao pode ser excluido");
		}

		materiaisRepository.delete(material);
	}

	public long contar() {
		return materiaisRepository.count();
	}

	public List<Materiais> listarSemEstoque() {
		return listarTodos().stream()
				.filter(material -> material.getQuantidade() == 0)
				.toList();
	}

	private String limparTexto(String texto) {
		return texto == null ? "" : texto.trim();
	}
}
