package br.com.projetos3.visita.web;

import br.com.projetos3.visita.entity.Feedback;
import br.com.projetos3.visita.service.FeedbackService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

// Importações estáticas essenciais
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class HomeControllerTests {

    @Autowired
    private MockMvc mvc;

    @MockBean // Usamos @MockBean para simular o serviço de feedback
    private FeedbackService feedbackService;

    @Test
    void testarEnvioFeedbackValido() throws Exception {
        // Testa o envio de um feedback válido
        mvc.perform(post("/feedback") // Faz um POST para a rota
                        .param("nome", "Visitante Teste")
                        .param("tipo", "Sugestão") //
                        .param("nota", "5") //
                        .param("comentario", "Ótimo aplicativo, muito útil!") //
                        .with(csrf()) // Adiciona um token CSRF válido (necessário pelo Spring Security)
                )
                .andExpect(status().is3xxRedirection()) // Espera um redirecionamento
                .andExpect(redirectedUrl("/#feedback-form")) // Espera redirecionar de volta para a âncora
                .andExpect(flash().attributeExists("ok")); // Espera a mensagem de sucesso

        // Verifica se o método salvar() do serviço FOI chamado 1 vez
        verify(feedbackService).salvar(any(Feedback.class));
    }

    @Test
    void testarEnvioFeedbackInvalido_SemComentario() throws Exception {
        // Testa o envio de um feedback inválido (comentário vazio)
        //
        mvc.perform(post("/feedback")
                        .param("nome", "Visitante Teste")
                        .param("tipo", "Reclamação")
                        .param("nota", "2")
                        .param("comentario", "") // Comentário vazio (inválido)
                        .with(csrf())
                )
                .andExpect(status().is3xxRedirection()) // Espera um redirecionamento
                .andExpect(redirectedUrl("/#feedback-form"))
                .andExpect(flash().attributeExists("erro")) // Espera a mensagem de erro
                .andExpect(flash().attributeExists("org.springframework.validation.BindingResult.feedback")); // Espera o erro de validação

        // Verifica se o método salvar() NUNCA foi chamado
        verify(feedbackService, never()).salvar(any(Feedback.class));
    }
}