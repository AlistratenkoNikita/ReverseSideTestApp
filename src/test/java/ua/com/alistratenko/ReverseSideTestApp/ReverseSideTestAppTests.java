package ua.com.alistratenko.ReverseSideTestApp;

import com.mongodb.MongoClient;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.test.context.junit4.SpringRunner;
import ua.com.alistratenko.ReverseSideTestApp.requestSender.RequestSender;

import static org.springframework.test.util.AssertionErrors.assertTrue;


@RunWith(SpringRunner.class)
@SpringBootTest
public class ReverseSideTestAppTests {

	@Autowired
	MongoTemplate db;

	@Autowired
	MongoClient client;

	@Autowired
	RequestSender requestSender;

	@Value("${spring.data.mongodb.database}")
	protected String dbName;


	@Test
	public void checkingDbConnection() {
		assertTrue("DB connection is alive", client.getDatabase(dbName) != null);
	}

	@Test
	public void testVkConnection(){
		assertTrue("Vk connection is alive", requestSender.getUserById(1).getFirsName() != null);
	}

}
