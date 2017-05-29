package my.web.controller;

import java.util.List;

import javax.inject.Inject;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import my.web.dao.BoardDAO;
import my.web.domain.BoardVO;
import my.web.domain.Criteria;
import my.web.domain.SearchCriteria;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(
	locations = {"file:src/main/webapp/WEB-INF/spring/**/root-context.xml"})
public class BoardDAOTest {
	@Inject
	private BoardDAO dao;

	private static final Logger logger = LoggerFactory.getLogger(BoardDAOTest.class);

//	@Test
//	public void testCreate() throws Exception {
//		BoardVO board = new BoardVO();
//		board.setTitle("새글 제목입니다.");
//		board.setContent("새글 내용입니다.");
//		board.setWriter("test02");
//
//		dao.create(board);
//	}

//	@Test
//	public void testRead() throws Exception {
//		logger.info(dao.read(1).toString());
//	}

//	@Test
//	public void testListAll() throws Exception {
//		logger.info(dao.listAll().toString());
//	}

//	@Test
//	public void testUpdate() throws Exception {
//		BoardVO board = new BoardVO();
//		board.setIdx(4);
//		board.setTitle("수정된 제목입니다.");
//		board.setContent("수정된 내용입니다.");
//
//		dao.update(board);
//	}

//	@Test
//	public void testDelete() throws Exception {
//		dao.delete(4);
//	}

//	@Test
//	public void testListPage() throws Exception {
//		int page = 3;
//
//		List<BoardVO> list = dao.listPage(page);
//
//		for(BoardVO boardVO : list) {
//			logger.info(boardVO.getIdx() + " : " + boardVO.getTitle());
//		}
//	}

//	@Test
//	public void testListCriteria() throws Exception {
//		Criteria cri = new Criteria();
//		cri.setPage(2);
//		cri.setPerPageNum(20);
//
//		List<BoardVO> list = dao.listCriteria(cri);
//
//		for(BoardVO boardVO : list) {
//			logger.info(boardVO.getIdx() + " : " + boardVO.getTitle());
//		}
//	}

//	@Test
//	public void testURI() throws Exception {
//		// query에 해당하는 문자열들을 추가해서 원하는 URI 생성
//		UriComponents uriComponents =
//				UriComponentsBuilder.newInstance()
//				.path("/board/read")
//				.queryParam("idx", 1)
//				.queryParam("perPageNum", 20)
//				.build();
//
//		logger.info("/board/read?idx=1&perPageNum=20");
//		logger.info(uriComponents.toString());
//	}

//	@Test
//	public void testURI2() throws Exception {
//		// query에 해당하는 문자열들을 추가해서 원하는 URI 생성
//		UriComponents uriComponents =
//				UriComponentsBuilder.newInstance()
//				.path("/{module}/{page}")
//				.queryParam("idx", 1)
//				.queryParam("perPageNum", 20)
//				.build()
//				.expand("board", "read")
//				.encode();
//
//		logger.info("/board/read?idx=1&perPageNum=20");
//		logger.info(uriComponents.toString());
//	}

	@Test
	public void testDynamic1() throws Exception {
		SearchCriteria cri = new SearchCriteria();
		cri.setPage(1);
		cri.setKeyword("50");
		cri.setSearchType("t");

		logger.info("==========================");

		List<BoardVO> list = dao.listSearch(cri);

		for(BoardVO boardVO : list) {
			logger.info(boardVO.getIdx() + " : " + boardVO.getTitle());
		}

		logger.info("==========================");
		logger.info("COUNT: " + dao.listSearchCount(cri));
	}
}