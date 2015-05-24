package myvote.dao;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.authentication.UserCredentials;
import org.springframework.data.mongodb.core.MongoTemplate;

import com.mongodb.MongoClient;

@Configuration
public class SpringMongoBean {
	public @Bean MongoTemplate mongoTemplate() throws Exception{
		MongoTemplate mongoTemplate = new MongoTemplate(new MongoClient("ds061148.mongolab.com:61148"), "myvote", new UserCredentials("sumit", "sumit"));
		return mongoTemplate;
	}
}
