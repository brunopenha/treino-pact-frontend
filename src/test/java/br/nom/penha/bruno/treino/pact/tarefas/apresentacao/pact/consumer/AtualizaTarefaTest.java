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

import java.time.LocalDate;

public class AtualizaTarefaTest {

    @Rule
    public PactProviderRule mockProvedor = new PactProviderRule("Tarefas", this);

    @Pact(consumer = "TarefasApresentacao")
    public RequestResponsePact criaPacto(PactDslWithProvider contrutor){
        DslPart corpo = new PactDslJsonBody()
                                .numberType("id",1)
                                .stringType("task","Tarefa Atualizada")
                                .array("dueDate")
                                    .numberType(2021)
                                    .numberType(9)
                                    .numberType(10)
                                .closeArray();
        return contrutor
                .given("Existe uma tarefa com o id = 1")
                .uponReceiving("Atualiza uma tarefa")
                    .path("/todo/1")
                    .method("PUT")
                    .matchHeader("Content-type","application/json.*","application/json")
                    .body(corpo)
                .willRespondWith()
                    .status(200)
                .toPact();

    }

    @Test
    @PactVerification
    public void deveriaAtualizarUmaTarefa(){
        RepositorioTarefas repo = new RepositorioTarefas(mockProvedor.getUrl());

        repo.atualizar(new Todo(1L,"Tarefa Atualizada", LocalDate.now()));


    }
}
