package br.com.projetos3.visita.web;

import br.com.projetos3.visita.entity.Feedback;
import br.com.projetos3.visita.repository.FeedbackRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Controller
public class FeedbackController {

    private final FeedbackRepository repo;

    public FeedbackController(FeedbackRepository repo) {
        this.repo = repo;
    }

    @GetMapping("/admin/feedbacks")
    public String listarFeedbacks(
            @RequestParam(required = false) String tipo,
            @RequestParam(required = false, defaultValue = "recentes") String ordenar,
            Model model) {

        // Busca todos
        List<Feedback> feedbacks = repo.findAll();

        // Filtro de tipo (reclamação/sugestão)
        if (tipo != null && !tipo.isBlank()) {
            feedbacks = feedbacks.stream()
                    .filter(f -> f.getTipo().equalsIgnoreCase(tipo))
                    .collect(Collectors.toList());
        }

        // Ordenação
        if ("estrelas".equalsIgnoreCase(ordenar)) {
            feedbacks.sort(Comparator.comparing(Feedback::getNota).reversed());
        } else { // padrão: mais recentes
            feedbacks.sort(Comparator.comparing(Feedback::getCriadoEm).reversed());
        }

        model.addAttribute("feedbacks", feedbacks);
        model.addAttribute("tipoSelecionado", tipo);
        model.addAttribute("ordenacaoSelecionada", ordenar);
        model.addAttribute("pageTitle", "Feedbacks");
        return "admin/feedbacks";
    }
}