package pl.java.task_manager_app;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Controller
public class MainController {

    private TaskRepository taskRepository;

    public MainController(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }


    @GetMapping("/")
    public String home(Model model, @RequestParam(required = false) String task) {

        List<Task> taskList = null;

        if (task != null) {
            if (task.equals("completed")) {
                taskList = taskRepository.getAllByFinishIsTrue();
            } else if (task.equals("sortByDateAsc")) {
                taskList = taskRepository.getAllByFinishIsFalseSortByDateAsc();
            }
        } else {
            taskList = taskRepository.getAllByFinishIsFalse();
        }

        model.addAttribute("tasks", taskList);
        model.addAttribute("taskModify", new Task());
        model.addAttribute("addTask", new Task());
        return "home";
    }

    @PostMapping("/addTask")
    public String addNewTask(Task task) {
        task.setFinish(false);
        taskRepository.save(task);
        return "redirect:/";
    }

    @GetMapping("/markAsComplete")
    public String getTask(@RequestParam Long id) {

        Task task = null;
        System.out.println(id);
        Optional<Task> taskOptional = taskRepository.findById(id);

        if (taskOptional.isPresent()) {
            task = taskOptional.get();
            task.setFinish(true);
            task.setCompletionDate(LocalDate.now());
        } else {
            System.out.println("Nie ma takiego zadania");
        }
        taskRepository.save(task);
        return "redirect:/";
    }

    @GetMapping("/edit")
    public String getEditForm(@RequestParam Long id, Model model) {

        for (Task task : taskRepository.getAll()) {
            if (task.getId().equals(id)) {
                model.addAttribute("task", task);
                return "edit";
            }
        }
        return "redirect:/";
    }

    @PostMapping("/edit")
    public String edit(Task input) {
        for (Task task : taskRepository.getAll()) {
            if (input.getId().equals(task.getId())) {
                task.setName(input.getName());
                task.setDescription(input.getDescription());
                task.setDeadLine(input.getDeadLine());
                taskRepository.save(task);
            }
        }
        return "redirect:/";
    }
}