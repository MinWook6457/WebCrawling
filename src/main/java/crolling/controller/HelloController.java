package crolling.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

// 디스패처 서블릿이 우선적으로 @Controller로 매핑된 모든 어노테이션 파싱
@Controller
public class HelloController {

	@GetMapping("/home")
    public String home(Model model) {
        model.addAttribute("message", "Hello, Spring Boot MVC!");
        return "home";
    }
}

