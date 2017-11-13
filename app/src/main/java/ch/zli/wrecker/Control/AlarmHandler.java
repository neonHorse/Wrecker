package ch.zli.wrecker.Control;

/**
 * Created by admin on 07.11.2017.
 */

public class AlarmHandler {
    private static final AlarmHandler ourInstance = new AlarmHandler();

    public static AlarmHandler getInstance() {
        return ourInstance;
    }

    private AlarmHandler() {
    }
}
