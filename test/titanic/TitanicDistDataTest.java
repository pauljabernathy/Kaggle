/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package titanic;

import learning.stats.ProbDist;
import java.util.List;
import learning.naivebayes.*;

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
public class TitanicDistDataTest {
    
    public TitanicDistDataTest() {
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
     * Test of setUpData method, of class TitanicDistData.
     */
    @Test
    public void testSetUpData() {
        System.out.println("\ntesting setUpData");
        TitanicDistData instance = new TitanicDistData();
        instance.setUpData();
        
        ProbDist<Classification> classifications = instance.getClassificationDist();
        classifications.display();
    }

    /**
     * Test of getSurvivedDists method, of class TitanicDistData.
     */
    @Test
    public void testGetSurvivedDists() {
        System.out.println("\ntesting getSurvivedDists");
        TitanicDistData instance = new TitanicDistData();
        List expResult = null;
        List result = instance.getSurvivedDists();
        displayDistList(result);
        //assertEquals(expResult, result);
    }

    /**
     * Test of getNotSurvivedDists method, of class TitanicDistData.
     */
    @Test
    public void testGetNotSurvivedDists() {
        System.out.println("\ntesting getNotSurvivedDists");
        TitanicDistData instance = new TitanicDistData();
        List expResult = null;
        List result = instance.getNotSurvivedDists();
        //assertEquals(expResult, result);
        displayDistList(result);
    }

    /**
     * Test of getSurvivedSex method, of class TitanicDistData.
     */
    @Test
    public void testGetSurvivedSex() {
        System.out.println("\ntesting getSurvivedSex");
        TitanicDistData instance = new TitanicDistData();
        ProbDist expResult = null;
        ProbDist result = instance.getSurvivedSex();
        result.display();
    }

    /**
     * Test of getNotSurvivedSex method, of class TitanicDistData.
     */
    @Test
    public void testGetNotSurvivedSex() {
        System.out.println("\ntesting getNotSurvivedSex");
        TitanicDistData instance = new TitanicDistData();
        ProbDist expResult = null;
        ProbDist result = instance.getNotSurvivedSex();
        result.display();
    }

    /**
     * Test of getSurvivedIsChild method, of class TitanicDistData.
     */
    @Test
    public void testGetSurvivedIsChild() {
        System.out.println("\ntesting getSurvivedIsChild");
        TitanicDistData instance = new TitanicDistData();
        ProbDist expResult = null;
        ProbDist result = instance.getSurvivedIsChild();
        result.display();
    }

    /**
     * Test of getNotSurvivedIsChild method, of class TitanicDistData.
     */
    @Test
    public void testGetNotSurvivedIsChild() {
        System.out.println("\ntesting getNotSurvivedIsChild");
        TitanicDistData instance = new TitanicDistData();
        ProbDist expResult = null;
        ProbDist result = instance.getNotSurvivedIsChild();
        result.display();
    }

    /**
     * Test of getSurvivedClass method, of class TitanicDistData.
     */
    @Test
    public void testGetSurvivedClass() {
        System.out.println("\ntesting getSurvivedClass");
        TitanicDistData instance = new TitanicDistData();
        ProbDist expResult = null;
        ProbDist result = instance.getSurvivedClass();
        result.display();
    }

    /**
     * Test of getNotSurvivedClass method, of class TitanicDistData.
     */
    @Test
    public void testGetNotSurvivedClass() {
        System.out.println("\ntesting getNotSurvivedClass");
        TitanicDistData instance = new TitanicDistData();
        ProbDist expResult = null;
        ProbDist result = instance.getNotSurvivedClass();
        result.display();
    }
    
    private void displayDistList(List<ProbDist> list) {
        for(ProbDist current : list) {
            current.display();
        }
    }
}