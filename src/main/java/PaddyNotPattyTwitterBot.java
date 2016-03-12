import java.util.Random;

import twitter4j.Query;
import twitter4j.QueryResult;
import twitter4j.Status;
import twitter4j.StatusUpdate;
import twitter4j.Twitter;
import twitter4j.TwitterFactory;

public class PaddyNotPattyTwitterBot {

    private static final long TEN_MINUTES_IN_MILLIS = 600000;
    private static long LAST_TWEET_ID_REPLIED_TO = 0000000000000000001l;
    private static final String KEYWORD_SCANNING_FOR = "St. Patty";
    private static String[] startOfTweetOptions = { "Hey dude,", "Sorry but", "Believe it or not,", "Actually," };

    public static void main(String... args) {
        paddyNotPatty();
    }

    private static void paddyNotPatty() {
        // access the twitter API using the twitter4j.properties file
        Twitter twitter = TwitterFactory.getSingleton();

        // keep the bot running all the time
        while (true) {
            // create a new search
            Query query = new Query(KEYWORD_SCANNING_FOR);
            query.setSinceId(LAST_TWEET_ID_REPLIED_TO);

            try {
                // get the results from that search
                QueryResult queryResult = twitter.search(query);

                // loop through the new tweets that haven't been replied to yet
                for (Status tweet : queryResult.getTweets()) {

                    /*
                     * do this before we tweet, in case something goes wrong we
                     * don't want to try it again
                     */
                    LAST_TWEET_ID_REPLIED_TO = tweet.getId();

                    String tweetToBeSent = getTweetToSend(tweet.getUser().getScreenName());

                    StatusUpdate statusUpdate = new StatusUpdate(tweetToBeSent);
                    statusUpdate.inReplyToStatusId(tweet.getId());
                    twitter.updateStatus(statusUpdate);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            try {
                Thread.sleep(TEN_MINUTES_IN_MILLIS);
            } catch (InterruptedException ie) {
                ie.printStackTrace();
            }
        }
    }

    private static String getTweetToSend(String userNameBeingRepliedTo) {

        int randomIndex = new Random().nextInt(startOfTweetOptions.length);
        String startOfTweet = (startOfTweetOptions[randomIndex]);

        String tweetToBeSent = "@" + userNameBeingRepliedTo + startOfTweet + " it's Paddy not Patty";
        return tweetToBeSent;
    }

}
