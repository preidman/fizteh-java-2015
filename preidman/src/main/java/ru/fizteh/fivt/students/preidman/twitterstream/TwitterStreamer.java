package ru.fizteh.fivt.students.preidman.twitterstream;

import com.beust.jcommander.JCommander;
import twitter4j.*;
import java.io.IOException;

import static ru.fizteh.fivt.students.preidman.twitterstream.Constants.MAX_COUNT_OF_TRIES;


public class TwitterStreamer {

    public static void printHelp(JCommander jCommander) {
        jCommander.setProgramName("TwitterParser");
        jCommander.usage();
    }

    public static void main(String[] args) {
        ParameterParser parser = new ParameterParser();
        JCommander jCommander = new JCommander(parser);
        jCommander.parse(args);
        Twitter twitter = new TwitterFactory().getInstance();
        TwitterStream twitterStream = new TwitterStreamFactory().getInstance();
        TwitterService twitterService = new TwitterService(twitter, twitterStream);
        int triesCounter = 0;
        while (triesCounter < MAX_COUNT_OF_TRIES) {
            try {
                if (parser.isHelp()) {
                    printHelp(jCommander);
                } else {
                    twitterService.printTweets(parser);
                }
                break;
            } catch (IOException | TwitterException exception) {
                triesCounter++;
            }
        }
    }
}
