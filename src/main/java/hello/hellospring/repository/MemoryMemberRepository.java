package hello.hellospring.repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.stereotype.Repository;

import hello.hellospring.domain.Member;

public class MemoryMemberRepository implements MemberRepository {
	
	// 실무에서는 동시성 문제가 있을 수 있기에 공유되는 변수일 때는 ConcurrnetHashMap을 사용해야 함.
	private static Map<Long, Member> store = new HashMap<>();
	// 이또한 동일한 문제로 실무에서는 AtomicLong을 쓴다.
	private static long sequence = 0L;
	
	@Override
	public Member save(Member member) {
		// Id에 셋팅 후
		member.setId(++sequence);
		// store에 반환
		store.put(member.getId(), member);
		return member;
	}
	
	@Override
	public Optional<Member> findById(Long id) {
		// Optional로 감쌀 경우 null인 경우에도 return이 가능하다.
		return Optional.ofNullable(store.get(id));
	}
	
	@Override
	public Optional<Member> findByName(String name) {
		// 람다식.
		// member에서 name과 동일한 값이 있으면 반환한다.
		return store.values().stream()
				.filter(member -> member.getName().equals(name))
				.findAny();
	}
	
	@Override
	public List<Member> findAll() {
		// 모든 값을 반환한다.
		return new ArrayList<>(store.values());
	}
	
	public void clearStore() {
		// store의 데이터를 정리한다.
		store.clear();
	}
}
