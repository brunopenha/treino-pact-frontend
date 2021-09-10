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
import java.time.LocalDate;
import java.util.Date;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;

public class ObtemTarefaPeloIdTest {

    @Rule
    public PactProviderRule mockProvider = new PactProviderRule("Tarefas", this);

    @Pact(consumer = "TarefasApresentacao")
    public RequestResponsePact criaPacto(PactDslWithProvider construtor){
        DslPart corpo = new PactDslJsonBody()
                .numberType("id",1L)
                .stringType("task", "Comprar leite")
                .date("dueDate","yyyy-MM-dd", new Date());

        return construtor
                .given("Existe uma tarefa com o id = 1")
                .uponReceiving("Quando vier uma tarefa #1")
                .path("/todo/1")
                .method("GET")
                .willRespondWith()
                .status(200)
                .body(corpo)
                .toPact();
    }

    @Test
    @PactVerification
    public void test() throws IOException {
        // Preparo o ambiente
        RepositorioTarefas consumidor = new RepositorioTarefas(mockProvider.getUrl());
        System.out.println("mockProvider.getUrl() >>>>>>>>>>> " + mockProvider.getUrl());

        // Executo a tarefa que quero testar
        Todo tarefa = consumidor.obtemTarefasPeloId(1L);

        // Verifico o resultado
        assertThat(tarefa.getId(), is(1L));
        assertThat(tarefa.getTask(), is("Comprar leite"));
        assertThat(tarefa.getDueDate(), is(LocalDate.now()));

    }
}
