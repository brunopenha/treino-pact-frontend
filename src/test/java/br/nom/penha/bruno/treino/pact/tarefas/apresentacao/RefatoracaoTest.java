package br.nom.penha.bruno.treino.pact.tarefas.apresentacao;

import br.nom.penha.bruno.treino.pact.tarefas.apresentacao.controller.TasksController;
import org.junit.Test;
import org.springframework.ui.Model;
import org.springframework.web.servlet.mvc.support.RedirectAttributesModelMap;

import java.lang.reflect.Field;

import static org.junit.Assert.*;

public class RefatoracaoTest {


    @Test
    public void antes() throws NoSuchFieldException, IllegalAccessException {
        TasksController servico = new TasksController();

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

}