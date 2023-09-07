package hello.hellospring.service;

import java.util.List;
import java.util.Optional;

import hello.hellospring.domain.Member;
import hello.hellospring.repository.MemberRepository;
import hello.hellospring.repository.MemoryMemberRepository;

public class MemberService {

	private MemberRepository memberRepository = new MemoryMemberRepository();
	
	// DI 의존성 주입
	public MemberService(MemberRepository memberRepository) {
		this.memberRepository = memberRepository;
	}
	
	/*
	 * 회원 가입
	 */
	public Long join(Member member) {
		validateDuplicateMember(member); // 중복 회원 검증
		memberRepository.save(member);
		return member.getId();
	}
	
	private void validateDuplicateMember(Member member) {
		// 동일한 이름을 가진 중복 회원X
				// Optional<Member> result = memberRepository.findByName(member.getName());
				// ifPresent : null이 아닌 값이 있으면 동작. Optional로 인해 null 값을 전달 받을 수 있어서 사용 가능
				// Optional 기능 중 orElseGet()은 값이 없으면 메소드를 실행하거나 default 값을 꺼내는 등의 기능을 한다.
				/*
				 * result.ifPresent(m -> { throw new IllegalStateException("이미 존재하는 회원입니다.");
				 * });
				 */
		
		// 보기 좋게 리팩토링
		memberRepository.findByName(member.getName())
			.ifPresent(m -> {
				throw new IllegalStateException("이미 존재하는 회원입니다.");
			});
	}
	
	/*
	 * 전체 회원 조회
	 * 서비스 로직은 비지니스에 가까운 name을 쓴다.
	 * 그래야 문제가 생겼을 때 바로 매칭이 된다.
	 */
	public List<Member> findMembers() {
		return memberRepository.findAll();
	}
	
	public Optional<Member> findOne(Long memberId) {
		return memberRepository.findById(memberId);
	}
}
