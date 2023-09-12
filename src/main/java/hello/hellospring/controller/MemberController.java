package hello.hellospring.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import hello.hellospring.domain.Member;
import hello.hellospring.service.MemberService;

/*
 * 스프링 컨테이너가 생성될 때
 * @Controller 애너테이션이 있으면 spring 해당 controller를 컨테이너에 넣어두고 관리한다.
 * 이것을 스프링 빈이라고 한다.
 */
@Controller
public class MemberController {
	/*
	 * private final MemberService memberService = new MemberService();
	 * 이렇게 new로 계속 생성하면 성능상에 낭비가 된다.
	 * 사유 1 : 여러 군데에서 사용될 때 마다 생성
	 * 사유 2 : 내부에 코드가 별게 없다.
	 */
	private final MemberService memberService;
	
	/*
	 * @Autowired 애너테이션을 사용하면
	 * MemberController가 생성이 될 때
	 * 스프링 컨테이너에 등록되어있는 MemberService를 가져와 사용한다(연결).
	 */
	@Autowired
	public MemberController(MemberService memberService) {
		this.memberService = memberService;
	}
	
	// get은 주로 조회할 때 쓴다.
	@GetMapping("/members/new")
	public String createForm() {
		return "members/createMemberForm";
	}
	
	// post는 데이터 등록을 위해 전송할 때 사용한다.
	@PostMapping("/members/new")
	public String create(MemberForm form) {
		// 받아온 form 데이터를 통해
		Member member = new Member();
		member.setName(form.getName());
		// member를 등록한다.
		memberService.join(member);
		// 그 후 리다이렉트를 통해 index 페이지로 이동한다.
		return "redirect:/";
	}
	
	@GetMapping("/members")
	public String list(Model model) {
		List<Member> members = memberService.findMembers();
		// addAttribute를 통해 html로 데이터를 넘긴다.
		model.addAttribute("members", members);
		return "members/memberList";
	}
}
