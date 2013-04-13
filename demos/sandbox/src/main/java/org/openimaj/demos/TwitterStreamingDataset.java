package org.openimaj.demos;

import org.openimaj.data.dataset.StreamingDataset;
import org.openimaj.util.api.auth.DefaultTokenFactory;
import org.openimaj.util.api.auth.common.TwitterAPIToken;
import org.openimaj.util.concurrent.ArrayBlockingDroppingQueue;
import org.openimaj.util.concurrent.BlockingDroppingQueue;
import org.openimaj.util.function.Operation;
import org.openimaj.util.stream.BlockingDroppingBufferedStream;

import twitter4j.Status;
import twitter4j.StatusAdapter;
import twitter4j.TwitterStream;
import twitter4j.TwitterStreamFactory;
import twitter4j.conf.Configuration;
import twitter4j.conf.ConfigurationBuilder;

public class TwitterStreamingDataset extends BlockingDroppingBufferedStream<Status> implements StreamingDataset<Status> {
	public TwitterStreamingDataset(TwitterAPIToken token, BlockingDroppingQueue<Status> buffer) {
		super(buffer);

		final TwitterStream twitterStream = new TwitterStreamFactory(makeConfiguration(token)).getInstance();

		twitterStream.addListener(new StatusAdapter() {
			@Override
			public void onStatus(Status status) {
				try {
					register(status);
				} catch (final InterruptedException e) {
					// ignore
				}
			}
		});

		twitterStream.sample();
	}

	private Configuration makeConfiguration(TwitterAPIToken token) {
		final ConfigurationBuilder cb = new ConfigurationBuilder()
				.setOAuthConsumerKey(token.consumerKey)
				.setOAuthConsumerSecret(token.consumerSecret)
				.setOAuthAccessToken(token.accessToken)
				.setOAuthAccessTokenSecret(token.accessSecret);

		return cb.build();
	}

	@Override
	public Status getRandomInstance() {
		return this.next();
	}

	@Override
	public int numInstances() {
		return Integer.MAX_VALUE;
	}

	public static void main(String[] args) {
		final TwitterAPIToken token = DefaultTokenFactory.getInstance().getToken(TwitterAPIToken.class);

		final ArrayBlockingDroppingQueue<Status> buffer = new ArrayBlockingDroppingQueue<Status>(1);
		final TwitterStreamingDataset dataset = new TwitterStreamingDataset(token, buffer);

		dataset.forEach(new Operation<Status>() {
			@Override
			public void perform(Status object) {
				System.out.println("inserted: " + buffer.insertCount() + "\tdropped: " + buffer.dropCount());

				try {
					Thread.sleep(100);
				} catch (final InterruptedException e) {
					e.printStackTrace();
				}
			}
		});
	}
}