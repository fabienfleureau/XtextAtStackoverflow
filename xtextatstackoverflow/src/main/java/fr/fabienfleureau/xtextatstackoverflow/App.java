package fr.fabienfleureau.xtextatstackoverflow;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import com.google.code.stackexchange.client.query.QuestionApiQuery;
import com.google.code.stackexchange.client.query.StackExchangeApiQueryFactory;

import fr.fabienfleureau.xtextatstackoverflow.data.QuestionBaseService;
import fr.fabienfleureau.xtextatstackoverflow.stackoverflow.CheckQuestionTask;
import fr.fabienfleureau.xtextatstackoverflow.stackoverflow.InitQuestionsTask;
import fr.fabienfleureau.xtextatstackoverflow.stackoverflow.StackoverflowService;
import fr.fabienfleureau.xtextatstackoverflow.twitter.TwitterObserver;

/**
 * Hello world!
 *
 */
public class App {
	private static final int DELAY_BETWEEN_QUESTION_CHECK = 300;
	private static final String XTEXT_TAG = "java";

	public static void main(String[] args) {
		StackExchangeApiQueryFactory queryFactory = StackExchangeApiQueryFactory.newInstance();
		// Init question base
		QuestionBaseService questionBaseService = QuestionBaseService.getInstance();
		// Init query
		QuestionApiQuery questionApiQuery = queryFactory.newQuestionApiQuery().withTags(XTEXT_TAG);
		StackoverflowService.getInstance().setQuery(questionApiQuery);
		initializeQuestionsBase();
		CheckQuestionTask questionTask = new CheckQuestionTask(QuestionBaseService.getInstance());
		// Add twitter Observer
		questionTask.addObserver(questionBaseService);
		questionTask.addObserver(new TwitterObserver());
		Executors.newScheduledThreadPool(1).scheduleAtFixedRate(questionTask, 0, DELAY_BETWEEN_QUESTION_CHECK, TimeUnit.SECONDS);
	}

	private static void initializeQuestionsBase() {
		// Add existing questions
		System.out.println("Initialisation : Add existing questions");
		InitQuestionsTask initQuestionsTask = new InitQuestionsTask();
		ScheduledExecutorService executorService = Executors.newScheduledThreadPool(1);
		executorService.scheduleAtFixedRate(initQuestionsTask, 0, 30, TimeUnit.SECONDS);
		try {
			Boolean initialized = initQuestionsTask.get();
			if (initialized.booleanValue()) {
				System.out.println("Initialisation done.");
			}
		} catch (InterruptedException | ExecutionException e) {
			e.printStackTrace();
		} finally {
			executorService.shutdown();
		}
	}
	
}
