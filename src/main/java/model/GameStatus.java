package model;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import javax.xml.bind.annotation.XmlRootElement;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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


//    public GameStatus getDeepCopy(GameStatus original){
//
//        List<Map<String, String>> copy = new ArrayList<>();
//        for (int i = 0; i < status.size(); i++) {
//            Map<String, String> map = status.get(i);
//            String jsonString = gson.toJson(map);
////            System.out.println(jsonString);
//
//            Type type = new TypeToken<HashMap<String, String>>(){}.getType();
//            HashMap<String, String> mapCopy = gson.fromJson(jsonString, type);
//            list2.add(i, mapCopy);
//        }
//        list2.get(0).put("id", "99");
//        return null;
//    }
}
