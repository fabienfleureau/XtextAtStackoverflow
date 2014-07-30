package fr.fabienfleureau.xtextatstackoverflow.twitter;

import java.util.Observable;
import java.util.Observer;

import com.google.code.stackexchange.schema.Question;

public class TwitterObserver implements Observer {

	private TwitterService twitterService;

	public TwitterObserver() {
		System.out.println("Twitter service connected");
		twitterService = TwitterService.getInstance();
	}
	
	@Override
	public void update(Observable paramObservable, Object paramObject) {
		if (paramObject instanceof Question) {
			update(paramObservable, (Question) paramObject);
		}
	}

	public void update(Observable paramObservable, Question question) {
		String tweetContent = computeTweet(question);
		twitterService.tweet(tweetContent);
	}

	private String computeTweet(Question question) {
		// TODO get url via question
		return question.getTitle() + " http://stackoverflow.com/questions/"+ question.getQuestionId();
	}
	
}
