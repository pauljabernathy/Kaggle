/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package titanic;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

import org.apache.log4j.*;
import java.util.List;

/**
 *
 * @author paul
 */
public class VariableTest {
    
    private static Logger logger;
    
    public VariableTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
        logger = Logger.getLogger(VariableTest.class);
        logger.addAppender(new ConsoleAppender(new PatternLayout(Constants.DEFAULT_LOGGING_PATTERN)));
        logger.setLevel(Level.DEBUG);
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
    }

    @Test
    public void testValues() {
    }

    @Test
    public void testValueOf() {
    }

    @Test
    public void testGetInstance() {
    }

    @Test
    public void testIntValue() {
    }

    @Test
    public void testGetEnumNames() {
        logger.info("\ntesting getEnumNames()");
        int[] columns = null;
        columns = new int[] { 2, 5 };
        assertEquals("{ UNKNOWN }", Variable.getEnumNames(null));
        assertEquals("{ CLASS, SEX }", Variable.getEnumNames(columns));
        
        columns = new int[] { 2, 3, 5 };
        assertEquals("{ CLASS, LAST_NAME, SEX }", Variable.getEnumNames(columns));
    }
    
    @Test
    public void testIsCategorical() {
        logger.info("\ntesting isCategorical");
        assertEquals(false, Variable.PASSENGERID.isCategorical());
        assertEquals(true, Variable.SURVIVED.isCategorical());
        assertEquals(true, Variable.CLASS.isCategorical());
        assertEquals(false, Variable.FIRST_NAME.isCategorical());
        assertEquals(false, Variable.LAST_NAME.isCategorical());
        assertEquals(true, Variable.SEX.isCategorical());
        assertEquals(false, Variable.SIBSP.isCategorical());
        assertEquals(false, Variable.PARCH.isCategorical());
        assertEquals(false, Variable.TICKET.isCategorical());
        assertEquals(false, Variable.FARE.isCategorical());
        assertEquals(false, Variable.CABIN.isCategorical());
        assertEquals(true, Variable.EMBARKED.isCategorical());
        assertEquals(true, Variable.ISCHILD.isCategorical());
    }
    
    @Test
    public void testGetAllowedVariables() {
        logger.info("\ntesting getAlloedVariables()");
        List result = null;
        
        result = Variable.getAllowedValues(Variable.PASSENGERID);
        assertEquals(0, result.size());
        
        result = Variable.getAllowedValues(Variable.SURVIVED);
        assertEquals(2, result.size());
        assertEquals(0, result.get(0));
        assertEquals(1, result.get(1));
        
        result = Variable.getAllowedValues(Variable.CLASS);
        assertEquals(3, result.size());
        assertEquals(1, result.get(0));
        assertEquals(2, result.get(1));
        assertEquals(3, result.get(2));
        
        result = Variable.getAllowedValues(Variable.SEX);
        assertEquals(2, result.size());
        assertEquals("male", result.get(0));
        assertEquals("female", result.get(1));
        
        result = Variable.getAllowedValues(Variable.FIRST_NAME);
        assertEquals(0, result.size());
        
        result = Variable.getAllowedValues(Variable.LAST_NAME);
        assertEquals(0, result.size());
        
        result = Variable.getAllowedValues(Variable.SIBSP);
        assertEquals(0, result.size());
        
        result = Variable.getAllowedValues(Variable.PARCH);
        assertEquals(0, result.size());
        
        result = Variable.getAllowedValues(Variable.CABIN);
        assertEquals(0, result.size());
        
        result = Variable.getAllowedValues(Variable.FARE);
        assertEquals(0, result.size());
        
        result = Variable.getAllowedValues(Variable.TICKET);
        assertEquals(0, result.size());
        
        result = Variable.getAllowedValues(Variable.UNKNOWN);
        assertEquals(0, result.size());
        
        result = Variable.getAllowedValues(Variable.EMBARKED);
        assertEquals(3, result.size());
        assertEquals("S", result.get(0));
        assertEquals("Q", result.get(1));
        assertEquals("C", result.get(2));
        
        result = Variable.getAllowedValues(Variable.ISCHILD);
        assertEquals(2, result.size());
        assertEquals(true, result.get(0));
        assertEquals(false, result.get(1));
    }
    
    @Test
    public void testGetTotalNumValues() {
        logger.info("\ntesting getTotalNumValues(Variable[] variables)");
        assertEquals(0, Variable.getTotalNumValues(null));
        assertEquals(0, Variable.getTotalNumValues(new Variable[0]));
        assertEquals(3, Variable.getTotalNumValues(new Variable[] { Variable.CLASS }));
        assertEquals(0, Variable.getTotalNumValues(new Variable[] { Variable.FARE }));
        assertEquals(10, Variable.getTotalNumValues(new Variable[] { Variable.CLASS, Variable.SEX, Variable.EMBARKED, Variable.ISCHILD }));
    }
}