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

import org.apache.log4j.*;

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
    
    private ProbDist<Classification> probDist;
    private Logger logger;
    
    public static void main(String[] args) {
        TitanicNaiveBayes titanic = new TitanicNaiveBayes();
        System.out.println(Variable.CLASS);
        System.out.println(Variable.SEX);
        System.out.println(Variable.SIBSP);
        System.out.println(Variable.PARCH);
        System.out.println(Variable.ISCHILD);
        Variable a = Variable.CLASS;
        Variable b = Variable.getInstance(2);
        System.out.println(a.intValue());
        System.out.println(b);
        titanic.runTrain("data/titanic.csv", "train_results.csv");
        //titanic.runTest("data/titanic_test.csv", "test_results_20140125.csv", new int[]{ 5, 7, 8, 12, 13 }, Constants.TRAINING_FILE, new int[]{ 5, 7, 8, 12, 13 });
        //titanic.runTest("titanic_test.csv", "test_results_20131110_2.csv", new int[]{ 5, 7, 8, 13 });
        //titanic.runTest("titanic_test.csv", "test_results_20131110_3.csv", new int[]{ 2, 5, 7 });
        //titanic.runTest("titanic_test.csv", "test_results_20131110_4.csv", new int[]{ 2, 5, 7, 8, 12 });
        //titanic.runTest("titanic_test.csv", "test_results_20131110_5.csv", new int[]{ 2, 5, 7, 8 });
        //titanic.runTest("data/titanic_test.csv", "test_result_5_7_8.csv", new int[]{ 5, 7, 8 }, "data/titanic.csv");
        //titanic.runTest("data/titanic_test.csv", "test_results_{ 5, 7, 8 }.csv", new int[]{ 5, 7, 8 }, "data/titanic.csv");
        /*titanic.runTest("titanic_test.csv", "test_results_20131110_5.csv", new int[]{ 5, 7, 8, 12 }, "data/titanic.csv");
        titanic.runTest("titanic_test.csv", "test_results_20131110_5.csv", new int[]{ 5, 7, 12, 13 }, "data/titanic.csv");
        titanic.runTest("titanic_test.csv", "test_results_20131110_5.csv", new int[]{ 5, 7, 12 }, "data/titanic.csv");
        titanic.runTest("titanic_test.csv", "test_results_20131110_5.csv", new int[]{ 5, 7 }, "data/titanic.csv");
        */
        //titanic.runTest("data/titanic.csv", "data/test_results.csv");
    }
    
    public TitanicNaiveBayes() {
        this.probDist = new TitanicDistData().getClassificationDist();
        logger = Logger.getLogger(this.getClass());
        logger.addAppender(new ConsoleAppender(new PatternLayout(Constants.DEFAULT_LOGGING_PATTERN)));
        logger.setLevel(Level.DEBUG);
    }
    
    public void runTrain(String inputFile, String outputFile) {
        List<int[]> permutations = Utilities.getCondensedPermutations(TRAIN_INDECES);
        List<Result> results = new ArrayList<Result>();
        //for(int[] a : permutations) {
        for(int i = 0; i < permutations.size(); i++) {
            int[] a = permutations.get(i);
            //Utilities.showArray(a);
            results.add(doOneTrainingRun(inputFile, outputFile, a));
        }

        Collections.sort(results, new ResultComparator());
        //for(Result result : results) {
        String testFilename = "";
        for(int i = results.size() - 1; i >= 0; i--) {
            logger.debug(results.get(i));
            //logger.debug(Variable.getEnumNames(results.get(i).getIndeces()) + results.get(i));
            testFilename = "test_results/test_results_" + i + "_" + Utilities.arrayToString(results.get(i).getIndeces()) + ".csv";
            //logger.debug(testFilename);
            runTest("data/titanic_test.csv", testFilename, results.get(i).getIndeces(), "titanic.csv");
        }
    }
    
    private Result doOneTrainingRun(String inputFile, String outputFile, int[] indeces) {
        //logger.debug("\ndoOneTrainingRun(" + inputFile + ", " + outputFile + ", " + Utilities.arrayToString(indeces));
        List<List> data = (List<List>)Titanic.loadData(inputFile, indeces, Titanic.COLUMN_SEPARATOR);
        List<String> correct = Titanic.loadCorrectClassifications(inputFile, Constants.SURVIVED_COLUMN, Titanic.COLUMN_SEPARATOR);
        
        Classifier classifier = new Classifier();
        classifier.setDist(this.getProbDist());
        try {
            ProbDist<Classification> classDist = CSVReader.getDistributions(inputFile, Constants.SURVIVED_COLUMN, indeces, ",");
            classifier.setDist(classDist);
        } catch(Exception e) {
            System.err.println(e.getClass() + " in doOneTrainingRun():  " + e.getMessage());
            return new Result(indeces, new ArrayList<String>(), 0.0);
        }
        List<String> result = classifier.classify(data);
        int numCorrect = Titanic.compare(correct, result);
        double percent = (double)numCorrect / (double)data.size() * 100;
        //logger.debug("out of " + data.size() + " " + numCorrect + " or " +  percent + "% were correct ");
        try {
            Titanic.writeToFile(result, outputFile, 1);
        } catch(IOException e) {
            System.err.println(e.getClass() + " writing result file in doOneTrainingRun():  " + e.getMessage());
        }
        return new Result(indeces, result, percent);
    }
    
    public void runTest(String inputFile, String outputFile) {
        //runTest(inputFile, outputFile, TEST_INDECES, Constants.TRAINING_FILE, TEST_INDECES);
        List<int[]> permutations = Utilities.getCondensedPermutations(TRAIN_INDECES);
        List<Result> results = new ArrayList<Result>();
        //for(int[] a : permutations) {
        String filename = "";
        for(int i = 0; i < permutations.size(); i++) {
            int[] a = permutations.get(i);
            Utilities.showArray(a);
            filename = "test_results_" + Utilities.arrayToString(a) + ".csv";
            logger.debug(filename);
            results.add(doOneTrainingRun(inputFile, outputFile, a));
        }

        Collections.sort(results, new ResultComparator());
        //for(Result result : results) {
        for(int i = results.size() - 1; i >= 0; i--) {
            logger.debug(results.get(i));
        }
    }
    
    public  void runTest(String inputFile, String outputFile, int[] indeces, String referenceFile) {
        runTest(inputFile, outputFile, indeces, referenceFile, indeces);
    }
    
    public void runTest(String inputFile, String outputFile, int[] indeces, String referenceFile, int[] referenceIndeces) {
        //List<List> data = (List<List>)loadData(inputFile, new int[]{ 1, 3, 11 }, Titanic.COLUMN_SEPARATOR);
        List<List> data = (List<List>)Titanic.loadData(inputFile, indeces, Titanic.COLUMN_SEPARATOR);
        Classifier classifier = new Classifier();
        classifier.setDist(this.getProbDist());
        try {
            ProbDist<Classification> classDist = CSVReader.getDistributions(referenceFile, Constants.SURVIVED_COLUMN, referenceIndeces, ",");
            List<Classification> cl = classDist.getValues();
            classifier.setDist(classDist);
        } catch(Exception e) {
            System.err.println(e.getClass() + " in runTest():  " + e.getMessage());
            return;
        }
        List<String> result = classifier.classify(data);
        result = checkForUnknowns(result);
        
        try {
            Titanic.writeToFile(result, outputFile, 892);
        } catch(IOException e) {
            System.err.println(e.getClass() + " writing result file in runTest():  " + e.getMessage());
        }
    }
    
    protected List<String> checkForUnknowns(List<String> list) {
        List<String> result = new ArrayList<String>();
        if(list == null) {
            return result;
        }
        for(String s : list) {
            if(s.equals(Constants.UNKNOWN)) {
                result.add("0");
            } else {
                result.add(s);
            }
        }
        return result;
    }
    
    public ProbDist<Classification> getProbDist() {
        return this.probDist;
    }
    
}
