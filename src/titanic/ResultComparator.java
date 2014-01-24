/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package titanic;
import java.util.Comparator;
/**
 *
 * @author paul
 */
public class ResultComparator implements Comparator<Result> {
    public int compare(Result left, Result right) {       
        return left.compareTo(right);
    }
}
