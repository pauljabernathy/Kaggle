/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package titanic;

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
    
    
}
