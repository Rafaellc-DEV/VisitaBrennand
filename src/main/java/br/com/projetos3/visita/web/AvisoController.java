package br.com.projetos3.visita.web;

import br.com.projetos3.visita.entity.Aviso;
import br.com.projetos3.visita.repository.AvisoRepository; // Importe o Repository
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/admin/avisos") // Controla tudo dentro de /admin/avisos
public class AvisoController {

    private final AvisoRepository avisoRepository;

    // Injeta o repositório para podermos salvar
    public AvisoController(AvisoRepository avisoRepository) {
        this.avisoRepository = avisoRepository;
    }

    // Este método MOSTRA a página do formulário
    @GetMapping
    public String mostrarFormulario(Model model) {
        if (!model.containsAttribute("aviso")) {
            model.addAttribute("aviso", new Aviso());
        }
        model.addAttribute("pageTitle", "Publicar Aviso");
        return "admin/publicar_aviso"; // O nome do seu ficheiro HTML
    }

    // Este método RECEBE os dados do formulário
    @PostMapping("/salvar")
    public String salvarAviso(@ModelAttribute Aviso aviso, RedirectAttributes ra) {
        // O Spring vai automaticamente ligar os campos do formulário
        // (data, texto, ativo) ao objeto 'aviso'

        avisoRepository.save(aviso); // Salva no banco de dados

        // Adiciona uma mensagem de sucesso para ser mostrada na página
        ra.addFlashAttribute("success", "Aviso publicado com sucesso!");
        return "redirect:/admin/avisos"; // Redireciona de volta para o formulário
    }
}