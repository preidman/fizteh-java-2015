package ru.fizteh.fivt.students.preidman.twitterstream;

import twitter4j.Status;


public class TweetParser {
    public String getTweet(Status status) {
        String result = "@" + status.getUser().getScreenName() + ": ";
        String text = status.getText();
        if (status.isRetweet()) {
            int index = text.indexOf("@");
            text = text.substring(index, text.length());
            index = text.indexOf(" ");
            String retweetedName = text.substring(0, index - 1);
            text = text.substring(index + 1, text.length());
            result += "ретвитнул " + retweetedName + ": ";
        }
        result += text;
        if (!status.isRetweet() && status.getRetweetCount() > 0) {
            WordMatcher matcher = new WordMatcher();
            result += " (" + status.getRetweetCount() + " "
                    + matcher.wordForRetweet(status.getRetweetCount()) + ")";
        }
        return result;
    }
}