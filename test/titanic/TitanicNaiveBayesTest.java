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

import learning.util.Utilities;
import org.apache.log4j.*;
import java.util.List;
import java.util.ArrayList;

/**
 *
 * @author paul
 */
public class TitanicNaiveBayesTest {
    
    private static Logger logger;
    
    public TitanicNaiveBayesTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
        logger = Logger.getLogger(TitanicNaiveBayesTest.class);
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
    public void testRunTrain() {
    }

    @Test
    public void testRunTest() {
    }

    //@Test
    public void testCheckForUnknowns() {
        logger.info("\ntesting checkForUnknowns()");
        TitanicNaiveBayes instance = new TitanicNaiveBayes();
        List<String> list = null;
        List<String> result = null;
        result = instance.checkForUnknowns(list);
        assertEquals(0, result.size());
        
        list = new ArrayList<String>();
        assertEquals(0, instance.checkForUnknowns(list).size());
        
        list.add("first");
        list.add("second");
        list.add("third");
        result = instance.checkForUnknowns(list);
        assertEquals(3, result.size());
        
        list.add("unknown");
        list.add(Constants.UNKNOWN);
        list.add("sixth");
        result = instance.checkForUnknowns(list);
        assertEquals(6, result.size());
        assertEquals("first", result.get(0));
        assertEquals("0", result.get(3));
        assertEquals("0", result.get(4));
    }
}