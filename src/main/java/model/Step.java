package model;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Step {

    // all nodes, all attributes
    // the attribute is pre-determined per algorithm (so that client knows how to parse it)
    private GameStatus game;
    // some nodes, all attributes
    private GameStatus update;
    // the message to display to log steps
    private String msg;

    public Step(){

    }

    public Step(GameStatus game, String msg) {
        this.game = game;
        this.msg = msg;
    }

    public Step(GameStatus game, GameStatus update, String msg) {
        this.game = game;
        this.update = update;
        this.msg = msg;
    }

    public GameStatus getGame() {
        return game;
    }

    public void setGame(GameStatus game) {
        this.game = game;
    }

    public GameStatus getUpdate() {
        return update;
    }

    public void setUpdate(GameStatus update) {
        this.update = update;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String toString(){
        return "Game: " + this.game + ";\nUpdate: " + this.update + ";\nmsg: " + this.msg;
    }
}
