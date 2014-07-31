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
		System.out.println("Updating: " + new Date());
		if (questionBaseService.isPresent()) {
			final QuestionBaseService service = questionBaseService.get();
			long newQuestions = questions
					.stream()
					.filter(question -> !service.isPresent(question))
					.map(question -> {
						setChanged();
						notifyObservers(question);
						return question;
					}).count();
			if (newQuestions == 1) {
				System.out.println(newQuestions + " new question detected.");
			} else if (newQuestions > 1) {
				System.out.println(newQuestions + " new questions detected.");
			}
	
		} else {
			questions.forEach(question -> System.out.println(question.getTitle()));
		}
	}

	public void firstRunThenAddObserver(TwitterObserver twitterObserver) {
		// TODO gérer le cas ou le 1er run n'a pas initialisé le flux
		run();
		addObserver(twitterObserver);
	}
	
}
