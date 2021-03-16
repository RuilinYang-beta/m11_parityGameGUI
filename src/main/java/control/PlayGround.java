package control;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import model.GameStatus;
import model.Step;

import java.lang.reflect.Type;
import java.util.*;

public class PlayGround {

    private static HashMap<String, String> makeMap(List<String> labelList, List<String> dataList){
        HashMap<String, String> result = new HashMap<>();
        for (int i = 0; i < labelList.size(); i++){
            result.put(labelList.get(i), dataList.get(i));
        }
        return result;
    }

    public static void main(String[] args) {
        // deep copy: use gson to seriazlize and deserialize
        GameStatus gs0 = new GameStatus();
        HashMap<String, String> node0 = new HashMap<>();
        node0.put("id", "1");
        node0.put("region", "22");
        node0.put("status", "HIGHLIGHTED");

        HashMap<String, String> node1 = new HashMap<>();
        node1.put("id", "2");
        node1.put("dim1", "a binary dim");
        node1.put("dim2", "a numeric dim");
        node1.put("dim3", "this is a string");

        gs0.add(node0);
        gs0.add(node1);

        // deep copy list1
        GameStatus gs1 = gs0.getDeepCopy();
        gs1.get(0).put("id", "0000");

        System.out.println(gs0);
        System.out.println(gs1);


    }
}
