package my.web.service;

import java.util.List;

import javax.inject.Inject;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import my.web.domain.Criteria;
import my.web.domain.ReplyVO;
import my.web.dao.BoardDAO;
import my.web.dao.ReplyDAO;

@Service
public class ReplyServiceImpl implements ReplyService {

	@Inject
	private ReplyDAO replyDAO;

	@Inject
	private BoardDAO boardDAO;

	@Transactional
	@Override
	public void addReply(ReplyVO vo) throws Exception {
		replyDAO.create(vo);
		boardDAO.updateReplyCnt(vo.getBoard_idx(), 1);
	}

	@Override
	public List<ReplyVO> listReply(int board_idx) throws Exception {
		return replyDAO.list(board_idx);
	}

	@Override
	public void modifyReply(ReplyVO vo) throws Exception {
		replyDAO.update(vo);
	}

	@Transactional
	@Override
	public void removeReply(int idx) throws Exception {
		int board_idx = replyDAO.getBoard_idx(idx);
		replyDAO.delete(idx);
		boardDAO.updateReplyCnt(board_idx, -1);
	}

	@Override
	public List<ReplyVO> listReplyPage(int board_idx, Criteria cri) throws Exception {
		return replyDAO.listPage(board_idx, cri);
	}

	@Override
	public int count(int board_idx) throws Exception {
		return replyDAO.count(board_idx);
	}
}
