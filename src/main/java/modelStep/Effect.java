package modelStep;

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
