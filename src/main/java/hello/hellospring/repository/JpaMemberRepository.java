package hello.hellospring.repository;

import java.util.List;
import java.util.Optional;

import javax.persistence.EntityManager;

import hello.hellospring.domain.Member;

public class JpaMemberRepository implements MemberRepository {
	// JPA는 EntityManager로 모든 동작을 한다.
	// spring boot가 자동으로 EntityManager를 만들어 준다. 이를 injection 받는다.
	private final EntityManager em;
	
	public JpaMemberRepository(EntityManager em) {
		this.em = em;
	}
	
	@Override
	public Member save(Member member) {
		// JPA가 insert, id generated 까지 모두 해준다.
		em.persist(member);
		return member;
	}

	@Override
	public Optional<Member> findById(Long id) {
		Member member = em.find(Member.class, id);
		return Optional.ofNullable(member);
	}

	@Override
	public Optional<Member> findByName(String name) {
		List<Member> result = em.createQuery("select m from Member m where m.name = :name", Member.class)
				.setParameter("name", name)
				.getResultList();
		
		return result.stream().findAny();
	}

	@Override
	public List<Member> findAll() {
		// 객체를 대상으로 쿼리를 보낸다. m은 Member 객체 자체를 말한다.
		return em.createQuery("select m from Member m", Member.class)
				.getResultList();
	}

}
