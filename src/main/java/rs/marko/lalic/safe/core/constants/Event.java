package rs.marko.lalic.safe.core.constants;

/**
 * Constants used for events
 * Created by Marko Lalic on 8/19/2017.
 */
public class Event {

    public static final String EVENT_TYPE = "eventType";
    public static final String START = "start";
    public static final String END = "end";
    public static final String TOKEN = "token";

    public static final String EVENT_GET_PERSONS = "GetPersons";
    public static final String EVENT_GET_PERSON = "GetPerson";
    public static final String EVENT_ADD_PERSON = "AddPerson";
    public static final String EVENT_UPDATE_PERSON = "UpdatePerson";
    public static final String EVENT_CREATE_FOLDER = "CreateFolder";
    public static final String EVENT_ADD_FILE = "AddFile";

    /**
     * Private empty construct so that constants class cant be instanced
     */
    private Event() {
    }
}
