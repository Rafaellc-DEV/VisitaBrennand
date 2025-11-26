package br.com.projetos3.visita.web;

import br.com.projetos3.visita.entity.Aviso;
import br.com.projetos3.visita.entity.ComoChegar;
import br.com.projetos3.visita.entity.Feedback;
import br.com.projetos3.visita.entity.Horario;
import br.com.projetos3.visita.service.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

// Importações estáticas essenciais
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class HomeControllerTests {

    @Autowired
    private MockMvc mvc;

    // Mock dos serviços utilizados pelo HomeController
    @MockBean
    private FeedbackService feedbackService;

    @MockBean
    private AvisoService avisoService;

    @MockBean
    private RegraService regraService;

    @MockBean
    private ComoChegarService comoChegarService;

    @MockBean
    private HorarioService horarioService; // Necessário pois a Home carrega os horários no rodapé/corpo

    @Test
    void testarEnvioFeedbackValido() throws Exception {
        // Mock necessário para evitar NullPointerException ao carregar a página após o redirect (se houver lógica no controller)
        when(horarioService.getHorario()).thenReturn(new Horario());
        when(comoChegarService.getComoChegar()).thenReturn(new ComoChegar());

        mvc.perform(post("/feedback")
                        .param("nome", "Visitante Teste")
                        .param("tipo", "Sugestão")
                        .param("nota", "5")
                        .param("comentario", "Ótimo aplicativo, muito útil!")
                        .with(csrf())
                )
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/#feedback-form"))
                .andExpect(flash().attributeExists("ok"));

        verify(feedbackService).salvar(any(Feedback.class));
    }

    @Test
    void testarEnvioFeedbackInvalido_SemComentario() throws Exception {
        mvc.perform(post("/feedback")
                        .param("nome", "Visitante Teste")
                        .param("tipo", "Reclamação")
                        .param("nota", "2")
                        .param("comentario", "") // Inválido: Vazio
                        .with(csrf())
                )
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/#feedback-form"))
                .andExpect(flash().attributeExists("erro"))
                .andExpect(flash().attributeExists("org.springframework.validation.BindingResult.feedback"));

        verify(feedbackService, never()).salvar(any(Feedback.class));
    }

    // --- TESTES DA API DE AVISOS ---

    @Test
    void testarApiAvisoEncontrado() throws Exception {
        LocalDate dataEspecifica = LocalDate.of(2025, 12, 25);

        Aviso avisoMock = new Aviso();
        avisoMock.setData(dataEspecifica);
        avisoMock.setAtivo(false); // Fechado
        avisoMock.setTexto("Fechado para o Natal");

        List<Aviso> listaDeAvisos = List.of(avisoMock);

        when(avisoService.porData(dataEspecifica)).thenReturn(listaDeAvisos);

        mvc.perform(get("/api/avisos")
                        .param("data", "2025-12-25")
                )
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].texto", is("Fechado para o Natal")))
                .andExpect(jsonPath("$[0].ativo", is(false)));
    }

    @Test
    void testarApiAvisoNaoEncontrado() throws Exception {
        LocalDate dataSemAviso = LocalDate.of(2025, 11, 11);

        when(avisoService.porData(dataSemAviso)).thenReturn(Collections.emptyList());

        mvc.perform(get("/api/avisos")
                        .param("data", "2025-11-11")
                )
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(0)));
    }

    // --- TESTE DE VISUALIZAÇÃO (COMO CHEGAR) ---

    @Test
    void testarVisualizacaoComoChegarNaHome() throws Exception {
        // 1. Preparar dados simulados
        ComoChegar dadosAtualizados = new ComoChegar();
        dadosAtualizados.setBarcoDescricao("Texto Novo do Barco");
        dadosAtualizados.setCarroDescricao("Texto Novo do Carro");
        dadosAtualizados.setCatamaraDescricao("Texto Novo do Catamarã");

        // Preenche campos obrigatórios para evitar erro no template
        dadosAtualizados.setBarcoPreco("R$ 10");
        dadosAtualizados.setBarcoTempo("10 min");
        dadosAtualizados.setBarcoBike("Sim");
        dadosAtualizados.setCarroPasso1("P1");
        dadosAtualizados.setCarroPasso2("P2");
        dadosAtualizados.setCarroPasso3("P3");
        dadosAtualizados.setCatamaraLink("http://link.com");

        // Mock de Horário também é necessário pois a Home carrega ele
        when(horarioService.getHorario()).thenReturn(new Horario());

        // 2. Mock do serviço ComoChegar
        when(comoChegarService.getComoChegar()).thenReturn(dadosAtualizados);

        // 3. Executar a requisição GET na Home
        mvc.perform(get("/"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("comoChegar"))
                // Verifica se o HTML final contém os textos definidos
                .andExpect(content().string(containsString("Texto Novo do Barco")))
                .andExpect(content().string(containsString("Texto Novo do Carro")))
                .andExpect(content().string(containsString("Texto Novo do Catamarã")));
    }
}