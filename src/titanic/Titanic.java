/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package titanic;

import java.util.List;
import java.util.ArrayList;
import java.io.*;
import java.util.Arrays;

import learning.naivebayes.*;

/**
 *
 * @author paul
 */
public class Titanic {
    
    public static final String COLUMN_SEPARATOR = ",";
    
    public static void main(String[] args) {
        TitanicNaiveBayes titanic = new TitanicNaiveBayes();
        titanic.runTrain("data/titanic.csv", "train_results.csv");
        //titanic.runTest("titanic_test.csv", "test_results_20131104_2.csv");
    }
    
    
    
    //TODO:  should it be a List<List<String>>?
    /**
     * 
     * @param filename
     * @param columns an array of column indeces to use from the input file (allow you to not use all the columns in the input file)
     * @return 
     */
    public static List<? extends List> loadData(String filename, int[] columns, String columnSeparator) {
        ArrayList<ArrayList> result = new ArrayList<ArrayList>();
        if(!validFileAndColumns(filename, columns)) {
            return result;
        }
        Arrays.sort(columns);
        try {
            BufferedReader reader = new BufferedReader(new FileReader(filename));
            if(reader.ready()) {
                reader.readLine();  //read first line
                //TODO:  test for header
            }
            String line = "";
            String[] vars = null;
            while(reader.ready()) {
                line = reader.readLine();
                result.add(parseLine(line, columns, columnSeparator));
            }
        } catch(IOException e) {
            System.err.println(e.getClass() + " in loadData(" + filename + "):  " + e.getMessage());
        }
        return result;
    }
    
    public static List<String> loadCorrectClassifications(String filename, int column, String columnSeparator) {
        List<List> answers = (List<List>)loadData(filename, new int[] { column }, columnSeparator);
        List result = new ArrayList<String>();
        for(List list : answers) {
            if(list != null && list.get(0) != null) {
                result.add(list.get(0));
            }
        }
        return result;
    }
    
    //TODO:  move to naivebayes.Classifer
    private static ArrayList<String> parseLine(String line, int[] columns, String columnSeparator) {
        ArrayList<String> row = new ArrayList<String>();
        String[] vars = line.split(columnSeparator);
        if(vars == null ||  vars.length < columns[columns.length - 1]) {
            //since columns is sorted, it's last element should be it's largest, which can be no larger than the last column index
            return row;
        }
        for(int column : columns) {
            row.add(scrub(vars[column]));
            //row.add(vars[column]);
        }
        return row;
    }
    
    private static boolean validFileAndColumns(String filename, int[] columns) {
        if(columns == null || columns.length < 1) {
            return false;
        }
        if(filename == null || filename.equals("")) {
            return false;
        }
        return true;
    }
    
    private static String scrub(String input) {
        return input.replace("\"", "");
    }
    
    public static int compare(List<String> a, List<String> b) {
        if(a == null || b == null || a.size() != b.size()) {
            System.out.println("can't compare the lists");
            return 0;
        }
        int numSame = 0;
        for(int i = 0; i < a.size(); i++) {
            if(a.get(i).equals(b.get(i))) {
                //System.out.println(i + 1 + "  CORRECT - " + a.get(i) + " == " + b.get(i));
                numSame++;
            } else {
                //System.out.println(i + 1 + "  WRONG - " + a.get(i) + " != " + b.get(i));
            }
        }
        return numSame;
    }
    
    public static void writeToFile(List<String> list, String filename, int startingId) throws IOException {
        if(list == null || list.isEmpty()) {
            return;
        }
        PrintWriter writer = new PrintWriter(new FileWriter(filename));
        int currentId = startingId;
        writer.println("survived,passengerId");
        for(String current : list) {
            writer.println(current + "," + currentId);
            currentId++;
        }
        writer.flush();
        writer.close();
    }
}
