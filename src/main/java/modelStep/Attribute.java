package modelStep;

import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;
import java.util.Collection;

/**
 * A node has some standard attributes: id, strategy, winner, effect(HIGHLIGHT/SHADE/NEUTRAL),
 * the GUI can handle these 4 attributes, but other attributes which may differs per algorithm need
 * to be specified in the getAttributes method of each algorithm class. For example, DFI has "freeze"
 * and "distract", Priority Promotion has "region".
 *
 * The type of each of these attributes can be either "color" or "text" depending on how you want to
 * visualize it. If you choose "color", then you need to specify all possible values of this attribute,
 * so that in the frontend you can choose the color for each possible value; if you choose "text", then
 * there is no need to specify all possible values.
 * Type color is recommended if there's not too many possible values; type text vice versa.
 *
 * In addition, you also need to specify "color" attribute of a node, normally it has two possible values
 * ("even"/"odd") and is displayed as attribute type color. This attribute is separeted from the "winner"
 * attribute because there are many cases where you want to color a node when it doesn't have a winnner yet.
 */
@XmlRootElement
public class Attribute {
    private String name;
    private AttributeType type;
    private Collection<String> values;

    public enum AttributeType {
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

    public Attribute() {

    }

    public Attribute(String name, AttributeType type) {
        this.name = name;
        this.type = type;
        this.values = new ArrayList<>();
    }

    public Attribute(String name, AttributeType type, Collection<String> values) {
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

    public void setType(AttributeType type) {
        this.type = type;
    }

    public AttributeType getType() {
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