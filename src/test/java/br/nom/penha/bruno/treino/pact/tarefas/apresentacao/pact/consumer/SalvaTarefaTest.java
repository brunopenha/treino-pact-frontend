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

public class SalvaTarefaTest {

    @Rule
    public PactProviderRule mockProvider = new PactProviderRule("Tarefas", this);

    @Pact(consumer = "TarefasApresentacao")
    public RequestResponsePact criaPacto(PactDslWithProvider construtor){

        DslPart corpoRequisitado = new PactDslJsonBody()
                .nullValue("id")
                .stringType("task", "Uma nova tarefa")
                .array("dueDate")
                    .number(LocalDate.now().getYear())
                    .number(LocalDate.now().getMonthValue())
                    .number(LocalDate.now().getDayOfMonth())
                .closeArray();

        DslPart corpoRetornado = new PactDslJsonBody()
                .numberType("id")
                .stringType("task", "Uma nova tarefa")
                .date("dueDate","yyyy-MM-dd", new Date());

        return construtor
                .uponReceiving("Salvar uma tarefa")
                .path("/todo")
                .method("POST")
                .matchHeader("Content-type","application/json.*","application/json")
                .body(corpoRequisitado)
                .willRespondWith()
                .status(200)
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
        Todo tarefa = consumidor.salvar(new Todo(null,"Uma nova tarefa", LocalDate.now()));

        // Verifico o resultado
        assertThat(tarefa.getId(), is(notNullValue()));
        assertThat(tarefa.getTask(), is("Uma nova tarefa"));
        assertThat(tarefa.getDueDate(), is(LocalDate.now()));

    }
}
