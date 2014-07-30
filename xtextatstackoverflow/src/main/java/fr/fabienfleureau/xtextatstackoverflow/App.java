package fr.fabienfleureau.xtextatstackoverflow;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import com.google.code.stackexchange.client.query.QuestionApiQuery;
import com.google.code.stackexchange.client.query.StackExchangeApiQueryFactory;

import fr.fabienfleureau.xtextatstackoverflow.data.QuestionBaseService;
import fr.fabienfleureau.xtextatstackoverflow.stackoverflow.CheckQuestionTask;
import fr.fabienfleureau.xtextatstackoverflow.stackoverflow.StackoverflowService;
import fr.fabienfleureau.xtextatstackoverflow.twitter.TwitterObserver;

/**
 * Hello world!
 *
 */
public class App {
	private static final String XTEXT_TAG = "java";

	public static void main(String[] args) {
		StackExchangeApiQueryFactory queryFactory = StackExchangeApiQueryFactory.newInstance();
		// Init question base
		QuestionBaseService questionBaseService = QuestionBaseService.getInstance();
		// Init query
		QuestionApiQuery questionApiQuery = queryFactory.newQuestionApiQuery().withTags(XTEXT_TAG);
		StackoverflowService.getInstance().setQuery(questionApiQuery);
		ScheduledExecutorService executorService = Executors.newScheduledThreadPool(1);
		CheckQuestionTask questionTask = new CheckQuestionTask(questionBaseService);
		// Add existing questions
		System.out.println("Initialisation : Add existing questions");
		questionTask.addObserver(questionBaseService);
		// Add twitter Observer
		questionTask.firstRunThenAddObserver(new TwitterObserver());
		executorService.scheduleAtFixedRate(questionTask, 0, 30, TimeUnit.SECONDS);
	}
	
}
