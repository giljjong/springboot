package hello.hellospring.repository;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import hello.hellospring.domain.Member;

import static org.assertj.core.api.Assertions.*;

import java.util.List;

public class MemoryMemberRepositoryTest {
	// 객체 미리 선언
	MemoryMemberRepository repository = new MemoryMemberRepository();
	
	// @AfterEach 어노테이션은 테스트가 끝날 때마다 행동한다.
	@AfterEach
	public void afterEach() {
		repository.clearStore();
	}
	
	// @Test 어노테이션으로 해당 메소드를 바로 실행 시킨다.
	@Test
	public void save() {
		// 유저 생성
		Member member = new Member();
		member.setName("spring");
		
		repository.save(member);
		
		Member result = repository.findById(member.getId()).get();
		
		/* 
		 * jupiter에서 나온 것을 사용한다.
		 * 정상실행일 경우 초록 빛
		 * 에러가 날 경우 붉은 빛
		 *  */
		// Assertions.assertEquals(member, result);
		Assertions.assertThat(member).isEqualTo(result);
		// Assertions를 static으로 선언하여 객체 선언 없이 메소드 사용 가능
		assertThat(member).isEqualTo(result);
	}
	
	@Test
	public void findByName() {
		// 회원 가입
		Member member1 = new Member();
		member1.setName("spring1");
		repository.save(member1);
		
		Member member2 = new Member();
		member2.setName("spring2");
		repository.save(member2);
		
		// get()을 사용할 경우 Optional이 언박싱 되어 반환
		Member result = repository.findByName("spring1").get();
		
		assertThat(result).isEqualTo(member1);
	}
	
	@Test
	public void findAll() {
		// 회원 가입
		Member member1 = new Member();
		member1.setName("spring1");
		repository.save(member1);
		
		Member member2 = new Member();
		member2.setName("spring2");
		repository.save(member2);
		// findAll 메소드는 List로 반환한다.
		List<Member> result = repository.findAll();
		
		assertThat(result.size()).isEqualTo(2);
	}
}
