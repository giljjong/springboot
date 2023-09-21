package hello.hellospring.repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.sql.DataSource;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;

import hello.hellospring.domain.Member;

public class JdbcTemplateMemberRepository implements MemberRepository {
	// 디자인 패턴 중 template라는 것이 있는데 그걸 많이 사용하여 줄였기 때문.
	private final JdbcTemplate jdbcTemplate;
	
	// 생성자가 하나일 때 @Autowired를 생략 할 수 있다.
	public JdbcTemplateMemberRepository(DataSource dataSource) {
		jdbcTemplate = new JdbcTemplate(dataSource);
	}
	
	@Override
	public Member save(Member member) {
		// insert문을 만들어준다.
		// generatedKey 기능도 추가하여 key 값을 반환한다.
		SimpleJdbcInsert jdbcInsert = new SimpleJdbcInsert(jdbcTemplate);
		jdbcInsert.withTableName("member").usingGeneratedKeyColumns("id");
		
		Map<String, Object> parameters = new HashMap<>();
		parameters.put("name", member.getName());
		
		Number key = jdbcInsert.executeAndReturnKey(new MapSqlParameterSource(parameters));
		member.setId(key.longValue());
		return member;
	}

	@Override
	public Optional<Member> findById(Long id) {
		// 0번 자리에 입력해준 쿼리를 실행시켜 1번 자리의 메소드를 통해 객체에 넣어준다.
		List<Member> result = jdbcTemplate.query("select * from member where id = ?", memberRowMapper(), id);
		// Optional을 없애고 반환
		return result.stream().findAny();
	}

	@Override
	public Optional<Member> findByName(String name) {
		List<Member> result = jdbcTemplate.query("select * from member where name = ?",  memberRowMapper(), name);
		return result.stream().findAny();
	}

	@Override
	public List<Member> findAll() {
		return jdbcTemplate.query("select * from member", memberRowMapper());
	}

	private RowMapper<Member> memberRowMapper() {
		// 람다 형식으로 변환
		// 여기서 객체를 생성한다.
		return (rs, rowNum) -> {
				Member member = new Member();
				member.setId(rs.getLong("id"));
				member.setName(rs.getString("name"));
				return member;
		};
	}
}
