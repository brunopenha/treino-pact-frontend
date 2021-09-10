package br.nom.penha.bruno.treino.pact.tarefas.apresentacao.controller;

import br.nom.penha.bruno.treino.pact.tarefas.apresentacao.model.Todo;
import br.nom.penha.bruno.treino.pact.tarefas.apresentacao.repositorios.RepositorioTarefas;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.client.RestTemplate;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Controller
public class AfazeresController {
	
	@Value("${backend.host}")
	private String BACKEND_HOST;

	@Value("${backend.port}")
	private String BACKEND_PORT;

	private RepositorioTarefas repositorioTarefas;


	public RepositorioTarefas getRepo(){
		if(repositorioTarefas == null){
			repositorioTarefas = new RepositorioTarefas(getBackendURL());
		}
		return repositorioTarefas;
	}

	private String getBackendURL() {
		return "http://" + BACKEND_HOST + ":" + BACKEND_PORT;
	}
	
	@GetMapping("")
	public String index(Model model) {
		model.addAttribute("todos", getRepo().obtemTarefas());
		return "index";
	}
	
	@GetMapping("add")
	public String add(Model model) {
		model.addAttribute("todo", new Todo());
		return "add";
	}

	@PostMapping("save")
	public String save(Todo todo, Model model) {
		try {
			RestTemplate restTemplate = new RestTemplate();
			if(todo.getId() == null) {
			    getRepo().salvar(todo);
			} else {
			    getRepo().atualizar(todo);
			}
			model.addAttribute("sucess", "Sucesso!");
			return "index";
		} catch(Exception e) {
			Pattern compile = Pattern.compile("message\":\"(.*)\",");
			Matcher m = compile.matcher(e.getMessage());
			m.find();
			model.addAttribute("error", m.group(1));
			model.addAttribute("todo", todo);
			return "add"; 
		} finally {
			model.addAttribute("todos", getRepo().obtemTarefas());
		}
	}
	
	@GetMapping("delete/{id}")
	public String delete(@PathVariable Long id, Model model) {
	    getRepo().remover(id);
		model.addAttribute("todos", getRepo().obtemTarefas());
		return "index";
	}
	
	@GetMapping("edit/{id}")
	public String edit(@PathVariable Long id, Model model) {
	    Todo todo = getRepo().obtemTarefasPeloId(id);
		if(todo == null) {
			model.addAttribute("error", "Todo invalida");
			model.addAttribute("todos", getRepo().obtemTarefas());
			return "index";
		}
		model.addAttribute("todo", todo);
		return "add";
	}

}
