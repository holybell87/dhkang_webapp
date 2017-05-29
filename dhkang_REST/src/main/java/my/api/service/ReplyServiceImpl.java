package my.api.service;

import java.util.List;

import javax.inject.Inject;

import org.springframework.stereotype.Service;

import my.api.domain.Criteria;
import my.api.domain.ReplyVO;
import my.api.dao.ReplyDAO;

@Service
public class ReplyServiceImpl implements ReplyService {

	@Inject
	private ReplyDAO dao;

	@Override
	public void addReply(ReplyVO vo) throws Exception {
		dao.create(vo);
	}

	@Override
	public List<ReplyVO> listReply(int board_idx) throws Exception {
		return dao.list(board_idx);
	}

	@Override
	public void modifyReply(ReplyVO vo) throws Exception {
		dao.update(vo);
	}

	@Override
	public void removeReply(int idx) throws Exception {
		dao.delete(idx);
	}

	@Override
	public List<ReplyVO> listReplyPage(int board_idx, Criteria cri) throws Exception {
		return dao.listPage(board_idx, cri);
	}

	@Override
	public int count(int board_idx) throws Exception {
		return dao.count(board_idx);
	}
}
