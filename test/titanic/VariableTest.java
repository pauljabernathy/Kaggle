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
}