package br.com.projetos3.visita.web;

import br.com.projetos3.visita.entity.Feedback;
import br.com.projetos3.visita.repository.FeedbackRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class FeedbackControllerTests {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private FeedbackRepository feedbackRepository; // O FeedbackController usa o Repositório

    private Feedback f1_sugestao_nota5;
    private Feedback f2_reclamacao_nota3;
    private Feedback f3_sugestao_nota4;
    private List<Feedback> todosOsFeedbacks;

    @BeforeEach
    void setUp() {
        // Configura os dados de teste que o repositório falso irá retornar
        f1_sugestao_nota5 = new Feedback();
        f1_sugestao_nota5.setTipo("Sugestão");
        f1_sugestao_nota5.setNota(5);
        f1_sugestao_nota5.setCriadoEm(LocalDateTime.now().minusDays(1)); // Ontem

        f2_reclamacao_nota3 = new Feedback();
        f2_reclamacao_nota3.setTipo("Reclamação");
        f2_reclamacao_nota3.setNota(3);
        f2_reclamacao_nota3.setCriadoEm(LocalDateTime.now()); // Hoje (mais recente)

        f3_sugestao_nota4 = new Feedback();
        f3_sugestao_nota4.setTipo("Sugestão");
        f3_sugestao_nota4.setNota(4);
        f3_sugestao_nota4.setCriadoEm(LocalDateTime.now().minusDays(2)); // Anteontem

        todosOsFeedbacks = new java.util.ArrayList<>(List.of(f1_sugestao_nota5, f2_reclamacao_nota3, f3_sugestao_nota4));
    }

    @Test
    @WithMockUser(roles = "ADMIN") // Simula um admin logado
    void testarListarTodosFeedbacks() throws Exception {
        // Define que o repositório falso deve retornar nossa lista
        when(feedbackRepository.findAll()).thenReturn(todosOsFeedbacks);

        // A ordenação padrão é "recentes"
        List<Feedback> esperado = List.of(f2_reclamacao_nota3, f1_sugestao_nota5, f3_sugestao_nota4);

        mvc.perform(get("/admin/feedbacks"))
                .andExpect(status().isOk()) // Espera HTTP 200
                .andExpect(view().name("admin/feedbacks")) // Espera a view correta
                .andExpect(model().attribute("feedbacks", esperado)); // Verifica se o modelo tem a lista ordenada
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void testarFiltroPorTipo() throws Exception {
        when(feedbackRepository.findAll()).thenReturn(todosOsFeedbacks);

        // Apenas sugestões, ordenadas por data (padrão)
        List<Feedback> esperado = List.of(f1_sugestao_nota5, f3_sugestao_nota4);

        mvc.perform(get("/admin/feedbacks")
                        .param("tipo", "Sugestão") // Aplica o filtro
                )
                .andExpect(status().isOk())
                .andExpect(model().attribute("feedbacks", esperado)); // Verifica se o modelo tem SÓ sugestões
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void testarOrdenacaoPorEstrelas() throws Exception {
        when(feedbackRepository.findAll()).thenReturn(todosOsFeedbacks);

        // Ordenado por nota, da maior para a menor
        List<Feedback> esperado = List.of(f1_sugestao_nota5, f3_sugestao_nota4, f2_reclamacao_nota3);

        mvc.perform(get("/admin/feedbacks")
                        .param("ordenar", "estrelas") // Aplica a ordenação
                )
                .andExpect(status().isOk())
                .andExpect(model().attribute("feedbacks", esperado)); // Verifica se a lista está ordenada por nota
    }
}