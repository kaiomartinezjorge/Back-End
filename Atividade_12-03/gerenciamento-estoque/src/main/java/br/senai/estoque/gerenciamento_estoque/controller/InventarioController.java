package br.senai.estoque.gerenciamento_estoque.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import br.senai.estoque.gerenciamento_estoque.repository.AtivoPatrimonialRepository;
import br.senai.estoque.gerenciamento_estoque.repository.MateriaisRepository;
import jakarta.servlet.http.HttpSession;

@Controller
public class InventarioController extends ControllerBase {

	@Autowired
	private MateriaisRepository materiaisRepository;

	@Autowired
	private AtivoPatrimonialRepository ativoPatrimonialRepository;

	@GetMapping("/inventario")
	public String visualizarInventario(Model model, HttpSession session) {
		if (!estaLogado(session)) {
			return "redirect:/login";
		}
		model.addAttribute("materiais", materiaisRepository.findAllByOrderByNomeAsc());
		model.addAttribute("ativos", ativoPatrimonialRepository.findAllByOrderByNomeAsc());
		return "inventario/lista";
	}
}
