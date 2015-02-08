/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package titanic;
import java.util.List;
import java.util.ArrayList;

/**
 *
 * @author paul
 */
public enum Variable {
    //2 class
    //5 sex
    //7 sibsp
    //8 parch
    //12 embarked
    //13 ischild
    UNKNOWN(-1), PASSENGERID(0), SURVIVED(1), CLASS(2), LAST_NAME(3), FIRST_NAME(4), SEX(5), AGE(6), SIBSP(7), PARCH(8), TICKET(9), FARE(10), CABIN(11), EMBARKED(12), ISCHILD(13);
    private int intValue;
    
    Variable(int which) {
        this.intValue = which;
    }
    
    public static Variable getInstance(int intValue) {
        Variable[] values = Variable.values();
        for(int i = 0; i < values.length; i++) {
            if(values[i].intValue == intValue) {
                return values[i];
            }
        }
        return Variable.UNKNOWN;
    }
    
    public int intValue() {
        return this.intValue;
    }
    
    public static String getEnumNames(int[] columns) {
        if(columns == null || columns.length == 0) {
            return "{ " + Variable.UNKNOWN + " }";
        }
        StringBuilder sb = new StringBuilder();
        sb.append("{ ");
        for(int i = 0; i < columns.length - 1; i++) {
            sb.append(Variable.getInstance(columns[i])).append(", ");
        }
        sb.append(Variable.getInstance(columns[columns.length - 1])).append(" }");
        return sb.toString();
    }
    
    public boolean isCategorical() {
        if(this == Variable.SURVIVED || this == Variable.CLASS || this == Variable.SEX || this == Variable.EMBARKED || this == Variable.ISCHILD) {
            return true;
        } else {
            return false;
        }
    }
    
    public List getAllowedValues() {
        return Variable.getAllowedValues(this);
    }
    
    /**
     * gives a list of the values allowed by the input variable if it is categorical and an empty list for non categorical variables; for example, if the input is ISCHILD, returns a list with true and false; if the input is FARE, returns an empty list
     * @param v
     * @return a list with the allowed values
     */
    public static List getAllowedValues(Variable v) {
        List list = new ArrayList();
        if(v == Variable.SURVIVED) {
            list.add(0);
            list.add(1);
        } else if(v == Variable.CLASS) {
            list.add(1);
            list.add(2);
            list.add(3);
        } else if(v == Variable.SEX) {
            list.add("male");
            list.add("female");
        } else if(v == Variable.EMBARKED) {
            list.add("S");
            list.add("Q");
            list.add("C");
        } else if(v == Variable.ISCHILD) {
            list.add(true);
            list.add(false);
        }
        return list;
    }
    
    public static int getTotalNumValues(Variable[] variables) {
        if(variables == null || variables.length == 0) {
            return 0;
        }
        int count = 0;
        for(int i = 0; i < variables.length; i++) {
            count += Variable.getAllowedValues(variables[i]).size();
        }
        return count;
    }
    
}
