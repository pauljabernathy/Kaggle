/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package titanic;

import java.util.ArrayList;
import java.util.List;
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
public class TitanicTest {
    
    public TitanicTest() {
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
     * Test of main method, of class Titanic.
     */
    //@Test
    public void testMain() {
        System.out.println("main");
        String[] args = null;
        Titanic.main(args);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of loadData method, of class Titanic.
     */
    @Test
    public void testLoadData() {
        System.out.println("\ntesting loadData()");
        String filename = "titanic.csv";
        int[] columns = new int[]{ 2, 5, 6, 13 };
        columns = new int[] { 2, 5, 7, 8, 12, 13 };
        String columnSeparator = ",";
        Titanic instance = new Titanic();
        List<List<String>> result = null;
        
        result = (List<List<String>>)instance.loadData(null, columns, columnSeparator);
        assertEquals(true, result.isEmpty());
        result = (List<List<String>>)instance.loadData("", columns, columnSeparator);
        assertEquals(true, result.isEmpty());
        
        result = (List<List<String>>)instance.loadData(filename, null, columnSeparator);
        assertEquals(true, result.isEmpty());
        result = (List<List<String>>)instance.loadData(filename, new int[1], columnSeparator);
        assertEquals(false, result.isEmpty());
        result = (List<List<String>>)instance.loadData(filename, new int[0], columnSeparator);
        assertEquals(true, result.isEmpty());
        result = (List<List<String>>)instance.loadData(filename, new int[] { 3, 6 }, columnSeparator);
        assertEquals(false, result.isEmpty());
        
        
        result = (List<List<String>>)instance.loadData(filename, columns, columnSeparator);
        assertEquals(891, result.size());
        for(List<String> list : result) {
            //assertEquals(4, list.size());
            /**/for(String column : list) {
                System.out.print(column + " ");
            }
            System.out.println();/**/
        }
        
        columns = new int[]{  3, 6  };
        result = (List<List<String>>)instance.loadData(filename, columns, columnSeparator);
        assertEquals(891, result.size());
        for(List<String> list : result) {
            assertEquals(2, list.size());
            /**for(String column : list) {
                System.out.print(column + " ");
            }
            System.out.println();/**/
        }
    }
    
    @Test
    public void testLoadCorrectClassifications() {
        System.out.println("\ntesting loadCorrectClassifications()");
        String filename = "titanic.csv";
        Titanic instance = new Titanic();
        List<String> result = instance.loadCorrectClassifications(filename, Constants.SURVIVED_COLUMN, ",");
        assertEquals("0", result.get(0));
        assertEquals("1", result.get(1));
        assertEquals("1", result.get(2));
        assertEquals("1", result.get(3));
        assertEquals("0", result.get(4));
        /**for(String classification : result) {
            System.out.println(classification);
        }/**/
    }

    /**
     * Test of parseLine method, of class Titanic.
     *
    @Test
    public void testParseLine() {
        System.out.println("parseLine");
        String line = "";
        int[] columns = null;
        String columnSeparator = ",";
        Titanic instance = new Titanic();
        ArrayList expResult = null;
        ArrayList<String> result = null;
        //result = instance.parseLine(line, columns, columnSeparator);
        //assertEquals(0, result.size());
        
        line = "3,1,3,\"Heikkinen, Miss. Laina\",female,26,0,0,STON/O2. 3101282,7.925,,S";
        columns = new int[]{ 1, 2, 5 };
        
        result = instance.parseLine(line, columns, columnSeparator);
        for(String column : result) {
            System.out.println(column);
        }
    }*/
}