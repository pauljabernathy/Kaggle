/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package titanic;

import java.util.List;

import learning.util.Utilities;

/**
 *
 * @author paul
 */
public class Result {
    private int[] indeces;
    private List<String> results;
    private double score;
    
    public Result(int[] indeces, List<String> results, double score) {
        this.indeces = indeces;
        this.results = results;
        this.score = score;
    }

    public int[] getIndeces() {
        return indeces;
    }

    public List<String> getResults() {
        return results;
    }

    public double getScore() {
        return score;
    }
    
    public int compareTo(Result other) {
        if(this.score < other.getScore()) {
            return -1;
        } else if(this.score == other.getScore()) {
            return 0;
        } else {
            return 1;
        }
    }
    
    public String toString() {
        //return Utilities.arrayToString(this.indeces) + " " + this.score;
        return Variable.getEnumNames(this.indeces) + " " + this.score;
    }
}
