package my.web.dao;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Repository;

import my.web.domain.BoardVO;
import my.web.domain.UserVO;
import my.web.dto.LoginDTO;

@Repository
public class UserDAOImpl implements UserDAO {
	@Inject
	private SqlSession sqlSession;

	private static final String NAMESPACE = "UserMapper";

	@Override
	public UserVO login(LoginDTO dto) throws Exception {
		return sqlSession.selectOne(NAMESPACE + ".login", dto);
	}
	
	@Override
	public void keepLogin(String uid, String sessionId, Date next) throws Exception {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("uid", uid);
		paramMap.put("sessionId", sessionId);
		paramMap.put("next", next);

		sqlSession.update(NAMESPACE + ".keepLogin", paramMap);
	}

	@Override
	public UserVO checkUserWithSessionKey(String value) throws Exception {
		return sqlSession.selectOne(NAMESPACE +".checkUserWithSessionKey", value);
	}

	@Override
	public void register(UserVO vo) throws Exception {
		sqlSession.insert(NAMESPACE + ".create", vo);
	}

	@Override
	public int selectUser(String uid) throws Exception {
		return sqlSession.selectOne(NAMESPACE + ".getUser", uid);
	}
}
