package hello.hellospring.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Optional;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import hello.hellospring.domain.Member;
import hello.hellospring.service.MemberService;

class MemberServiceTest {
	
	MemberService memberService;
	MemoryMemberRepository memberRepository;
	
	// 테스트 수행 전 동작
	@BeforeEach
	public void beforeEach() {
		// 의존성 주입을 한다.
		memberRepository = new MemoryMemberRepository();
		memberService = new MemberService(memberRepository);
	}
	
	@AfterEach
	public void afterEach() {
		memberRepository.clearStore();
	}

	// 테스트는 한글로 쓰기도 한다. 직관성.
	@Test
	void 회원가입() {
		// given 무엇인가가 주어졌을 때
		Member member = new Member();
		member.setName("hello"); // 이 값을 spring으로 변경할 경우 memoryDB에 계속 저장되기 때문에 예외가 발생한다.
		
		// when 이것을 실행 했을 때
		Long saveId = memberService.join(member);
		
		// then 이러한 결과가 나온다.
		Member findMember = memberService.findOne(saveId).get();
		Assertions.assertThat(member.getName()).isEqualTo(findMember.getName());
	}
	
	// 테스트는 예외가 생기는 경우가 더 중요하다.
	@Test
	public void 중복_회원_예외() {
		// given
		// 중복 회원을 만든다.
		Member member1 = new Member();
		member1.setName("spring");
		
		Member member2 = new Member();
		member2.setName("spring");
		
		// when
		memberService.join(member1);
		// 굳이 try를 사용하지 않아도 될 때 asserThrows를 사용할 수 있다.
		IllegalStateException e = assertThrows(IllegalStateException.class, () -> memberService.join(member2));
		assertThat(e.getMessage()).isEqualTo("이미 존재하는 회원입니다.");
		
		/*
		 * try { // 중복된 이름의 사용자를 기입 memberService.join(member2); fail("예외가 발생해야 합니다.");
		 * } catch(IllegalStateException e) {
		 * assertThat(e.getMessage()).isEqualTo("이미 존재하는 회원입니다."); }
		 */
		
		// then
	}

}
