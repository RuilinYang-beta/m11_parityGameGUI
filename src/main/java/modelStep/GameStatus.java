package modelStep;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.HashMap;


/**
 * This class tend to save some typing of HashMap<Integer, HashMap<String, String>>。
 * It's intended to be a generic template to describe the overall status / the update of the game.
 */
public class GameStatus extends HashMap<Integer, HashMap<String, String>>{

    public GameStatus(){
        super();
    }


    /**
     * Deep copy this object by serialize and deserialize every inner HashMap.
     * @return A deep copy of this object.
     */
    public GameStatus getDeepCopy(){
        Gson gson = new Gson();
        GameStatus copy = new GameStatus();

        for (var entry : this.entrySet()) {
            int id = entry.getKey();
            var map = entry.getValue();
            String jsonString = gson.toJson(map);

            Type type = new TypeToken<HashMap<String, String>>(){}.getType();
            HashMap<String, String> mapCopy = gson.fromJson(jsonString, type);
            copy.put(id, mapCopy);
        }
        return copy;
    }
}
