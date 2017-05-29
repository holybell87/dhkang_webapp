package my.api.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Repository;

import my.api.domain.Criteria;
import my.api.domain.ReplyVO;

@Repository
public class ReplyDAOImpl implements ReplyDAO {
	@Inject
	private SqlSession sqlSession;

	private static final String NAMESPACE = "ReplyMapper";

	@Override
	public List<ReplyVO> list(int board_idx) throws Exception {
		return sqlSession.selectList(NAMESPACE + ".list", board_idx);
	}
	
	@Override
	public void create(ReplyVO vo) throws Exception {
		sqlSession.insert(NAMESPACE+".create", vo);
	}

	@Override
	public void update(ReplyVO vo) throws Exception {
		sqlSession.update(NAMESPACE+".update", vo);
	}

	@Override
	public void delete(int idx) throws Exception {
		sqlSession.delete(NAMESPACE+".delete", idx);
	}

	@Override
	public List<ReplyVO> listPage(int board_idx, Criteria cri) throws Exception {
		Map<String, Object> paramMap = new HashMap<>();

		paramMap.put("board_idx", board_idx);
		paramMap.put("cri", cri);

		return sqlSession.selectList(NAMESPACE + ".listPage", paramMap);
	}

	@Override
	public int count(int board_idx) throws Exception {
		return sqlSession.selectOne(NAMESPACE + ".count", board_idx);
	}
}
