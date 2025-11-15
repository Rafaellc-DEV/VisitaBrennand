package br.com.projetos3.visita.web;

import br.com.projetos3.visita.entity.Aviso;
import br.com.projetos3.visita.repository.AvisoRepository;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;

// Importações estáticas
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class AvisoControllerTests {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private AvisoRepository avisoRepository; // O AvisoController usa o Repositório

    @Test
    @WithMockUser(roles = "ADMIN") // Precisa estar logado como admin
    void testarPublicarAvisoFechado() throws Exception {
        // 1. Prepara os dados do formulário
        String dataDoAviso = "2025-12-25";
        String textoDoAviso = "Fechado para o Natal";

        // 2. Executa a requisição POST (simulando o admin)
        mvc.perform(post("/admin/avisos/salvar") // Rota de salvamento
                        .param("data", dataDoAviso)
                        .param("texto", textoDoAviso)
                        .param("ativo", "false") // 'false' = Parque Fechado
                        .with(csrf()) // Adiciona o token de segurança CSRF
                )
                // 3. Verifica as Respostas
                .andExpect(status().is3xxRedirection()) // Espera um redirecionamento
                .andExpect(redirectedUrl("/admin/avisos")) // Para a página de avisos
                .andExpect(flash().attribute("success", "Aviso publicado com sucesso!")); // Com a msg de sucesso

        // 4. Verifica se o repositório foi chamado para SALVAR
        // Usamos o ArgumentCaptor para "capturar" o objeto que foi passado para o save()
        ArgumentCaptor<Aviso> avisoCaptor = ArgumentCaptor.forClass(Aviso.class);
        verify(avisoRepository).save(avisoCaptor.capture());

        // 5. Verifica se o objeto salvo tem os dados corretos
        Aviso avisoSalvo = avisoCaptor.getValue();
        assertThat(avisoSalvo.getData()).isEqualTo(LocalDate.of(2025, 12, 25));
        assertThat(avisoSalvo.getTexto()).isEqualTo(textoDoAviso);
        assertThat(avisoSalvo.isAtivo()).isFalse(); // Verifica se está marcado como "Fechado"
    }
}