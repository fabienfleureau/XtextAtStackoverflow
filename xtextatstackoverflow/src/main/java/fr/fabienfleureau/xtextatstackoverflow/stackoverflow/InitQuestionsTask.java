package fr.fabienfleureau.xtextatstackoverflow.stackoverflow;

import java.util.Date;
import java.util.List;
import java.util.concurrent.FutureTask;

import com.google.code.stackexchange.schema.Question;

import fr.fabienfleureau.xtextatstackoverflow.data.QuestionBaseService;

public class InitQuestionsTask extends FutureTask<Boolean> {

	public InitQuestionsTask() {
		super( () -> {
				List<Question> questions = StackoverflowService.getInstance().execute();
				System.out.println("Init: " + new Date());
				final QuestionBaseService service = QuestionBaseService.getInstance();
				long newQuestions = questions
					.stream()
					.filter(question -> !service.isPresent(question))
					.map(question -> {
						service.add(question);
						return question;
					}).count();
//				for (Question question : questions) {
//					if (!service.isPresent(question)) {
//						service.add(question);
//						done = Boolean.TRUE;
//					}
//				}
				if (newQuestions > 0) {
					System.out.println("Initialized " + newQuestions + " questions.");
					return Boolean.TRUE;
				}
				return Boolean.FALSE;
			});
	}

	@Override
	protected void set(Boolean callResult) {
		if (callResult.booleanValue()) {
			super.set(callResult);
		}
	}
}
