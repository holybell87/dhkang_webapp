package my.web.dao;

import java.util.List;

import my.web.domain.Criteria;
import my.web.domain.ReplyVO;

public interface ReplyDAO {

	public List<ReplyVO> list(int board_idx) throws Exception;

	public void create(ReplyVO vo) throws Exception;

	public void update(ReplyVO vo) throws Exception;

	public void delete(int idx) throws Exception;

	public List<ReplyVO> listPage(int board_idx, Criteria cri) throws Exception;

	public int count(int board_idx) throws Exception;

	public int getBoard_idx(int idx) throws Exception;
}