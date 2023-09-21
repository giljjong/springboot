package hello.hellospring.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.sql.DataSource;

import org.springframework.jdbc.datasource.DataSourceUtils;

import hello.hellospring.domain.Member;

public class JdbcMemberRepository implements MemberRepository {

	// DB와 연결하기 위해 필요하다.
	private final DataSource dataSource;
	
	// Spring에 의해 bean 주입
	public JdbcMemberRepository(DataSource dataSource) {
		this.dataSource = dataSource;
	}
	
	@Override
	public Member save(Member member) {
		// sql 구문 작성
		String sql = "insert into member(name) values(?)";
		// JDBC에 연결한다.
		Connection conn = null;
		PreparedStatement pstmt = null;
		// 결과 값
		ResultSet rs = null;
		
		try {
			conn = getConnection();
			pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
			
			// 1번 파라미터에 해당 값을 넣는다.
			pstmt.setString(1, member.getName());
			
			// 이 메소드가 실행될 때 쿼리가 실행된다.
			pstmt.executeUpdate();
			// key를 반환한다.
			rs = pstmt.getGeneratedKeys();
			
			if(rs.next()) {
				member.setId(rs.getLong(1));
			} else {
				throw new SQLException("id 조회 실패");
			}
			return member;
		} catch (Exception e) {
			throw new IllegalStateException(e);
		} finally {
			// 사용한 자본의 리소스를 반드시 반환해야 한다.
			close(conn, pstmt, rs);
		}
	}

	@Override
	public Optional<Member> findById(Long id) {
		String sql = "select * from member where id = ?";
		
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		try {
			conn = getConnection();
			pstmt = conn.prepareStatement(sql);
			pstmt.setLong(1, id);
			
			rs = pstmt.executeQuery();
			
			if(rs.next()) {
				// 값이 있으면 member 객체를 만들어 반환한다.
				Member member = new Member();
				member.setId(rs.getLong("id"));
				member.setName(rs.getString("name"));
				return Optional.of(member);
			} else {
				return Optional.empty();
			}
		} catch (Exception e) {
			throw new IllegalStateException(e);
		} finally {
			close(conn, pstmt, rs);
		}
	}

	@Override
	public Optional<Member> findByName(String name) {
		String sql = "select * from member where name = ?";
		
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		try {
			conn = getConnection();
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, name);
			rs = pstmt.executeQuery();
			
			if(rs.next()) {
				Member member = new Member();
				member.setId(rs.getLong("id"));
				member.setName(rs.getString("name"));
				return Optional.of(member);
			} else {
				return Optional.empty();
			}
		} catch (Exception e) {
			throw new IllegalStateException(e);
		} finally {
			close(conn, pstmt, rs);
		}
		
	}

	@Override
	public List<Member> findAll() {
		String sql = "select * from member";
		
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		try {
			conn = getConnection();
			pstmt = conn.prepareStatement(sql);
			rs = pstmt.executeQuery();
			
			List<Member> members = new ArrayList<>();
			while(rs.next()) {
				// 객체를 하나씩 list에 담아준다.
				Member member = new Member();
				member.setId(rs.getLong("id"));
				member.setName(rs.getString("name"));
				members.add(member);
			}
			return members;
		} catch (Exception e) {
			throw new IllegalStateException(e);
		} finally {
			close(conn, pstmt, rs);
		}
	}
	
	private Connection getConnection() {
		// springframework를 통해 jdbc connection을 사용할 때에는 DataSourceUtils를 통해서 connection을 해야한다.
		// 이유는 transaction에 걸릴 수 있기 때문에 동일한 database connection을 유지해야 하기 때문이다.
		return DataSourceUtils.getConnection(dataSource);
	}
	
	private void close(Connection conn, PreparedStatement pstmt, ResultSet rs) {
		// 반드시 실행과는 역순으로 종료해주어야 한다.
		try {
			if(rs != null) {
				rs.close();
			}
		} catch(SQLException e) {
			e.printStackTrace();
		}
		try {
			if (pstmt != null) {
				pstmt.close();
			}
		} catch(SQLException e) {
			e.printStackTrace();
		}
		try {
			if (conn != null) {
				conn.close();
			}
		} catch(SQLException e) {
			e.printStackTrace();
		}
	}

}
