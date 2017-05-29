package my.web.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Repository;

import my.web.domain.BoardVO;
import my.web.domain.Criteria;
import my.web.domain.SearchCriteria;

@Repository
public class BoardDAOImpl implements BoardDAO {
	@Inject
	private SqlSession sqlSession;

	private static final String NAMESPACE = "BoardMapper";

	@Override
	public void create(BoardVO vo) throws Exception {
		sqlSession.insert(NAMESPACE+".create", vo);
	}

	@Override
	public BoardVO read(int idx) throws Exception {
		return sqlSession.selectOne(NAMESPACE+".read", idx);
	}

	@Override
	public void update(BoardVO vo) throws Exception {
		sqlSession.update(NAMESPACE+".update", vo);
	}

	@Override
	public void delete(int idx) throws Exception {
		sqlSession.delete(NAMESPACE+".delete", idx);
	}

	@Override
	public List<BoardVO> listAll() throws Exception {
		return sqlSession.selectList(NAMESPACE+".listAll");
	}

	@Override
	public List<BoardVO> listPage(int page) throws Exception {
		if(page <= 0) {
			page = 1;
		}

		page = (page - 1) * 10;

		return sqlSession.selectList(NAMESPACE+".listPage", page);
	}

	@Override
	public List<BoardVO> listCriteria(Criteria cri) throws Exception {
		return sqlSession.selectList(NAMESPACE+".listCriteria", cri);
	}

	@Override
	public int countPaging(Criteria cri) throws Exception {
		return sqlSession.selectOne(NAMESPACE+".countPaging", cri);
	}

	@Override
	public List<BoardVO> listSearch(SearchCriteria cri) throws Exception {
		return sqlSession.selectList(NAMESPACE+".listSearch", cri);
	}

	@Override
	public int listSearchCount(SearchCriteria cri) throws Exception {
		return sqlSession.selectOne(NAMESPACE+".listSearchCount", cri);
	}

	@Override
	public void updateReplyCnt(int idx, int amount) throws Exception {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("idx", idx);
		paramMap.put("amount", amount);

		sqlSession.update(NAMESPACE + ".updateReplyCnt", paramMap);
	}

	@Override
	public void updateViewCnt(int idx) throws Exception {
		sqlSession.update(NAMESPACE + ".updateViewCnt", idx);
	}

	@Override
	public void addAttach(String fullName) throws Exception {
		sqlSession.insert(NAMESPACE + ".addAttach", fullName);
	}

	@Override
	public List<String> getAttach(int idx) throws Exception {
		return sqlSession.selectList(NAMESPACE + ".getAttach", idx);
	}

	@Override
	public void deleteAttach(int idx) throws Exception {
		sqlSession.delete(NAMESPACE + ".deleteAttach", idx);
	}

	@Override
	public void replaceAttach(String fullName, int idx) throws Exception {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("idx", idx);
		paramMap.put("fullName", fullName);

		sqlSession.insert(NAMESPACE + ".replaceAttach", paramMap);
	}
}