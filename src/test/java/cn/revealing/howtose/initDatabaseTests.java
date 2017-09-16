package cn.revealing.howtose;

import cn.revealing.howtose.dao.QuestionDAO;
import cn.revealing.howtose.dao.UserDAO;
import cn.revealing.howtose.model.Question;
import cn.revealing.howtose.model.User;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import java.util.Date;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = HowtoseApplication.class)
public class initDatabaseTests {

	@Autowired
	public UserDAO userDAO;

	@Autowired
	public QuestionDAO questionDAO;
	@Test
	public void initDatabase() {
		for(int i = 0; i< 10; ++i){
			/*User user = new User();
			user.setHeadUrl("xxx");
			user.setName(String.format("USER%d", i));
			user.setPassword("a");
			user.setSalt("a");
			userDAO.addUser(user);

			user.setPassword("XXA");
			userDAO.updatePassword(user);*/

			Question question = new Question();
			question.setCommentCount(i);
			Date date = new Date();
			date.setTime(date.getTime() + 1000 * 3600 * i);
			question.setCreatedDate(date);
			question.setUserId(i + 1);
			question.setContent(String.format("Content%d", i));
			question.setTitle(String.format("Title%d", i));
			questionDAO.addQuestion(question);

			System.out.print(questionDAO.selectLatestQuestions(0, 0, 10));
		}
	}

}
