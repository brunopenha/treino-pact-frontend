package br.nom.penha.bruno.treino.pact.tarefas.apresentacao.repositorios;

import br.nom.penha.bruno.treino.pact.tarefas.apresentacao.model.Todo;
import org.springframework.web.client.RestTemplate;

public class RepositorioTarefas {

    private String url;

    public RepositorioTarefas(String url) {
        this.url = url;
    }


    public Todo[] obtemTarefas() {
        RestTemplate restTemplate = new RestTemplate();
        return restTemplate.getForObject(url + "/todo", Todo[].class);
    }

    public Todo salvar(Todo todoAserSalva) {

        RestTemplate restTemplate = new RestTemplate();
        return restTemplate.postForObject(url + "/todo", todoAserSalva, Todo.class);

    }

    public void atualizar(Todo todoAserAtualizada) {
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.put(url + "/todo/" + todoAserAtualizada.getId(), todoAserAtualizada);

    }


    public void remover(Long id) {
        RestTemplate restTemplate = new RestTemplate();

        restTemplate.delete(url + "/todo/" + id);

    }


    public Todo obtemTarefasPeloId(Long id) {
        RestTemplate restTemplate = new RestTemplate();
        return restTemplate.getForObject(
                url + "/todo/" + id, Todo.class);
    }

}
