/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package titanic;

import java.util.List;
import java.util.ArrayList;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author paul
 */
public class TitanicTreeTest {
    
    public TitanicTreeTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
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

    /**
     * Test of main method, of class TitanicTree.
     */
    //@Test
    public void testMain() {
        System.out.println("main");
        String[] args = null;
        TitanicTree.main(args);
        // TODO review the generated test code and remove the default call to fail.
        //fail("The test case is a prototype.");
    }

    /**
     * Test of runTrain method, of class TitanicTree.
     */
    //@Test
    public void testRunTrain() {
        System.out.println("runTrain");
        String inputFile = "";
        String outputFile = "";
        TitanicTree instance = new TitanicTree();
        instance.runTrain(inputFile, outputFile);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of runTest method, of class TitanicTree.
     */
    //@Test
    public void testRunTest() {
        System.out.println("runTest");
        String inputFile = "";
        String outputFile = "";
        TitanicTree instance = new TitanicTree();
        instance.runTest(inputFile, outputFile);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of classify method, of class TitanicTree.
     */
    @Test
    public void testClassify() {
        System.out.println("classify");
        List<List> data = null;
        TitanicTree instance = new TitanicTree();
        List expResult = null;
        List result = instance.classify(data);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        //fail("The test case is a prototype.");
    }

    /**
     * Test of classifyOne method, of class TitanicTree.
     */
    @Test
    public void testClassifyOne() {
        System.out.println("classifyOne");
        List<String> row = new ArrayList<String>();
        TitanicTree instance = new TitanicTree();
        
        row.add("3");
        row.add("male");
        row.add("FALSE");
        int result = instance.classifyOne(row);
        assertEquals(0, result);
        
        row.remove(2);
        row.add("TRUE");
        result = instance.classifyOne(row);
        assertEquals(0, result);
        
        row.set(0, "1");
        result = instance.classifyOne(row);
        assertEquals(1, result);
    }
}