package hello.hellospring.repository;

import java.util.List;
import java.util.Optional;

import hello.hellospring.domain.Member;

public interface MemberRepository {
	Member save(Member member);
	/*
	 * id, name로 회원을 찾는다.
	 * Optional? null 처리 시 Optional로 감싸서 반환. JAVA 8의 기능
	 * */
	Optional<Member> findById(Long id);
	Optional<Member> findByName(String name);
	// 지금까지 저장된 모든 회원의 리스트 반환
	List<Member> findAll();
}
