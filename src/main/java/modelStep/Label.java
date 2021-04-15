package modelStep;

import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;
import java.util.Collection;

/**
 * A node has some standard labels: id, strategy, winner, effect(HIGHLIGHT/SHADE/NEUTRAL), and color
 * (where color usually represents an assumed winner, it has default displayed color for "even" and "odd").
 * The GUI can handle these 5 labels, but other labels which may differs per algorithm need
 * to be specified in the getLabels method of each algorithm class. For example, DFI has "freeze"
 * and "distract", Priority Promotion has "region".
 *
 * The type of each of these labels can be either "color" or "text" depending on how you want to
 * visualize it. If you choose "color", then you need to specify all possible values of this label,
 * so that in the frontend you can choose the color for each possible value; if you choose "text", then
 * there is no need to specify all possible values.
 * Type color is recommended if there's not too many possible values; type text vice versa.
 */
@XmlRootElement
public class Label {
    private String name;
    // type can be either "color" or "text“
    private LabelType type;
    // if type is "color", you need to specify all possible values
    // if type is "text" then it doesn't matter
    private Collection<String> values;

    public enum LabelType {
        color {
            @Override
            public String toString() {
                return "color";
            }
        },

        text {
            @Override
            public String toString() {
                return "text";
            }
        }
    }

    public Label() {

    }

    public Label(String name, LabelType type) {
        this.name = name;
        this.type = type;
        this.values = new ArrayList<>();
    }

    public Label(String name, LabelType type, Collection<String> values) {
        this.name = name;
        this.type = type;
        this.values = values;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }

    public void setType(LabelType type) {
        this.type = type;
    }

    public LabelType getType() {
        return this.type;
    }

    public void setValues(Collection<String> values) {
        this.values = values;
    }

    public Collection<String> getValues() {
        return this.values;
    }

    public void addValues(String value) {
        this.values.add(value);
    }
}