package jpabook.jpashop;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HelloController {
    // hello란 url로 오면 이 컨트롤러가 호출된다!
    @GetMapping("hello")
    // Model : 스프링 UI에 있는 model => 컨트롤러에서 데이터를 싫어서 view에 넘길 수 있음
    // data라는 key에 hello!! 라는 값을 넘길 것
    // return  : 화면 이름 > resource/templates/hello.html
    public String hello(Model model){
        model.addAttribute("data", "hello!!");
        return "hello";
    }
}
