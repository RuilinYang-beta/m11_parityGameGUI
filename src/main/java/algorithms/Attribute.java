package algorithms;

import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;
import java.util.Collection;

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