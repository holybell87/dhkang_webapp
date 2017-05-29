package my.web.dao;

import java.util.List;

import my.web.domain.BoardVO;
import my.web.domain.Criteria;
import my.web.domain.SearchCriteria;

public interface BoardDAO {

	// Register
	public void create(BoardVO vo) throws Exception;

	// Read
	public BoardVO read(int idx) throws Exception;

	// Update
	public void update(BoardVO vo) throws Exception;

	// Delete
	public void delete(int idx) throws Exception;

	// All list
	public List<BoardVO> listAll() throws Exception;

	// Pagination
	public List<BoardVO> listPage(int page) throws Exception;
	public List<BoardVO> listCriteria(Criteria cri) throws Exception;
	public int countPaging(Criteria cri) throws Exception;

	// Pagination + Search
	public List<BoardVO> listSearch(SearchCriteria cri) throws Exception;
	public int listSearchCount(SearchCriteria cri) throws Exception;

	public void updateReplyCnt(int idx, int amount) throws Exception;

	public void updateViewCnt(int idx) throws Exception;

	public void addAttach(String fullName) throws Exception;

	public List<String> getAttach(int idx) throws Exception;
	
	public void deleteAttach(int idx) throws Exception;

	public void replaceAttach(String fullName, int idx) throws Exception;
}
