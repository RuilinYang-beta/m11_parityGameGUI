package model;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


/**
 * This class tend to save some typing of List<Map<String, String>>.
 * It's intended to be a generic template to describe the overall status / the update of the game.
 */
public class GameStatus extends ArrayList<HashMap<String, String>>{


    public GameStatus(){
        super();
    }

    public GameStatus(HashMap<String, String> map) {
        super();
        this.add(map);
    }


    /**
     * Deep copy this object by serialize and deserialize every HashMap inside it.
     * @return A deep copy of this object.
     */
    public GameStatus getDeepCopy(){
        Gson gson = new Gson();
        GameStatus copy = new GameStatus();

        for (int i = 0; i < this.size(); i++) {
            Map<String, String> map = this.get(i);
            String jsonString = gson.toJson(map);

            Type type = new TypeToken<HashMap<String, String>>(){}.getType();
            HashMap<String, String> mapCopy = gson.fromJson(jsonString, type);
            copy.add(i, mapCopy);
        }

        return copy;
    }
}
