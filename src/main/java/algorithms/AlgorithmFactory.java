package algorithms;

import modelStep.Label;

import java.util.Collection;

public class AlgorithmFactory {

    public static Collection<Label> getLabels(String algorithmName) {
        if (algorithmName.equals("Priority Promotion")) {
            return PriorityPromotion.getLabels();
        } else if (algorithmName.equals("DFI")) {
            return DFI.getLabels();
        } else if (algorithmName.equals("Zielonka")) {
            return Zielonka.getLabels();
        } else {
            // TODO: accommodate customized algorithms
            return null;
        }
    }

    public static Algorithm getAlgorithm(String algorithmName) {
        if (algorithmName.equals("Priority Promotion")) {
            return new PriorityPromotion();
        } else if (algorithmName.equals("DFI")) {
            return new DFI();
        } else if (algorithmName.equals("Zielonka")) {
            return new Zielonka();
        } else {
            // TODO: accommodate customized algorithms
            return null;
        }
    }

}
