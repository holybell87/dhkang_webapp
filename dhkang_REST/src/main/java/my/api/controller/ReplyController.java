package my.api.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import my.api.domain.Criteria;
import my.api.domain.PageMaker;
import my.api.domain.ReplyVO;
import my.api.service.ReplyService;

@RestController
@RequestMapping("/replies")
public class ReplyController {
	@Inject
	private ReplyService service;

	// 댓글 등록
	@RequestMapping(value = "", method = RequestMethod.POST)
	public ResponseEntity<String> register(@RequestBody ReplyVO vo) {
		ResponseEntity<String> entity = null;

		try {
			service.addReply(vo);
			entity = new ResponseEntity<String>("SUCCESS", HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			entity = new ResponseEntity<String>(e.getMessage(), HttpStatus.BAD_REQUEST);
		}

		return entity;
	}

	// 게시글의 댓글 모두 보기
	@RequestMapping(value = "/all/{board_idx}", method = RequestMethod.GET)
	public ResponseEntity<List<ReplyVO>> list(@PathVariable("board_idx") int board_idx) {
		ResponseEntity<List<ReplyVO>> entity = null;

		try {
			entity = new ResponseEntity<>(service.listReply(board_idx), HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			entity = new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}

		/*
		[
		  {
		    "idx": 3,
		    "board_idx": 59,
		    "replytext": "댓글을 추가합니다.",
		    "replyer": "user_01",
		    "regdate": 1494838707000,
		    "updatedate": 1494838707000
		  },
		  {
		    "idx": 1,
		    "board_idx": 59,
		    "replytext": "댓글 수정 테스트",
		    "replyer": "user01",
		    "regdate": 1494830952000,
		    "updatedate": 1494832698000
		  }
		]
		*/

		return entity;
	}

	// 댓글 수정
	@RequestMapping(value = "/{idx}", method = { RequestMethod.PUT, RequestMethod.PATCH })
	public ResponseEntity<String> update(@PathVariable("idx") int idx, @RequestBody ReplyVO vo) {
		ResponseEntity<String> entity = null;

		try {
			vo.setIdx(idx);
			service.modifyReply(vo);

			entity = new ResponseEntity<String>("SUCCESS", HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			entity = new ResponseEntity<String>(e.getMessage(), HttpStatus.BAD_REQUEST);
		}

		return entity;
	}

	// 댓글 삭제
	@RequestMapping(value = "/{idx}", method = RequestMethod.DELETE)
	public ResponseEntity<String> remove(@PathVariable("idx") int idx) {
		ResponseEntity<String> entity = null;

		try {
			service.removeReply(idx);
			entity = new ResponseEntity<String>("SUCCESS", HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			entity = new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
		}

		return entity;
	}

	@RequestMapping(value = "/{board_idx}/{page}", method = RequestMethod.GET)
	public ResponseEntity<Map<String, Object>> listPage(@PathVariable("board_idx") int board_idx, @PathVariable("page") int page) {
		ResponseEntity<Map<String, Object>> entity = null;

		try {
			Criteria cri = new Criteria();
			cri.setPage(page);

			PageMaker pageMaker = new PageMaker();
			pageMaker.setCri(cri);

			Map<String, Object> map = new HashMap<String, Object>();
			List<ReplyVO> list = service.listReplyPage(board_idx, cri);

			map.put("list", list);

			int replyCount = service.count(board_idx);
			pageMaker.setTotalCount(replyCount);

			map.put("pageMaker", pageMaker);

			entity = new ResponseEntity<Map<String, Object>>(map, HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			entity = new ResponseEntity<Map<String, Object>>(HttpStatus.BAD_REQUEST);
		}
		
		/*
			{
			  "list": [
			    {
			      "idx": 1,
			      "board_idx": 59,
			      "replytext": "댓글 수정 테스트",
			      "replyer": "user01",
			      "regdate": 1494830952000,
			      "updatedate": 1494832698000
			    }
			  ],
			  "pageMaker": {
			    "totalCount": 1,
			    "startPage": 1,
			    "endPage": 1,
			    "prev": false,
			    "next": false,
			    "displayPageNum": 5,
			    "cri": {
			      "page": 1,
			      "perPageNum": 10,
			      "pageStart": 0
			    }
			  }
			}
		*/

		return entity;
	}
}
