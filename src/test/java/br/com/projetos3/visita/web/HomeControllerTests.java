package br.com.projetos3.visita.web;

import br.com.projetos3.visita.entity.Aviso;
import br.com.projetos3.visita.entity.Feedback;
import br.com.projetos3.visita.service.AvisoService;
import br.com.projetos3.visita.service.FeedbackService;
import br.com.projetos3.visita.service.RegraService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType; // Importar
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

// Importações estáticas essenciais
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

    // O HomeController precisa dos 3 serviços no construtor
    // Precisamos simular (Mock) todos eles.
    @MockBean
    private FeedbackService feedbackService;
    @MockBean
    private AvisoService avisoService; // ADICIONADO
    @MockBean
    private RegraService regraService; // ADICIONADO


    @Test
    void testarEnvioFeedbackValido() throws Exception {
        // (Este teste você já tinha)
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
        // (Este teste você já tinha)
        mvc.perform(post("/feedback")
                        .param("nome", "Visitante Teste")
                        .param("tipo", "Reclamação")
                        .param("nota", "2")
                        .param("comentario", "")
                        .with(csrf())
                )
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/#feedback-form"))
                .andExpect(flash().attributeExists("erro"))
                .andExpect(flash().attributeExists("org.springframework.validation.BindingResult.feedback"));

        verify(feedbackService, never()).salvar(any(Feedback.class));
    }

    // --- NOVOS TESTES DA API DE AVISOS ---

    @Test
    void testarApiAvisoEncontrado() throws Exception {
        // 1. Cenário:
        // Queremos testar a data "2025-12-25".
        // O serviço deve retornar um aviso para esta data.
        LocalDate dataEspecifica = LocalDate.of(2025, 12, 25);

        Aviso avisoMock = new Aviso();
        avisoMock.setData(dataEspecifica);
        avisoMock.setAtivo(false); // Fechado
        avisoMock.setTexto("Fechado para o Natal");

        List<Aviso> listaDeAvisos = List.of(avisoMock);

        // 2. Configura o Mock (O que o serviço deve retornar)
        // QUANDO o avisoService.porData() for chamado com esta data...
        when(avisoService.porData(dataEspecifica)).thenReturn(listaDeAvisos); // ...ENTÃO retorne a lista com o aviso.

        // 3. Executa e Verifica
        mvc.perform(get("/api/avisos") // Rota da API
                        .param("data", "2025-12-25") // Passa a data como parâmetro
                )
                .andExpect(status().isOk()) // Espera HTTP 200 (Sucesso)
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON)) // Espera um JSON
                .andExpect(jsonPath("$", hasSize(1))) // Espera que a lista JSON tenha 1 item
                .andExpect(jsonPath("$[0].texto", is("Fechado para o Natal"))) // Verifica o texto
                .andExpect(jsonPath("$[0].ativo", is(false))); // Verifica se está "Fechado"
    }

    @Test
    void testarApiAvisoNaoEncontrado() throws Exception {
        // 1. Cenário:
        // Queremos testar uma data que não tem avisos.
        LocalDate dataSemAviso = LocalDate.of(2025, 11, 11);

        // 2. Configura o Mock
        // QUANDO o avisoService.porData() for chamado...
        when(avisoService.porData(dataSemAviso)).thenReturn(Collections.emptyList()); // ...ENTÃO retorne uma lista vazia.

        // 3. Executa e Verifica
        mvc.perform(get("/api/avisos")
                        .param("data", "2025-11-11")
                )
                .andExpect(status().isOk()) // Espera HTTP 200
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON)) // Espera um JSON
                .andExpect(jsonPath("$", hasSize(0))); // Espera que a lista JSON esteja vazia
    }
}