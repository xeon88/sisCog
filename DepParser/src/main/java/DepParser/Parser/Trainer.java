package DepParser.Parser;

import DepParser.Model.*;
import DepParser.Utils.PrintUltis;

import java.util.*;

/**
 * Created by Marco Corona on 09/08/2017.
 *
 */


public abstract  class Trainer{

    protected Oracle oracle;
    protected Classifier classifier;
    protected Model model;   
    protected int count;

    public Trainer(int operators) {

        this.model = new Model(operators);
        count = 1;
    }


    public Classifier getClassifier() {
        return classifier;
    }

    public Oracle getOracle() {
        return oracle;
    }

    public Model getModel() {
        return model;
    }

    public int getCount() {
        return count;
    }

    public abstract void train(GoldTree tree, Sentence s);

    public void updates(int [] features, int oracle, int predicted, int count ){
        model.updateWeights(features, oracle, 1);
        model.updateWeights(features, predicted, -1);
        model.updateMeanWeights(features,oracle,count);
        model.updateMeanWeights(features,predicted,-count);
    }


}


