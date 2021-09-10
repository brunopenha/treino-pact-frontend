package br.nom.penha.bruno.treino.pact.tarefas.apresentacao.pact.consumer;

import au.com.dius.pact.consumer.dsl.DslPart;
import au.com.dius.pact.consumer.dsl.PactDslJsonBody;
import au.com.dius.pact.consumer.dsl.PactDslWithProvider;
import au.com.dius.pact.consumer.junit.PactProviderRule;
import au.com.dius.pact.consumer.junit.PactVerification;
import au.com.dius.pact.core.model.RequestResponsePact;
import au.com.dius.pact.core.model.annotations.Pact;
import br.nom.penha.bruno.treino.pact.tarefas.apresentacao.model.Todo;
import br.nom.penha.bruno.treino.pact.tarefas.apresentacao.repositorios.RepositorioTarefas;
import org.junit.Rule;
import org.junit.Test;

import java.io.IOException;

import static org.hamcrest.CoreMatchers.containsString;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

public class SalvaTarefaInvalidaTest {

    @Rule
    public PactProviderRule mockProvider = new PactProviderRule("Tarefas", this);

    @Pact(consumer = "TarefasApresentacao")
    public RequestResponsePact criaPacto(PactDslWithProvider construtor){

        DslPart corpoRequisitado = new PactDslJsonBody()
                .nullValue("id")
                .nullValue("task")
                .nullValue("dueDate");

        DslPart corpoRetornado = new PactDslJsonBody()
                .stringType("message", "Fill the task description");

        return construtor
                .uponReceiving("Salvar uma tarefa inválida")
                .path("/todo")
                .method("POST")
                .matchHeader("Content-type","application/json.*","application/json")
                .body(corpoRequisitado)
                .willRespondWith()
                .status(400)
                .body(corpoRetornado)
                .toPact();
    }


    @Test
    @PactVerification
    public void test() throws IOException {
        // Preparo o ambiente
        RepositorioTarefas consumidor = new RepositorioTarefas(mockProvider.getUrl());
        System.out.println("mockProvider.getUrl() >>>>>>>>>>> " + mockProvider.getUrl());

        // Executo a tarefa que quero testar
        try {
            consumidor.salvar(new Todo(null,null, null));
            fail("Deveria ter sido lançada uma exceção");
        } catch (Exception e) {

            // Verifico o resultado
            System.out.println(e.getMessage());
            assertThat(e.getMessage(), containsString("400 Bad Request"));
            assertThat(e.getMessage(), containsString("Fill the task description"));
        }

    }
}
