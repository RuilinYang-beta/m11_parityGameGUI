package model;

import javax.xml.bind.annotation.XmlRootElement;
import java.util.Collection;

@XmlRootElement
public class Step {

    private Collection<Integer> focus;
    private Action action;
    private Integer type;
    private String msg;   // the message to display to log steps

    public Step(){

    }

    public Step(Collection<Integer> focus, Action action, int type, String msg){
        this.focus = focus;
        this.action = action;
        this.type = type;
        this.msg = msg;

    }

    public enum Action{
        HIGHLIGHT,
        SHADE,
        NEUTRALIZE
    }

    public Collection<Integer> getFocus() {
        return focus;
    }

    public void setFocus(Collection<Integer> focus) {
        this.focus = focus;
    }

    public Action getAction() {
        return action;
    }

    public void setAction(Action action) {
        this.action = action;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String toString() {
        return "" + focus + "; " + action + "; " + type + "; " + msg;
    }
}
