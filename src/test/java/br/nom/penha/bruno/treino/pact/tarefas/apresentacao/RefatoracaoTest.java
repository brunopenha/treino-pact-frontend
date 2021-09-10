package br.nom.penha.bruno.treino.pact.tarefas.apresentacao;

import br.nom.penha.bruno.treino.pact.tarefas.apresentacao.controller.AfazeresController;
import br.nom.penha.bruno.treino.pact.tarefas.apresentacao.repositorios.RepositorioTarefas;
import org.junit.Test;
import org.springframework.ui.Model;
import org.springframework.web.servlet.mvc.support.RedirectAttributesModelMap;

import java.lang.reflect.Field;

public class RefatoracaoTest {


    @Test
    public void antes() throws NoSuchFieldException, IllegalAccessException {
        AfazeresController servico = new AfazeresController();

        Field host = servico.getClass().getDeclaredField("BACKEND_HOST");
        host.setAccessible(true);
        host.set(servico,"localhost");

        Field porta = servico.getClass().getDeclaredField("BACKEND_PORT");
        porta.setAccessible(true);
        porta.set(servico,"8000");

        Model model = new RedirectAttributesModelMap();

        servico.index(model);
        System.out.println(model.getAttribute("todos"));
    }

    @Test
    public void depois(){
        RepositorioTarefas repo = new RepositorioTarefas("http://localhost:8000");
        System.out.println(repo.obtemTarefas()[0]);
    }

}