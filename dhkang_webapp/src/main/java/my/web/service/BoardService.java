package my.web.service;

import java.util.List;

import my.web.domain.BoardVO;
import my.web.domain.Criteria;
import my.web.domain.SearchCriteria;

public interface BoardService {

	public void regist(BoardVO board) throws Exception;

	public BoardVO read(int idx) throws Exception;

	public void modify(BoardVO board) throws Exception;

	public void remove(int idx) throws Exception;

	public List<BoardVO> listAll() throws Exception;

	public List<BoardVO> listCriteria(Criteria cri) throws Exception;

	public int listCountCriteria(Criteria cri) throws Exception;

	public List<BoardVO> listSearchCriteria(SearchCriteria cri) throws Exception;

	public int listSearchCount(SearchCriteria cri) throws Exception;

	public List<String> getAttach(int idx) throws Exception;
}