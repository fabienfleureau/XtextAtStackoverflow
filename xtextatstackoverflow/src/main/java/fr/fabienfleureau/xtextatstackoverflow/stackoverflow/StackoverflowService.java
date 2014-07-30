package fr.fabienfleureau.xtextatstackoverflow.stackoverflow;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import com.google.code.stackexchange.client.exception.StackExchangeApiException;
import com.google.code.stackexchange.client.query.QuestionApiQuery;
import com.google.code.stackexchange.client.query.StackExchangeApiQueryFactory;
import com.google.code.stackexchange.common.PagedList;
import com.google.code.stackexchange.schema.Question;

public class StackoverflowService {

	private static StackoverflowService instance;
	private Optional<QuestionApiQuery> query;
	
	private StackoverflowService() {
	}
	
	public static StackoverflowService getInstance() {
		if (instance == null) {
			instance = new StackoverflowService();
		}
		return instance;
	}
	
	public void setQuery(QuestionApiQuery questionApiQuery) {
		query = Optional.ofNullable(questionApiQuery);
	}
	
	public void clear() {
		query = Optional.empty();
	}
	
	public List<Question> execute() {
		try {
			if (query.isPresent()) {
				return query.get().list();
			} 
		} catch (StackExchangeApiException e) {
			e.printStackTrace();
		}
		return Collections.emptyList();
	}
		
	public static void main(String[] args) {
		StackExchangeApiQueryFactory queryFactory = StackExchangeApiQueryFactory.newInstance();
		QuestionApiQuery questionApiQuery = queryFactory.newQuestionApiQuery().withTags("java");
		PagedList<Question> list = questionApiQuery.list();
		for (Question question : list) {
			System.out.println(question);
		}
	}
}
