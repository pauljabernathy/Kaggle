/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package titanic;

import java.io.IOException;
import java.util.List;
import java.util.ArrayList;
import static learning.titanic.Titanic.loadCorrectClassifications;
import static learning.titanic.Titanic.loadData;

/**
 *
 * @author paul
 */
public class TitanicTree implements TitanicStrategy {
    
    public static final int TRAIN_SURVIVED_INDEX = 2;
    public static final int TRAIN_CLASS_INDEX = 3;
    public static final int TRAIN_GENDER_INDEX = 6;
    public static final int TRAIN_ISCHILD_INDEX = 14;
    
    public static void main(String[] args) {
        TitanicTree tt = new TitanicTree();
        tt.runTrain("titanic.csv", "tree_train.csv");
        tt.runTest("titanic_test.csv", "tree_test.csv");
    }
    public void runTrain(String inputFile, String outputFile) {
        List<List> data = (List<List>)loadData(inputFile, new int[]{ TRAIN_CLASS_INDEX, TRAIN_GENDER_INDEX, TRAIN_ISCHILD_INDEX }, Titanic.COLUMN_SEPARATOR);
        List<String> correct = loadCorrectClassifications(inputFile, TRAIN_SURVIVED_INDEX, Titanic.COLUMN_SEPARATOR);
        
        List<String> results = classify(data);
        
        int numCorrect = Titanic.compare(correct, results);
        double percentCorrect = (double)numCorrect / (double)(data.size());
        System.out.println("numCorrect = " + numCorrect + ";  " + percentCorrect + "%");
        
        try {
            Titanic.writeToFile(results, outputFile, 892);
        } catch(IOException e) {
            System.err.println(e.getClass() + " writing result file:  " + e.getMessage());
        }
    }
    
    public void runTest(String inputFile, String outputFile) {
         List<List> data = (List<List>)loadData(inputFile, new int[]{ TRAIN_CLASS_INDEX - 1, TRAIN_GENDER_INDEX - 1, TRAIN_ISCHILD_INDEX - 1}, Titanic.COLUMN_SEPARATOR);
          List<String> results = classify(data);
        try {
            Titanic.writeToFile(results, outputFile, 1);
        } catch(IOException e) {
            System.err.println(e.getClass() + " writing result file:  " + e.getMessage());
        }
    }
    
    public List<String> classify(List<List> data) {
        List<String> results = new ArrayList<String>();
        if(data == null || data.isEmpty()) {
            return results;
        }
        for(List list : data) {
            results.add(Integer.toString(classifyOne(list)));
        }
        return results;
    }
    
    public int classifyOne(List<String> row) {
        if(row == null || row.isEmpty()) {
            return 0;
        }
        if(row.get(1).equals("female")) {
            //for females (child or adult), they were more likely to survive in all classes
            //except adult females of 3rd class were more likely to not survive
            if (row.get(0).equals("3") && row.get(2).equals("FALSE")) {
                return 0;
            } else {
                return 1;
            }
        } else {
            //For child males, 1st and 2nd class were more likely to survive but 3rd class was not
            //Adult males all all classes were more likely to not survive.
            /**/if(false) { //!row.get(0).equals("3") && row.get(2).equals("TRUE")) {
                return 1;
            } else if(row.get(2).equals("NA")) {
                return 0;
            }/**/
            return 0;
        }
    }
}
