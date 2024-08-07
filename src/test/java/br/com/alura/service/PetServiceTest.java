package br.com.alura.service;

import br.com.alura.client.ClientHttpConfiguration;
import br.com.alura.domain.Pet;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.net.http.HttpResponse;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

public class PetServiceTest {
    private ClientHttpConfiguration client = mock(ClientHttpConfiguration.class);
    private PetService petService = new PetService(client);
    private HttpResponse<String> response = mock(HttpResponse.class);
    private Pet pet = new Pet("TIPOTESTE", "nomeTeste", "racaTeste", 0, "corTeste", 8.0F);

    @Test
    public void deveVerificarSeDispararRequisicaoPostSeraChamado() throws IOException, InterruptedException {
        pet.setId(0L);
        String expectedIdOuNomeAbrigo = "Digite o id ou nome do abrigo:";
        String expectedNomeArquivo = "ID ou nome não cadastrado";

        String userInput = String.format("Teste%spets.csv", System.lineSeparator());
        ByteArrayInputStream bais = new ByteArrayInputStream(userInput.getBytes());
        System.setIn(bais);

        when(client.dispararRequisicaoPost(anyString(), any())).thenReturn(response);

        petService.importarPetsDoArquivo();
        verify(client.dispararRequisicaoPost(anyString(), anyString()), atLeast(1));

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintStream printStream = new PrintStream(baos);
        System.setOut(printStream);

        String[] lines = baos.toString().split(System.lineSeparator());
        String actualIdOuNome = lines[0];
        String actualNomeArquivo = lines[1];

        Assertions.assertEquals(expectedIdOuNomeAbrigo, actualIdOuNome);
        Assertions.assertEquals(expectedNomeArquivo, actualNomeArquivo);
    }
}
