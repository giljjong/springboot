package hello.hellospring.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {
	
	// "/"는 index를 뜻한다.
	// 우선 순위에 의해 Mapping된 url을 찾아 반환한다.
	@GetMapping("/")
	public String home() {
		return "home";
	}
}
