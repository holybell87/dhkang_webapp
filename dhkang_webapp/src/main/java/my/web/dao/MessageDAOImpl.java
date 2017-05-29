package my.web.dao;

import javax.inject.Inject;

import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Repository;

import my.web.domain.MessageVO;

@Repository
public class MessageDAOImpl implements MessageDAO {

	@Inject
	private SqlSession session;

	private static String NAMESPACE = "MessageMapper";

	@Override
	public void create(MessageVO vo) throws Exception {
		session.insert(NAMESPACE + ".create", vo);
	}

	@Override
	public MessageVO readMessage(int mid) throws Exception {
		return session.selectOne(NAMESPACE + ".readMessage", mid);
	}

	@Override
	public void updateState(int mid) throws Exception {
		session.update(NAMESPACE + ".updateState", mid);
	}
}
