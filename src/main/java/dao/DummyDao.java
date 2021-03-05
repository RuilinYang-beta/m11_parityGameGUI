package dao;

import model.DummyModel;

import java.util.HashMap;
import java.util.Map;

public enum DummyDao {
    instance;

    private Map<String, DummyModel> dummies = new HashMap<String, DummyModel>();

    private DummyDao(){
        DummyModel dm1 = new DummyModel("1", "good");
        DummyModel dm2 = new DummyModel("2", "normal");
        DummyModel dm3 = new DummyModel("3", "bad");

        dummies.put("1", dm1);
        dummies.put("2", dm2);
        dummies.put("3", dm3);
    }

    public Map<String, DummyModel> getModel(){
        return dummies;
    }

}

