package my.web.service;

import java.util.List;

import my.web.domain.Criteria;
import my.web.domain.ReplyVO;

public interface ReplyService {

	public void addReply(ReplyVO vo) throws Exception;

	public List<ReplyVO> listReply(int board_idx) throws Exception;

	public void modifyReply(ReplyVO vo) throws Exception;

	public void removeReply(int idx) throws Exception;

	public List<ReplyVO> listReplyPage(int board_idx, Criteria cri) throws Exception;

	public int count(int board_idx) throws Exception;
}
