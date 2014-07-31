package fr.fabienfleureau.xtextatstackoverflow.twitter;

import java.util.Optional;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class TwitterService {
	
	private static Long delayBetweenTweets = 120L;
	private static TwitterService instance;

	private final Queue<String> pendingTweets = new ConcurrentLinkedQueue<>();
	private final TwitterWrapper twitterWrapper = TwitterWrapper.getInstance();
	
	private Optional<ScheduledExecutorService> currentExecutor = Optional.empty();

	private TwitterService() {
	}
	
	public static TwitterService getInstance() {
		if (instance == null) {
			instance = new TwitterService();
		}
		return instance;
	}
	
	public void addToQueue(String content) {
		pendingTweets.add(content);
		if (!currentExecutor.isPresent() || currentExecutor.get().isTerminated()) {
			ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();
			executor.scheduleAtFixedRate(() -> {
				if (!pendingTweets.isEmpty()) {
					twitterWrapper.tweet(pendingTweets.remove());
				} else {
					executor.shutdown();
				}
			}, 0, delayBetweenTweets, TimeUnit.SECONDS);
			currentExecutor = Optional.of(executor);
		}
	}

	public static void main(String[] args) {
		TwitterService testTwitterService = TwitterService.getInstance();
		testTwitterService.addToQueue("1er tweet");
		testTwitterService.addToQueue("2nd tweet, 2 minutes plus tard");
	}
}
