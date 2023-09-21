package hello.hellospring.service;

import javax.persistence.EntityManager;
import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import hello.hellospring.repository.JdbcMemberRepository;
import hello.hellospring.repository.JdbcTemplateMemberRepository;
import hello.hellospring.repository.JpaMemberRepository;
import hello.hellospring.repository.MemberRepository;

// @Configuration 직접 코드로 작성한 bean을 스프링 컨테이너에 올린다.
@Configuration
public class SpringConfig {
	// springboot가 설정 파일을 참고하여 자체적으로 bean을 생성해준다.
	// private DataSource dataSource;
	
	// private EntityManager em;
	
	private final MemberRepository memberRepository;
	
	// 이를 통해 주입한다.
	@Autowired
	public SpringConfig(MemberRepository memberRepository) {
		this.memberRepository = memberRepository;
	}
	
	@Bean
	public MemberService memberService() {
		return new MemberService(memberRepository);
	}
	
	// @Bean
	// public MemberRepository memberRepository() {
		// return new MemoryMemberRepository();
		// MemberRepository는 구현체(Interface)이기 때문에 MemoryMemberRepository를 return
		// return new JdbcMemberRepository(dataSource);
		// return new JdbcTemplateMemberRepository(dataSource);
		// return new JpaMemberRepository(em);
	// }
}
