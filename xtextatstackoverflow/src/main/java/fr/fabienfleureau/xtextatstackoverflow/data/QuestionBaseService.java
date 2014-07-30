package fr.fabienfleureau.xtextatstackoverflow.data;

import java.util.Collections;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;

import org.apache.commons.collections.map.LRUMap;

import com.google.code.stackexchange.schema.Question;

public class QuestionBaseService implements Observer {

	private static QuestionBaseService instance;
	private Map<Long, Question> questionMap;
	
	@SuppressWarnings("unchecked")
	private QuestionBaseService() {
		questionMap = Collections.synchronizedMap(new LRUMap(100));
	}
	
	public static QuestionBaseService getInstance() {
		if (instance == null) {
			instance = new QuestionBaseService();
		}
		return instance;
	}
	
	public boolean isPresent(Question question) {
		return questionMap.containsKey(question.getQuestionId());
	}
	
	public void add(Question question) {
		questionMap.put(question.getQuestionId(), question);
	}
	
	@Override
	public void update(Observable paramObservable, Object paramObject) {
		if (paramObject instanceof Question) {
			add((Question) paramObject);
		}
	}
}
