/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package titanic;

import java.io.IOException;
import java.util.List;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

import learning.naivebayes.Classification;
import learning.naivebayes.Classifier;
import learning.naivebayes.io.CSVReader;
import learning.stats.ProbDist;
//import static learning.titanic.Titanic.*;
//import static learning.titanic.Titanic.loadData;

import learning.util.Utilities;

/**
 *
 * @author paul
 */
public class TitanicNaiveBayes implements TitanicStrategy {
    //{ 3, 6, 8, 9, 13, 14 }; class, sex, sibsp, parch, port embarked, ischild
    //2 class
    //5 sex
    //7 sibsp
    //8 parch
    //12 embarked
    //13 ischild
    //public static final int[] TRAIN_INDECES = /**/{ 3, 6, 8, 9, 13, 14 };///**/ new int[]{  3, 6  };
    public static final int[] TRAIN_INDECES = /**/{ 2, 5, 7, 8, 12, 13 };
    public static final int[] TEST_INDECES = /**new int[]{ 1, 3, 11 };///**/new int[]{ 2, 5};//, 7, 12, 13 };
    //public static final int SURVIVED_COLUMN = 1;
    
    public static void main(String[] args) {
        TitanicNaiveBayes titanic = new TitanicNaiveBayes();
        titanic.runTrain("data/titanic.csv", "train_results.csv");
        //titanic.runTest("titanic_test.csv", "test_results_20131110_1.csv", new int[]{ 5, 7, 8, 12, 13 });
        //titanic.runTest("titanic_test.csv", "test_results_20131110_2.csv", new int[]{ 5, 7, 8, 13 });
        //titanic.runTest("titanic_test.csv", "test_results_20131110_3.csv", new int[]{ 2, 5, 7 });
        //titanic.runTest("titanic_test.csv", "test_results_20131110_4.csv", new int[]{ 2, 5, 7, 8, 12 });
        //titanic.runTest("titanic_test.csv", "test_results_20131110_5.csv", new int[]{ 2, 5, 7, 8 });
        
    }
    
    public void runTrain(String inputFile, String outputFile) {
        List<int[]> permutations = Utilities.getCondensedPermutations(TRAIN_INDECES);
        List<Result> results = new ArrayList<Result>();
        //for(int[] a : permutations) {
        for(int i = 0; i < permutations.size(); i++) {
            int[] a = permutations.get(i);
            //if(a.length < 2) continue;
            //Utilities.showArray(a);
            results.add(doOneTrainingRun(inputFile, outputFile, a));
        }
        //doOneTrainingRun(inputFile, outputFile, TRAIN_INDECES);
        //doOneTrainingRun(inputFile, outputFile, new int[]{  9, 13, 14  });
        //doOneTrainingRun(inputFile, outputFile, new int[]{  3, 6  });
        /*for(Result result : results) {
            System.out.println(result);
        }*/
        Collections.sort(results, new ResultComparator());
        //for(Result result : results) {
        for(int i = results.size() - 1; i >= 0; i--) {
            //System.out.println(result);
            System.out.println(results.get(i));
        }
    }
    
    private Result doOneTrainingRun(String inputFile, String outputFile, int[] indeces) {
        //System.out.println("\ndoOneTrainingRun(" + inputFile + ", " + outputFile + ", " + Utilities.arrayToString(indeces));
        //java.io.File f = new java.io.File("");
        //System.out.println(f.getAbsolutePath());
        List<List> data = (List<List>)Titanic.loadData(inputFile, indeces, Titanic.COLUMN_SEPARATOR);
        List<String> correct = Titanic.loadCorrectClassifications(inputFile, Constants.SURVIVED_COLUMN, Titanic.COLUMN_SEPARATOR);
        
        Classifier classifier = new Classifier();
        TitanicDistData titanicDists = new TitanicDistData();
        classifier.setDist(titanicDists.getClassificationDist());
        try {
            ProbDist<Classification> classDist = CSVReader.getDistributions(inputFile, Constants.SURVIVED_COLUMN, indeces, ",");
            classifier.setDist(classDist);
            /**List<Classification> classifications = classDist.getValues();
            for(Classification c : classifications) {
                System.out.println(c.getName());
                List<ProbDist> featureDists = c.getFeatureCPDs();
                for(ProbDist dist : featureDists) {
                    dist.display();
                }
                System.out.println();
            }/**/
            //classifier.setDist(classDist);
        } catch(Exception e) {
            System.err.println(e.getClass() + " in train():  " + e.getMessage());
            return new Result(indeces, new ArrayList<String>(), 0.0);
        }
        //doClassification(data, classifier);
        List<String> result = classifier.classify(data);
        int numCorrect = Titanic.compare(correct, result);
        double percent = (double)numCorrect / (double)data.size() * 100;
        //System.out.println("out of " + data.size() + " " + numCorrect + " or " +  percent + "% were correct ");
        try {
            Titanic.writeToFile(result, outputFile, 1);
        } catch(IOException e) {
            System.err.println(e.getClass() + " writing result file:  " + e.getMessage());
        }
        return new Result(indeces, result, percent);
    }
    
    public void runTest(String inputFile, String outputFile) {
        runTest(inputFile, outputFile, TEST_INDECES);
    }
    
    public void runTest(String inputFile, String outputFile, int[] indeces) {
        //List<List> data = (List<List>)loadData(inputFile, new int[]{ 1, 3, 11 }, Titanic.COLUMN_SEPARATOR);
        List<List> data = (List<List>)Titanic.loadData(inputFile, indeces, Titanic.COLUMN_SEPARATOR);
        Classifier classifier = new Classifier();
        TitanicDistData titanicDists = new TitanicDistData();
        classifier.setDist(titanicDists.getClassificationDist());
        try {
            //ProbDist<Classification> classDist = CSVReader.getDistributions(inputFile, SURVIVED_COLUMN, TEST_INDECES, ",");
            ProbDist<Classification> classDist = CSVReader.getDistributions("titanic.csv", Constants.SURVIVED_COLUMN, indeces, ",");
            List<Classification> classifications = classDist.getValues();
            for(Classification c : classifications) {
                System.out.println(c.getName());
                List<ProbDist> featureDists = c.getFeatureCPDs();
                for(ProbDist dist : featureDists) {
                    dist.display();
                }
                System.out.println();
            }
            classifier.setDist(classDist);
        } catch(Exception e) {
            System.err.println(e.getClass() + " in train:  " + e.getMessage());
            return;
        }
        List<String> result = classifier.classify(data);
        result = checkForUnknowns(result);
        
        try {
            Titanic.writeToFile(result, outputFile, 892);
        } catch(IOException e) {
            System.err.println(e.getClass() + " writing result file:  " + e.getMessage());
        }
    }
    
    private List<String> checkForUnknowns(List<String> list) {
        List<String> result = new ArrayList<String>();
        for(String s : list) {
            if(s.equals("unknown")) {
                result.add("0");
            } else {
                result.add(s);
            }
        }
        return result;
    }
    
    
    
}
