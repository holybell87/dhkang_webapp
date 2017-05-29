package my.api.dao;

import java.util.List;

import my.api.domain.Criteria;
import my.api.domain.ReplyVO;

public interface ReplyDAO {

	public List<ReplyVO> list(int board_idx) throws Exception;

	public void create(ReplyVO vo) throws Exception;

	public void update(ReplyVO vo) throws Exception;

	public void delete(int idx) throws Exception;

	public List<ReplyVO> listPage(int board_idx, Criteria cri) throws Exception;

	public int count(int board_idx) throws Exception;
}