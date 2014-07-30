package fr.fabienfleureau.xtextatstackoverflow.stackoverflow;

import java.util.Date;
import java.util.List;
import java.util.Observable;
import java.util.Optional;

import com.google.code.stackexchange.schema.Question;

import fr.fabienfleureau.xtextatstackoverflow.data.QuestionBaseService;
import fr.fabienfleureau.xtextatstackoverflow.twitter.TwitterObserver;

public class CheckQuestionTask extends Observable implements Runnable {

	private Optional<QuestionBaseService> questionBaseService;

	public CheckQuestionTask(QuestionBaseService questionBaseService) {
		this.questionBaseService = Optional.ofNullable(questionBaseService);
	}
	
	public CheckQuestionTask() {
		this.questionBaseService = Optional.empty();
	}

	@Override
	public void run() {
		List<Question> questions = StackoverflowService.getInstance().execute();
		System.out.println(new Date());
		if (questionBaseService.isPresent()) {
			final QuestionBaseService service = questionBaseService.get();
			questions.forEach(question -> {
				if (!service.isPresent(question)) {
					setChanged();
					notifyObservers(question);
				}
			});
		} else {
			questions.forEach(question -> System.out.println(question.getTitle()));
		}
	}

	public void firstRunThenAddObserver(TwitterObserver twitterObserver) {
		run();
		addObserver(twitterObserver);
	}
	
}
