package br.nom.penha.bruno.treino.pact.tarefas.apresentacao.pact.consumer;

import au.com.dius.pact.consumer.dsl.DslPart;
import au.com.dius.pact.consumer.dsl.PactDslWithProvider;
import au.com.dius.pact.consumer.junit.PactProviderRule;
import au.com.dius.pact.consumer.junit.PactVerification;
import au.com.dius.pact.core.model.RequestResponsePact;
import au.com.dius.pact.core.model.annotations.Pact;
import br.nom.penha.bruno.treino.pact.tarefas.apresentacao.model.Todo;
import br.nom.penha.bruno.treino.pact.tarefas.apresentacao.repositorios.RepositorioTarefas;
import io.pactfoundation.consumer.dsl.LambdaDsl;
import org.junit.Rule;
import org.junit.Test;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Date;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class ObtemTodasAsTarefaTest {

    @Rule
    public PactProviderRule mockProvider = new PactProviderRule("Tarefas", this);

    @Pact(consumer = "TarefasApresentacao")
    public RequestResponsePact criaPacto(PactDslWithProvider construtor){

        DslPart corpo = LambdaDsl.newJsonArrayMinLike(1,(array) -> {
            array.object((obj) -> {
               obj.numberType("id", 1L);
               obj.stringType("task","Tarefa de Exemplo");
               obj.date("dueDate","yyyy-MM-dd", new Date());
            });
        }).build();

        return construtor
                .given("Existe uma tarefa com o id = 1")
                .uponReceiving("Buscar todas as tarefas")
                .path("/todo")
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
        Todo[] tarefas = consumidor.obtemTarefas();

        // Verifico o resultado
        assertThat(tarefas.length, is(1));
        assertThat(tarefas[0].getTask(), is("Tarefa de Exemplo"));
        assertThat(tarefas[0].getDueDate(), is(LocalDate.now()));

    }
}
