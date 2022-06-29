package task;

import javafx.scene.Parent;

public class TaskFX extends Parent {

    String text;
    String hexaColor;
    int user_id;
    int id;

    public TaskFX(int id, String text, String hexaColor) {
        this.id = id;
        this.text = text;
        this.hexaColor = hexaColor;
    }

    public String getHexaColor() {
        return hexaColor;
    }

    public void setHexaColor(String hexaColor) {
        this.hexaColor = hexaColor;
    }

    public int getid() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setText(String newText) {
        this.text = newText;
    }

    public String getText() {
        return text;
    }

    @Override
    public String toString() {
        return "TaskFX {" + "text=" + text + ", hexaColor=" + hexaColor + ", id=" + id + '}';
    }
}
