package hello.hellospring.service;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import hello.hellospring.repository.MemberRepository;
import hello.hellospring.repository.MemoryMemberRepository;

// @Configuration 직접 코드로 작성한 bean을 스프링 컨테이너에 올린다.
@Configuration
public class SpringConfig {
	
	@Bean
	public MemberService memberService() {
		return new MemberService(memberRepository());
	}
	
	@Bean
	public MemberRepository memberRepository() {
		// MemberRepository는 구현체(Interface)이기 때문에 MemoryMemberRepository를 return
		return new MemoryMemberRepository();
	}
}
