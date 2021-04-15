package modelStep;

/**
 * The effect of the color of a node's closest parent node.
 * HIGHLIGHT: display the color with normal strength
 * SHADE: display the color with a degree of transparency
 * NEUTRAL: don't display any color, this is useful to reset a node from colored to not-colored
 */
public enum Effect {
    HIGHLIGHT {
        @Override
        public String toString() {
            return "highlight";
        }
    },

    SHADE {
        @Override
        public String toString() {
            return "shade";
        }
    },

    NEUTRAL {
        @Override
        public String toString() {
            return "neutral";
        }
    }
}
