package br.nom.penha.bruno.treino.pact.tarefas.apresentacao.pact.consumer;

import au.com.dius.pact.consumer.dsl.PactDslWithProvider;
import au.com.dius.pact.consumer.junit.PactProviderRule;
import au.com.dius.pact.consumer.junit.PactVerification;
import au.com.dius.pact.core.model.RequestResponsePact;
import au.com.dius.pact.core.model.annotations.Pact;
import br.nom.penha.bruno.treino.pact.tarefas.apresentacao.repositorios.RepositorioTarefas;
import org.junit.Rule;
import org.junit.Test;

public class RemoveTarefaTest {

    @Rule
    public PactProviderRule mockProvedor = new PactProviderRule("Tarefas", this);

    @Pact(consumer = "TarefasApresentacao")
    public RequestResponsePact criaPacto(PactDslWithProvider contrutor){

        return contrutor
                .given("Existe uma tarefa com o id = 1")
                .uponReceiving("Remove uma tarefa")
                    .path("/todo/1")
                    .method("DELETE")
                .willRespondWith()
                    .status(204)
                .toPact();

    }

    @Test
    @PactVerification
    public void deveriaAtualizarUmaTarefa(){
        RepositorioTarefas repo = new RepositorioTarefas(mockProvedor.getUrl());

        repo.remover(1L);


    }
}
