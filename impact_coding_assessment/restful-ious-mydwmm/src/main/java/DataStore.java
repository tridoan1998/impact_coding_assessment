import java.util.HashMap;
import java.util.Map;

/**
 * Example DataStore class that provides access to user data.
 * Pretend this class accesses a database.
 */
public class DataStore {

    //Map of names to Person instances.
    private Map<String, User> personMap = new HashMap<>();

    //this class is a singleton and should not be instantiated directly!
    private static DataStore instance = new DataStore();
    public static DataStore getInstance(){
        return instance;
    }

    //private constructor so people know to use the getInstance() function instead
    private DataStore(){

    }

    public User getPerson(String name) {
        return personMap.get(name);
    }

    public void putPerson(User person) {
        personMap.put(person.User(), person);
    }
}