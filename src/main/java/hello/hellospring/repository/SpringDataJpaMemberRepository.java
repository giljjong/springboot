package hello.hellospring.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import hello.hellospring.domain.Member;

// JpaRepository를 extends하면 SpringDataJpa가 알아서 구현체를 만들어서 등록한다.
// JpaRepository가 interface에 대한 구현체를 만들어서 Spring bean에 등록한다.
public interface SpringDataJpaMemberRepository extends JpaRepository<Member, Long>,  MemberRepository{
	@Override
	Optional<Member> findByName(String name);
}
