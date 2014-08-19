/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package titanic;

import java.util.List;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

import org.apache.log4j.*;
import toolbox.util.ListArrayUtil;
import learning.genetic.DoubleGenome;
import toolbox.io.CSVReader;
import java.io.IOException;
import learning.neural.Neuron;

/**
 *
 * @author paul
 */
public class TitanicNeuralTest {
    
    private static Logger logger;
    
    public TitanicNeuralTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
        logger = ListArrayUtil.getLogger(TitanicNeuralTest.class, Level.DEBUG);
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
    public void testGetRowsAsLists() {
        logger.info("\ntesting getRowsAsLists()");
        TitanicNeural instance = new TitanicNeural();
        List<List<String>> result = null;
        Variable[] variables = new Variable[ ] { Variable.AGE, Variable.ISCHILD };
        try {
            assertEquals(0, instance.getRowsAsLists(null, null).size());
        } catch(IOException e) {
            logger.error(e.getClass() + " " + e.getMessage());
        }
        try {
            assertEquals(0, instance.getRowsAsLists(null, new Variable[0]).size());
        } catch(IOException e) {
            logger.error(e.getClass() + " " + e.getMessage());
        }
        try {
            assertEquals(0, instance.getRowsAsLists(null, variables).size());
        } catch(IOException e) {
            logger.error(e.getClass() + " " + e.getMessage());
        }
        
        try {
            assertEquals(0, instance.getRowsAsLists("", null).size());
        } catch(IOException e) {
            logger.error(e.getClass() + " " + e.getMessage());
        }
        try {
            assertEquals(0, instance.getRowsAsLists("", new Variable[0]).size());
        } catch(IOException e) {
            logger.error(e.getClass() + " " + e.getMessage());
        }
        try {
            assertEquals(0, instance.getRowsAsLists("", variables).size());
        } catch(IOException e) {
            logger.error(e.getClass() + " " + e.getMessage());
        }
        
        try {
            assertEquals(0, instance.getRowsAsLists("filethatdoesnotexist", null).size());
            
        } catch(IOException e) {
            logger.debug("correctly threw " + e.getClass() + " for non existent file:  " + e.getMessage());
        }
        try {
            assertEquals(0, instance.getRowsAsLists("filethatdoesnotexist", new Variable[0]).size());
        } catch(IOException e) {
            logger.debug("correctly threw " + e.getClass() + " for non existent file:  " + e.getMessage());
        }
        try {
            assertEquals(0, instance.getRowsAsLists("filethatdoesnotexist", variables).size());
        } catch(IOException e) {
            logger.debug("correctly threw " + e.getClass() + " for non existent file:  " + e.getMessage());
        }
        
        try {
            assertEquals(0, instance.getRowsAsLists("titanic.csv", null).size());
        } catch(IOException e) {
            logger.error(e.getClass() + " " + e.getMessage());
        }
        try {
            assertEquals(0, instance.getRowsAsLists("titanic.csv", new Variable[0]).size());
        } catch(IOException e) {
            logger.error(e.getClass() + " " + e.getMessage());
        }
        try {
            result = instance.getRowsAsLists("titanic.csv", variables);
            assertEquals(891, result.size());
            assertEquals(2, result.get(0).size());
            
            variables = new Variable[] { Variable.SURVIVED, Variable.CLASS, Variable.SEX, Variable.SIBSP, Variable.PARCH, Variable.TICKET, Variable.FARE, Variable.CABIN, Variable.EMBARKED, Variable.ISCHILD };
            result = instance.getRowsAsLists("titanic.csv", variables);
            assertEquals(891, result.size());
            assertEquals(10, result.get(0).size());
        } catch(IOException e) {
            logger.error(e.getClass() + " " + e.getMessage());
        }
    }
    
    @Test
    public void testGetTwoLayerNetwork() {
        logger.info("\ntesting getTwoLayerNetwork");
        TitanicNeural instance = new TitanicNeural();
        Neuron[][] network = instance.getTwoLayerNetwork(new DoubleGenome(14));
        assertEquals(3, network.length);
        assertEquals(14, network[0].length);
        for(Neuron n : network[0]) {
            assertEquals(0, n.getNumInputs());
        }
        assertEquals(14, network[1].length);
        for(Neuron n : network[1]) {
            assertEquals(1, n.getNumInputs());
        }
        assertEquals(1, network[2].length);
        assertEquals(14, network[2][0].getNumInputs());
    }
    
    @Test
    public void testGetSensorsAndFirstLayer() {
        logger.info("\ntesting getSensorsAndFirstLayer()");
        TitanicNeural instance = new TitanicNeural();
        Neuron[][] network = instance.getSensorsAndFirstLayer();
        assertEquals(2, network.length);
        assertEquals(14, network[0].length);
        for(Neuron n : network[0]) {
            assertEquals(0, n.getNumInputs());
        }
        assertEquals(14, network[1].length);
        for(Neuron n : network[1]) {
            assertEquals(1, n.getNumInputs());
        }
    }
    
    @Test
    public void testGetThreeLayerNetwork2() {
        logger.info("\ntesting getThreeLayerNetwork2()");
        TitanicNeural instance = new TitanicNeural();
        DoubleGenome genome = new DoubleGenome(225);
        genome.generateRandom();
        int hiddenLayerSize = 15;
        Neuron[][] network = instance.getThreeLayerNetwork2(genome, hiddenLayerSize);
        assertEquals(4, network.length);
        assertEquals(14, network[0].length);
        assertEquals(14, network[1].length);
        assertEquals(15, network[2].length);
        assertEquals(1, network[3].length);
    }
    
    @Test
    public void testInitializeInputs() {
        logger.info("\ntesting initializeInputs()");
        TitanicNeural instance = new TitanicNeural();
        Neuron[][] network = instance.getSensorsAndFirstLayer();
        
        
        assertEquals(network, instance.initializeInputs(null, network));
        network = instance.initializeInputs(null, null);
        if(network == null) {
            fail("output network was null");
        }
        assertEquals(0, network.length);
        
        network = new Neuron[0][];
        network = instance.initializeInputs(null, network);
        assertEquals(0, network.length);
        
        try {
            List<List<String>> rows = CSVReader.getRowsAsLists("titanic.csv", new int[] { 1, 2, 5, 6, 7, 8, 10, 12, 13 }, 900);
            network = instance.initializeInputs(rows.get(0), null);
            if(network == null) {
                fail("outpt nework was null");
            }
            assertEquals(0, network.length);
            
            network = new Neuron[0][];
            network = instance.initializeInputs(null, network);
            assertEquals(0, network.length);
            
            //now with both inputs being good
            network = instance.getSensorsAndFirstLayer();        
            assertEquals(2, network.length);
            for(Neuron[] outer : network) {
                for(Neuron n : outer) {
                    //logger.debug(n.getCachedOutput());
                    n.calculateOutput();
                    assertEquals(Neuron.INACTIVE, n.getCachedOutput(), 0.0);
                }
            }
            network = instance.initializeInputs(rows.get(0), network);
            for(int i = 0; i < network[0].length; i++) {
                logger.debug(network[0][i].getCachedOutput() + "\t" + network[1][i].getCachedOutput());
            }
            assertEquals(Neuron.INACTIVE, network[1][0].getCachedOutput(), 0.0);
            assertEquals(Neuron.INACTIVE, network[1][1].getCachedOutput(), 0.0);
            assertEquals(Neuron.ACTIVE, network[1][2].getCachedOutput(), 0.0);
            assertEquals(Neuron.ACTIVE, network[1][3].getCachedOutput(), 0.0);
            assertEquals(Neuron.INACTIVE, network[1][4].getCachedOutput(), 0.0);
            assertEquals(Neuron.INACTIVE, network[1][5].getCachedOutput(), 0.0);
            assertEquals(Neuron.INACTIVE, network[1][6].getCachedOutput(), 0.0);
            assertEquals(Neuron.INACTIVE, network[1][7].getCachedOutput(), 0.0);
            assertEquals(Neuron.INACTIVE, network[1][8].getCachedOutput(), 0.0);
            assertEquals(Neuron.ACTIVE, network[1][9].getCachedOutput(), 0.0);
            assertEquals(Neuron.INACTIVE, network[1][10].getCachedOutput(), 0.0);
            assertEquals(Neuron.INACTIVE, network[1][11].getCachedOutput(), 0.0);
            assertEquals(Neuron.INACTIVE, network[1][12].getCachedOutput(), 0.0);
            assertEquals(Neuron.ACTIVE, network[1][13].getCachedOutput(), 0.0);
        } catch(IOException e) {
            fail("IOException trying to parse the input file:  " + e.getMessage());
        }
    }
    
    @Test
    public void testResetNetwork() {
        logger.info("\ntesting resetNetwork()");
        TitanicNeural instance = new TitanicNeural();
        Neuron[][] network = null;
        network = instance.resetNetwork(null);
        if(network == null) {
            fail("output was null");
        }
        assertEquals(0, network.length);
        
        network = new Neuron[0][];
        network = instance.resetNetwork(null);
        if(network == null) {
            fail("output was null");
        }
        assertEquals(0, network.length);
        
        network = instance.getSensorsAndFirstLayer();
        for(Neuron[] outer : network) {
            for(Neuron n : outer) {
                assertEquals(Neuron.INACTIVE, n.getCachedOutput(), 0.0);
            }
        }
        
        //now set a neuron to something else
        network[1][1].setOutput(Neuron.ACTIVE);
        assertEquals(Neuron.ACTIVE, network[1][1].getCachedOutput(), 0.0);
        for(int i = 0; i < network.length; i++) {
            for(int j = 0; j < network[i].length; j++) {
                if(i != 1 || j != 1) {
                    assertEquals(Neuron.INACTIVE, network[i][j].getCachedOutput(), 0.0);
                }
            }
        }
        //and make sure the above neuron is inactive again after a reset
        network = instance.resetNetwork(network);
        for(Neuron[] outer : network) {
            for(Neuron n : outer) {
                assertEquals(Neuron.INACTIVE, n.getCachedOutput(), 0.0);
            }
        }
    }
}