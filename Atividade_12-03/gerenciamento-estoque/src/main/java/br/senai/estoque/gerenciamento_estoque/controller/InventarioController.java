package br.senai.estoque.gerenciamento_estoque.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import br.senai.estoque.gerenciamento_estoque.service.AtivoPatrimonialService;
import br.senai.estoque.gerenciamento_estoque.service.MaterialService;
import br.senai.estoque.gerenciamento_estoque.service.SessaoService;
import jakarta.servlet.http.HttpSession;

@Controller
public class InventarioController {

	@Autowired
	private MaterialService materialService;

	@Autowired
	private AtivoPatrimonialService ativoPatrimonialService;

	@Autowired
	private SessaoService sessaoService;

	@GetMapping("/inventario")
	public String visualizarInventario(Model model, HttpSession session) {
		if (!sessaoService.estaLogado(session)) {
			return "redirect:/login";
		}
		model.addAttribute("materiais", materialService.listarTodos());
		model.addAttribute("ativos", ativoPatrimonialService.listarTodos());
		return "inventario/lista";
	}
}
