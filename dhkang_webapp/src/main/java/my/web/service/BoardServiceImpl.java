package my.web.service;

import java.util.List;

import javax.inject.Inject;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import my.web.domain.BoardVO;
import my.web.domain.Criteria;
import my.web.domain.SearchCriteria;
import my.web.dao.BoardDAO;

@Service
public class BoardServiceImpl implements BoardService {

	@Inject
	private BoardDAO dao;

	@Transactional
	@Override
	public void regist(BoardVO board) throws Exception {
		dao.create(board);

		String[] files = board.getFiles();

		if(files == null) { return; }

		for(String fileName : files) {
			dao.addAttach(fileName);
		}
	}

	@Transactional(isolation=Isolation.READ_COMMITTED)
	@Override
	public BoardVO read(int idx) throws Exception {
		dao.updateViewCnt(idx);
		return dao.read(idx);
	}

	@Transactional
	@Override
	public void modify(BoardVO board) throws Exception {
		/**
		 * 1. 원래의 게시물 수정
		 * 2. 기존 첨부파일 목록 삭제
		 * 3. 새로운 첨부파일 정보 입력
		 */
		dao.update(board);

		int idx = board.getIdx();
		dao.deleteAttach(idx);

		String[] files = board.getFiles();

		if(files == null) { return; }

		for(String fileName : files) {
			dao.replaceAttach(fileName, idx);
		}
	}

	@Transactional
	@Override
	public void remove(int idx) throws Exception {
		dao.deleteAttach(idx);
		dao.delete(idx);
	}

	@Override
	public List<BoardVO> listAll() throws Exception {
		return dao.listAll();
	}

	@Override
	public List<BoardVO> listCriteria(Criteria cri) throws Exception {
		return dao.listCriteria(cri);
	}

	@Override
	public int listCountCriteria(Criteria cri) throws Exception {
		return dao.countPaging(cri);
	}

	@Override
	public List<BoardVO> listSearchCriteria(SearchCriteria cri) throws Exception {
		return dao.listSearch(cri);
	}

	@Override
	public int listSearchCount(SearchCriteria cri) throws Exception {
		return dao.listSearchCount(cri);
	}

	@Override
	public List<String> getAttach(int idx) throws Exception {
		return dao.getAttach(idx);
	}
}