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

import java.util.List;
import java.util.ArrayList;
import org.apache.log4j.*;
import toolbox.util.ListArrayUtil;
import learning.genetic.DoubleGenome;
import toolbox.io.CSVReader;
import java.io.IOException;
import java.util.Arrays;
import learning.neural.Neuron;

//TODO:  use TitanicNeural class variable
/**
 *
 * @author paul
 */
public class TitanicNeuralTest {
    
    private static Logger logger;
    private static final int TRAIN_FILE_LENGTH = 891;
    private TitanicNeural instance;
    private static final double ERROR = 0.0000001;
    public TitanicNeuralTest() {
        //instance = new TitanicNeural();
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
        instance = new TitanicNeural();
    }
    
    @After
    public void tearDown() {
    }

    //@Test
    public void testGetRowsAsLists() {
        logger.info("\ntesting getRowsAsLists()");
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
    
    //@Test
    public void testGetSurvived() {
        logger.info("\ntesting getSurvived()");
        int[] result = null;
        
        /*try {
            instance.getSurvived(null);
            fail("should have thrown IOException for instance.getSurvived(null, 1)");
        } catch(IOException e) {
            logger.debug("correctly threw the IOException for instance.getSurvived(null, 1)");
        }
        
        try {
            instance.getSurvived("");
            fail("should have thrown IOException for instance.getSurvived(\"\", 1)");
        } catch(IOException e) {
            logger.debug("correctly threw the IOException for instance.getSurvived(\"\", 1)");
        }
        
        try {
            instance.getSurvived("filethatdoesnotexist");
            fail("should have thrown IOException for instance.getSurvived(\"filethatdoesnotexist\", 1)");
        } catch(IOException e) {
            logger.debug("correctly threw the IOException for instance.getSurvived(\"filethatdoesnotexist\", 1)");
        }
        */
        try {
            /**toolbox.stats.DataList dl = CSVReader.getSingleColumn(TitanicNeural.TITANIC_FILE_NAME, TitanicNeural.SURVIVED_INDEX, TitanicNeural.COLUMN_SEPARATOR);
            logger.debug(dl.size());
            for(int i = 0; i < dl.size(); i++) {
                System.out.print(dl.get(i) + " ");
            }
            logger.debug("");
            List list = dl.getData();
            for(int i = 0; i < list.size(); i++) {
                System.out.print(list.get(i) + " ");
            }/**/
            result = instance.getSurvived();
            if(result == null) {
                fail("result was null");
            }
            assertEquals(TRAIN_FILE_LENGTH, result.length);
            assertEquals(0, result[0]);
            assertEquals(1, result[1]);
            assertEquals(1, result[2]);
            assertEquals(1, result[3]);
            assertEquals(0, result[4]);
            assertEquals(0, result[5]);
            
            assertEquals(0, result[886]);
            assertEquals(1, result[887]);
        } catch(IOException e) {
            fail(e.getClass() + " in testGetSurvived:  " + e.getMessage());
        }
        
    }
    
    //@Test
    public void testGetTwoLayerNetwork() {
        logger.info("\ntesting getTwoLayerNetwork");
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
    
    //@Test
    public void testGetSensorsAndFirstLayer() {
        logger.info("\ntesting getSensorsAndFirstLayer()");
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
    
    //@Test
    public void testGetThreeLayerNetwork2() {
        logger.info("\ntesting getThreeLayerNetwork2()");
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
    public void testGetThreeLayerNetwork3() {
        /*logger.info("\ntesting  getThreeLayerNetwork3(int hiddenLayerSize, Variable[] variables)");
        Neuron[][] result = null;
        result = instance.getThreeLayerNetwork3(0, null);
        assertEquals(4, result.length);
        assertEquals(0, result[0].length);
        assertEquals(0, result[1].length);
        assertEquals(0, result[2].length);
        assertEquals(1, result[3].length);
        
        result = instance.getThreeLayerNetwork3(20, null);
        assertEquals(4, result.length);
        assertEquals(0, result[0].length);
        assertEquals(0, result[1].length);
        assertEquals(20, result[2].length);
        assertEquals(1, result[3].length);
        
        Variable[] variables = new Variable[0];
        result = instance.getThreeLayerNetwork3(20, variables);
        assertEquals(4, result.length);
        assertEquals(0, result[0].length);
        assertEquals(0, result[1].length);
        assertEquals(20, result[2].length);
        assertEquals(1, result[3].length);
        
        variables = new Variable[] { Variable.CLASS };
        result = instance.getThreeLayerNetwork3(3, variables);
        assertEquals(4, result.length);
        assertEquals(4, result[0].length);
        assertEquals(4, result[1].length);
        assertEquals(3, result[2].length);
        assertEquals(1, result[3].length);
        
        result = instance.getThreeLayerNetwork3(5, variables);
        assertEquals(4, result.length);
        assertEquals(4, result[0].length);
        assertEquals(4, result[1].length);
        assertEquals(5, result[2].length);
        assertEquals(1, result[3].length);
        
        variables = new Variable[] { Variable.CLASS, Variable.SEX, Variable.EMBARKED, Variable.ISCHILD };
        int hiddenLayerSize = 5;
        result = instance.getThreeLayerNetwork3(hiddenLayerSize, variables);
        assertTrue("result length was not 4", result.length == 4);
        assertEquals(14, result[0].length);
        assertEquals(14, result[1].length);
        assertEquals(5, result[2].length);
        assertEquals(1, result[3].length);*/
    }
    
    //@Test
    public void testGetSensorsAndFirstLayer_Variable_array() {
        //logger.info("\ntesting getSensorsAndFirstLayer(Variable[] variables)");
        /*Neuron[][] result = null;
        Variable[] variables = null;
        
        result = instance.getSensorsAndFirstLayer(variables);
        if(result == null) {
            fail("result was null");
        }
        assertEquals(2, result.length);
        assertEquals(0, result[0].length);
        assertEquals(0, result[1].length);
        testNumInputs(result[0], result[1]);
                
        variables = new Variable[0];
        result = instance.getSensorsAndFirstLayer(variables);
        if(result == null) {
            fail("result was null");
        }
        assertEquals(2, result.length);
        assertEquals(0, result[0].length);
        assertEquals(0, result[1].length);
        testNumInputs(result[0], result[1]);
        
        variables = new Variable[] { Variable.SEX };
        result = instance.getSensorsAndFirstLayer(variables);
        assertEquals(2, result.length);
        assertEquals(3, result[0].length);
        assertEquals(3, result[1].length);
        testNumInputs(result[0], result[1]);
        
        variables = new Variable[] { Variable.FARE };
        result = instance.getSensorsAndFirstLayer(variables);
        assertEquals(2, result.length);
        assertEquals(2, result[0].length);
        assertEquals(2, result[1].length);
        testNumInputs(result[0], result[1]);
        
        variables = new Variable[] { Variable.SURVIVED, Variable.CLASS, Variable.SEX, Variable.AGE, Variable.SIBSP, Variable.PARCH, Variable.FARE, Variable.EMBARKED, Variable.ISCHILD };
        result = instance.getSensorsAndFirstLayer(variables);
        assertEquals(2, result.length);
        assertEquals(25, result[0].length);
        assertEquals(25, result[1].length);
        testNumInputs(result[0], result[1]);
        
        variables = new Variable[] { Variable.CLASS, Variable.SEX, Variable.AGE, Variable.SIBSP, Variable.PARCH, Variable.FARE, Variable.EMBARKED, Variable.ISCHILD };
        result = instance.getSensorsAndFirstLayer(variables);
        assertEquals(2, result.length);
        assertEquals(22, result[0].length);
        assertEquals(22, result[1].length);
        testNumInputs(result[0], result[1]);
        */
    }
    
    private void testNumInputs(Neuron[] sensors, Neuron[] neurons) {
        for(Neuron n : sensors) {
            assertEquals(0, n.getNumInputs());
        }
        for(Neuron n : neurons) {
            assertEquals(1, n.getNumInputs());
        }
    }
    
    @Test
    public void testDoOneRun2() {
        logger.info("\ntesting doOneRun2()");
        List<String> l = Arrays.asList(new String[] { "one", "two" });
        logger.debug(l.size());
        logger.debug(l.get(0));
        logger.debug(l.get(1));
        
        List<List<String>> rows = null;
        Variable[] variables = null;
        Neuron[][] network = null;
        DoubleGenome genome = null;
        //TODO:  test for null, empty inputs
        
        rows = new ArrayList<List<String>>();
        rows.add(Arrays.asList(new String[] { "male" }));
        rows.add(Arrays.asList(new String[] { "female" }));
        rows.add(Arrays.asList(new String[] { "1" }));
        
        variables = new Variable[] { Variable.SEX };
        //instance.getSensorsAndFirstLayer(variables);
        //this.testGetSensorsAndFirstLayer_Variable_array();
        network = instance.getThreeLayerNetwork3(2, variables);
        genome = new DoubleGenome(8);
        genome.setRawData(new double[] { 0.0, .9, .9, .0, .0, .0, .9, 0.0 });
        genome.setRawData(new double[] { 0.0, .9, .0, .9, .0, .0, .9, 0.0 });
        
        int[] result = null;
        result = instance.doOneRun2(rows, network, variables, genome);
        logger.debug(ListArrayUtil.arrayToString(result));
    }
    
    //@Test
    public void testGetInputActivationForRow() {
        logger.info("\ntesting getInputActivationForRow(List<String> row, Variable[] variables)");
        Variable[] variables = null;
        List<String> row = null;
        
        //empty/null values
        assertEquals(0, instance.getInputActivationForRow(row, variables).length);
        row = new ArrayList<String>();
        assertEquals(0, instance.getInputActivationForRow(row, variables).length);
        variables = new Variable[0];
        assertEquals(0, instance.getInputActivationForRow(row, variables).length);
        row = null;
        assertEquals(0, instance.getInputActivationForRow(row, variables).length);
        
        //row and variables not same size
        variables = new Variable[] { Variable.CLASS, Variable.EMBARKED };
        row = new ArrayList<String>();
        row.add("1");
        row.add("Q");
        row.add("male");
        assertEquals(0, instance.getInputActivationForRow(row, variables).length);
        
        variables = new Variable[] { Variable.CLASS, Variable.EMBARKED, Variable.SEX };
        row = new ArrayList<String>();
        row.add("1");
        row.add("Q");
        assertEquals(0, instance.getInputActivationForRow(row, variables).length);
        
        //values in rows not all matching up with variables - should give unknowns
        variables = new Variable[] { Variable.CLASS, Variable.EMBARKED };
        row = new ArrayList<String>();
        row.add("male");    //0, 0, 1 for unknown
        row.add("Q");       //0, 1, 0, 0 for Queenstown
        assertEquals(true, ListArrayUtil.haveSameElements(new double[] { 0.0, 0.0, 0.0, 1.0, 0.0, 1.0, 0.0, 0.0 }, instance.getInputActivationForRow(row, variables)));
        
        //"correct" input
        row.set(0, "2");
        assertEquals(true, ListArrayUtil.haveSameElements(new double[] { 0.0, 1.0, 0.0, 0.0, 0.0, 1.0, 0.0, 0.0 }, instance.getInputActivationForRow(row, variables)));
    }
    
    //@Test
    public void testGetInputActivationForVariable() {
        logger.info("getInputActivationForVariable(Variable variable, String input)");
        double[] result = null;
        //assertEquals(result.length, instance.getInputActivationForVariable(Variable.UNKNOWN, "")); //TODO:
        //result = instance.getInputActivationForVariable(Variable.CLASS, "");
        //assertEquals(4, result.length);
        //logger.debug(ListArrayUtil.arrayToString(result));
        assertEquals(true, ListArrayUtil.haveSameElements(new double[] { 0.0, 0.0, 0.0, 1.0 }, instance.getInputActivationForVariable(Variable.CLASS, null)));
        assertEquals(true, ListArrayUtil.haveSameElements(new double[] { 0.0, 0.0, 0.0, 1.0 }, instance.getInputActivationForVariable(Variable.CLASS, "")));
        assertEquals(true, ListArrayUtil.haveSameElements(new double[] { 0.0, 0.0, 0.0, 1.0 }, instance.getInputActivationForVariable(Variable.CLASS, "something else")));
        assertEquals(true, ListArrayUtil.haveSameElements(new double[] { 1.0, 0.0, 0.0, 0.0 }, instance.getInputActivationForVariable(Variable.CLASS, "1")));
        assertEquals(true, ListArrayUtil.haveSameElements(new double[] { 0.0, 1.0, 0.0, 0.0 }, instance.getInputActivationForVariable(Variable.CLASS, "2")));
        assertEquals(true, ListArrayUtil.haveSameElements(new double[] { 0.0, 0.0, 1.0, 0.0 }, instance.getInputActivationForVariable(Variable.CLASS, "3")));
        
        assertEquals(true, ListArrayUtil.haveSameElements(new double[] { 0.0, 0.0, 1.0 }, instance.getInputActivationForVariable(Variable.SEX, null)));
        assertEquals(true, ListArrayUtil.haveSameElements(new double[] { 0.0, 0.0, 1.0 }, instance.getInputActivationForVariable(Variable.SEX, "")));
        assertEquals(true, ListArrayUtil.haveSameElements(new double[] { 0.0, 0.0, 1.0 }, instance.getInputActivationForVariable(Variable.SEX, "neutral")));
        assertEquals(true, ListArrayUtil.haveSameElements(new double[] { 1.0, 0.0, 0.0 }, instance.getInputActivationForVariable(Variable.SEX, "male")));
        assertEquals(true, ListArrayUtil.haveSameElements(new double[] { 0.0, 1.0, 0.0 }, instance.getInputActivationForVariable(Variable.SEX, "female")));
        //TODO:  deal with spaces, caps, etc.
        //assertEquals(true, ListArrayUtil.haveSameElements(new double[] { 1.0, 0.0, 0.0 }, instance.getInputActivationForVariable(Variable.SEX, "MALE")));
        //assertEquals(true, ListArrayUtil.haveSameElements(new double[] { 0.0, 1.0, 0.0 }, instance.getInputActivationForVariable(Variable.SEX, "FEMALE")));
        
        assertEquals(true, ListArrayUtil.haveSameElements(new double[] { 0.0, 0.0, 0.0, 1.0 }, instance.getInputActivationForVariable(Variable.EMBARKED, null)));
        assertEquals(true, ListArrayUtil.haveSameElements(new double[] { 0.0, 0.0, 0.0, 1.0 }, instance.getInputActivationForVariable(Variable.EMBARKED, "")));
        assertEquals(true, ListArrayUtil.haveSameElements(new double[] { 0.0, 0.0, 0.0, 1.0 }, instance.getInputActivationForVariable(Variable.EMBARKED, "tahiti")));
        assertEquals(true, ListArrayUtil.haveSameElements(new double[] { 1.0, 0.0, 0.0, 0.0 }, instance.getInputActivationForVariable(Variable.EMBARKED, "S")));
        assertEquals(true, ListArrayUtil.haveSameElements(new double[] { 0.0, 1.0, 0.0, 0.0 }, instance.getInputActivationForVariable(Variable.EMBARKED, "Q")));
        assertEquals(true, ListArrayUtil.haveSameElements(new double[] { 0.0, 0.0, 1.0, 0.0 }, instance.getInputActivationForVariable(Variable.EMBARKED, "C")));
        
        assertEquals(true, ListArrayUtil.haveSameElements(new double[] { 0.0, 0.0, 1.0 }, instance.getInputActivationForVariable(Variable.ISCHILD, null)));
        assertEquals(true, ListArrayUtil.haveSameElements(new double[] { 0.0, 0.0, 1.0 }, instance.getInputActivationForVariable(Variable.ISCHILD, "")));
        assertEquals(true, ListArrayUtil.haveSameElements(new double[] { 0.0, 0.0, 1.0 }, instance.getInputActivationForVariable(Variable.ISCHILD, "neither")));
        assertEquals(true, ListArrayUtil.haveSameElements(new double[] { 1.0, 0.0, 0.0 }, instance.getInputActivationForVariable(Variable.ISCHILD, "TRUE")));
        assertEquals(true, ListArrayUtil.haveSameElements(new double[] { 1.0, 0.0, 0.0 }, instance.getInputActivationForVariable(Variable.ISCHILD, "true")));
        assertEquals(true, ListArrayUtil.haveSameElements(new double[] { 0.0, 1.0, 0.0 }, instance.getInputActivationForVariable(Variable.ISCHILD, "FALSE")));
        assertEquals(true, ListArrayUtil.haveSameElements(new double[] { 0.0, 1.0, 0.0 }, instance.getInputActivationForVariable(Variable.ISCHILD, "false")));
    }
    
    //@Test
    public void testUpdateNetwork() {
        logger.info("\ntesting updateNetwork()");
        DoubleGenome genome = new DoubleGenome(20);
        Neuron[][] network = null;
        network = instance.updateNetworkWeights(network, genome);
        assertEquals(0, network.length);
        
        Neuron sensor1 = new Neuron();
        Neuron sensor2 = new Neuron();
        Neuron input1 = new Neuron();
        Neuron input2 = new Neuron();
        Neuron hidden1 = new Neuron();
        Neuron hidden2 = new Neuron();
        Neuron output = new Neuron();
        
        network = new Neuron[4][];
        network[0] = new Neuron[] { sensor1, sensor2 };
        network[1] = new Neuron[] { input1, input2 };
        network[2] = new Neuron[] { hidden1, hidden2 };
        network[3] = new Neuron[] { output };
        
        input1.addInput(sensor1, 1.0);
        input2.addInput(sensor2, 1.0);
        hidden1.addInput(input1, .2);
        hidden2.addInput(input2, .5);
        output.addInput(hidden1, .5);
        output.addInput(hidden2, .5);
        sensor1.setOutput(Neuron.ACTIVE);
        sensor2.setOutput(Neuron.ACTIVE);
        assertEquals(Neuron.INACTIVE, output.calculateOutput(), 0.0);
        
        //genome.setRawData(new double[] { 1.0, 1.0, 1.0, 1.0, 1.0 } );
        //network = instance.updateNetworkWeights(network, genome);
        genome.setRawData(new double[] { 1.0, 1.0, 1.0, 1.0, 1.0, 1.0 } );
        network = instance.updateNetwork(network, genome);
        assertEquals(Neuron.ACTIVE, output.calculateOutput(), 0.0);
        
        sensor1.setOutput(Neuron.INACTIVE);
        sensor2.setOutput(Neuron.INACTIVE);
        network = instance.updateNetwork(network, genome);
        assertEquals(Neuron.INACTIVE, output.calculateOutput(), 0.0);
        
        
        //using some data observed in a run of the program
        Variable[] variables = new Variable[] { Variable.SURVIVED, Variable.CLASS, Variable.SEX, Variable.AGE, Variable.SIBSP, Variable.PARCH, Variable.FARE, Variable.EMBARKED, Variable.ISCHILD };
        variables = new Variable[] { Variable.CLASS, Variable.SEX, Variable.EMBARKED, Variable.ISCHILD };
        int hiddenLayerSize = 5;
        network = instance.getThreeLayerNetwork3(hiddenLayerSize, variables);
        double[] activation = new double[] { 0.0, 0.0, 1.0, 0.0, 0.0, 1.0, 0.0, 0.0, 1.0, 0.0, 0.0, 0.0, 1.0, 0.0 };
        for(int i = 0; i < activation.length; i++) {
            network[0][i].setOutput(activation[i]);
        }
        double[] genomeValues = new double[] { 0.3665746551330671, 0.9337695517194033, 0.8781619632697617, 0.22530194663457082, 0.8391646370656696, 0.4967701499098526, 0.8836718052083592, 0.3046202864659736, 0.20101257558696584, 0.09267380106402234, 0.15977769145395349, 0.7544197024544063, 0.033549011637036164, 0.4662207870219838, 0.6809597152964851, 0.529784829822647, 0.3857838395059644, 0.9122832408306667, 0.8961174284211463, 0.08377338927253608, 0.24680625269757017, 0.4258555828802266, 0.8906704290507254, 0.30265302176774456, 0.38946728693938515, 0.05325223361146658, 0.7657426473176343, 0.9698141685631386, 0.13569017772387781, 0.38192071905889036, 0.42794275384229674, 0.4677401250588612, 0.5440496295723464, 0.3767869620430372, 0.8865725353573783, 0.776048038208439, 0.6267063039327218, 0.44780611660087777, 0.7146660865194727, 0.07849776174128287, 0.4654776106743317, 0.7164016140227971, 0.5460828160008835, 0.4529980733883999, 0.5157719183576883, 0.9377314832077901, 0.3529470343912142, 0.18602246417825463, 0.9623083903648788, 0.43122974606485687, 0.7760481738090799, 0.2264798446218801, 0.7219895377330608, 0.6736282686053243, 0.9653851614694262, 0.1739680778547471, 0.576989447232784, 0.916671987775024, 0.2288591418062551, 0.9824709906056137, 0.9650658110431686, 0.30599860351919084, 0.2841891388990443, 0.25294286135327027, 0.15429891854148725, 0.49816153727182455, 0.062445194079888244, 0.5868562401470424, 0.8859064312046472, 0.6995325398406432, 0.8211644034165156, 0.9776728180789883, 0.08147796062967982, 0.3838285732801857, 0.790834866198532 };
        for(int i = 0; i < genomeValues.length; i++) {
            //genomeValues[i] = 1.0;
        } 
        logger.debug(ListArrayUtil.arrayToString(genomeValues));
        genome.setRawData(genomeValues);
        network = instance.updateNetwork(network, genome);
        for(int i = 0; i < network.length; i++) {
            //logger.debug(ListArrayUtil.arrayToString(network[i]));
            double[] nums = new double[network[i].length];
            for(int j = 0; j < network[i].length; j++) {
                nums[j] = network[i][j].getCachedOutput();
            }
            logger.debug(ListArrayUtil.arrayToString(nums));
        }
    }
    
    //TODO:  make a way to test the actual values of the weights directly
    //@Test
    public void testUpdateNetworkWeights() {
        logger.info("\ntesting updateNetworkWeights()");
        DoubleGenome genome = new DoubleGenome(20);
        Neuron[][] network = null;
        network = instance.updateNetworkWeights(network, genome);
        assertEquals(0, network.length);
        
        /**Variable[] variables = new Variable[] { Variable.CLASS };
        network = instance.getThreeLayerNetwork3(3, variables);
        assertEquals(4, network.length);
        assertEquals(4, network[0].length);
        assertEquals(4, network[1].length);
        assertEquals(3, network[2].length);
        assertEquals(1, network[3].length);/**/
        
        Neuron sensor1 = new Neuron();
        Neuron sensor2 = new Neuron();
        Neuron input1 = new Neuron();
        Neuron input2 = new Neuron();
        Neuron hidden1 = new Neuron();
        Neuron hidden2 = new Neuron();
        Neuron output = new Neuron();
        
        network = new Neuron[4][];
        network[0] = new Neuron[] { sensor1, sensor2 };
        network[1] = new Neuron[] { input1, input2 };
        network[2] = new Neuron[] { hidden1, hidden2 };
        network[3] = new Neuron[] { output };
        
        input1.addInput(sensor1, 1.0);
        input2.addInput(sensor2, 1.0);
        hidden1.addInput(input1, .2);
        hidden2.addInput(input2, .5);
        output.addInput(hidden1, .5);
        output.addInput(hidden2, .5);
        sensor1.setOutput(Neuron.ACTIVE);
        sensor2.setOutput(Neuron.ACTIVE);
        assertEquals(Neuron.INACTIVE, output.calculateOutput(), 0.0);
        
        //genome.setRawData(new double[] { 1.0, 1.0, 1.0, 1.0, 1.0 } );
        //network = instance.updateNetworkWeights(network, genome);
        genome.setRawData(new double[] { 1.0, 1.0, 1.0, 1.0, 1.0, 1.0 } );
        List<Double> weights = output.getWeights();
        assertEquals(0.5, weights.get(0), 0.0);
        network = instance.updateNetworkWeights(network, genome);
        weights = output.getWeights();
        assertEquals(1.0, weights.get(0), 0.0);
        network = instance.recalculateNetworkOutput(network);
        assertEquals(Neuron.ACTIVE, output.getCachedOutput(), 0.0);
    }
    
    //@Test
    public void testRecalculateOutput() {
        logger.info("\ntesting recalculateOutputs()");
        Neuron[][] network = null;
        
        /**assertEquals(4, instance.recalculateNetworkOutput(network).length);
        
        Neuron sensor1 = new Neuron();
        Neuron sensor2 = new Neuron();
        Neuron input1 = new Neuron();
        Neuron input2 = new Neuron();
        Neuron hidden1 = new Neuron();
        Neuron hidden2 = new Neuron();
        Neuron output = new Neuron();
        
        network = new Neuron[4][];
        network = instance.recalculateNetworkOutput(network);
        assertEquals(4, network.length);
        assertEquals(Neuron.INACTIVE, output.getCachedOutput(), 0.0);
        
        network[0] = new Neuron[] { sensor1, sensor2 };
        network[1] = new Neuron[] { input1, input2 };
        network[2] = new Neuron[] { hidden1, hidden2 };
        network[3] = new Neuron[] { output };
        
        input1.addInput(sensor1, 1.0);
        input2.addInput(sensor2, 1.0);
        hidden1.addInput(input1, .5);
        hidden1.addInput(input2, .4);
        hidden2.addInput(input2, .5);
        hidden2.addInput(input1, .4);
        output.addInput(hidden1, .5);
        output.addInput(hidden2, .5);
        sensor1.setOutput(Neuron.ACTIVE);
        sensor2.setOutput(Neuron.ACTIVE);
        assertEquals(Neuron.INACTIVE, output.calculateOutput(), 0.0);
        network = instance.recalculateNetworkOutput(network);
        assertEquals(Neuron.ACTIVE, output.getCachedOutput(), 0.0);
        
        sensor1.setOutput(Neuron.INACTIVE);
        sensor2.setOutput(Neuron.INACTIVE);
        assertEquals(Neuron.ACTIVE, output.getCachedOutput(), 0.0);
        network = instance.recalculateNetworkOutput(network);
        assertEquals(Neuron.INACTIVE, output.getCachedOutput(), 0.0);
        /**/
        
        
        Variable[] variables = new Variable[] { Variable.CLASS, Variable.SEX, Variable.EMBARKED, Variable.ISCHILD };
        int hiddenLayerSize = 5;
        network = instance.getThreeLayerNetwork3(hiddenLayerSize, variables);
        System.out.println(network[0].length + " " + ListArrayUtil.arrayToString(network[0]));
        System.out.println(network[1].length + " " + ListArrayUtil.arrayToString(network[1]));
        System.out.println(network[2].length + " " + ListArrayUtil.arrayToString(network[2]));
        System.out.println(network[3].length + " " + ListArrayUtil.arrayToString(network[3]));
        int requiredSize = (network[1].length + 1) * network[2].length;
        DoubleGenome genome = new DoubleGenome(requiredSize);//variables.length * hiddenLayerSize + hiddenLayerSize);
        for(int i = 0; i < genome.getSize(); i++) {
            genome.set(i, 1.0);
        }
        List<Double> weights = network[3][0].getWeights();
        assertEquals(0.0, weights.get(0), 0.0);
        network = instance.updateNetworkWeights(network, genome);
        weights = network[3][0].getWeights();
        assertEquals(1.0, weights.get(0), 0.0);
        double[] inputActivation = new double[] { 0.0, 0.0, 1.0, 0.0, 1.0, 0.0, 0.0, 1.0, 0.0, 0.0, 0.0, 0.0, 1.0, 0.0 };
        for(int j = 0; j < inputActivation.length; j++) {
            network[0][j].setOutput(inputActivation[j]);
        }
        network = instance.recalculateNetworkOutput(network);
        assertEquals(Neuron.ACTIVE, network[3][0].getCachedOutput(), 0.0);
    }
    
    //@Test
    public void testResetNetwork() {
        logger.info("\ntesting resetNetwork()");
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
    
    
    //@Test
    public void testCalculateRawOutput() {
        logger.info("\ntesting calculateRawOutput()");
        Neuron instance = new Neuron();
        Neuron input1 = new Neuron(new ArrayList<Neuron>(), new ArrayList<Double>(), .2);
        Neuron input2 = new Neuron(new ArrayList<Neuron>(), new ArrayList<Double>(), .2);
        Neuron input3 = new Neuron(new ArrayList<Neuron>(), new ArrayList<Double>(), .1);
        
        instance.addInput(input1, Neuron.ACTIVE);
        assertEquals(.2, instance.calculateRawOutput(), ERROR);
        
        instance.addInput(input2, Neuron.INACTIVE);
        assertEquals(.2, instance.calculateRawOutput(), ERROR);
        
        instance.addInput(input3, Neuron.ACTIVE);
        assertEquals(.3, instance.calculateRawOutput(), ERROR);
        
        
        
        //testing problem discovered in TitanicNeural
        
        input1 = new Neuron(new ArrayList<Neuron>(), new ArrayList<Double>(), 0);
        input2 = new Neuron(new ArrayList<Neuron>(), new ArrayList<Double>(), 0);
        input3 = new Neuron(new ArrayList<Neuron>(), new ArrayList<Double>(), 1.0);
        Neuron input4 = new Neuron(new ArrayList<Neuron>(), new ArrayList<Double>(), 0);
        Neuron input5 = new Neuron(new ArrayList<Neuron>(), new ArrayList<Double>(), 1.0);
        Neuron input6 = new Neuron(new ArrayList<Neuron>(), new ArrayList<Double>(), 0);
        Neuron input7 = new Neuron(new ArrayList<Neuron>(), new ArrayList<Double>(), 0);
        Neuron input8 = new Neuron(new ArrayList<Neuron>(), new ArrayList<Double>(), 1);
        Neuron input9 = new Neuron(new ArrayList<Neuron>(), new ArrayList<Double>(), 0);
        Neuron input10 = new Neuron(new ArrayList<Neuron>(), new ArrayList<Double>(), 0);
        Neuron input11 = new Neuron(new ArrayList<Neuron>(), new ArrayList<Double>(), 0);
        Neuron input12 = new Neuron(new ArrayList<Neuron>(), new ArrayList<Double>(), 0);
        Neuron input13 = new Neuron(new ArrayList<Neuron>(), new ArrayList<Double>(), 1);
        Neuron input14 = new Neuron(new ArrayList<Neuron>(), new ArrayList<Double>(), 0);
        
        Neuron hidden1 = new Neuron();
        hidden1.addInput(input1, 1.0);
        hidden1.addInput(input2, 1.0);
        hidden1.addInput(input3, 1.0);
        hidden1.addInput(input4, 1.0);
        hidden1.addInput(input5, 1.0);
        hidden1.addInput(input6, 1.0);
        hidden1.addInput(input7, 1.0);
        hidden1.addInput(input8, 1.0);
        hidden1.addInput(input9, 1.0);
        hidden1.addInput(input10, 1.0);
        hidden1.addInput(input11, 1.0);
        hidden1.addInput(input12, 1.0);
        hidden1.addInput(input13, 1.0);
        hidden1.addInput(input14, 1.0);
        
        assertEquals(4.0, hidden1.calculateRawOutput(), 0.0);
        assertEquals(Neuron.ACTIVE, hidden1.calculateOutput(), 0.0);
        
        Neuron hidden2 = new Neuron();
        Neuron hidden3 = new Neuron();
        Neuron hidden4 = new Neuron();
        Neuron hidden5 = new Neuron();
        
        Neuron output = new Neuron();
        assertEquals(Neuron.INACTIVE, output.calculateOutput(), 0.0);

        output.addInput(hidden1, 1.0);
        output.addInput(hidden2, 1.0);
        output.addInput(hidden3, 1.0);
        output.addInput(hidden4, 1.0);
        output.addInput(hidden5, 1.0);
        
        Neuron[][] network = new Neuron[4][];
        //0.0, 0.0, 1.0, 0.0, 1.0, 0.0, 0.0, 1.0, 0.0, 0.0, 0.0, 0.0, 1.0, 0.0
        network[0] = new Neuron[] { new Neuron(0.0), new Neuron(0.0), new Neuron(1.0), new Neuron(0.0), new Neuron(1.0), new Neuron(0.0), new Neuron(0.0), new Neuron(1.0), new Neuron(0.0), new Neuron(0.0), new Neuron(0.0), new Neuron(0.0), new Neuron(1.0), new Neuron(0.0) };
        network[1] = new Neuron[] { input1, input2, input3, input4, input5, input6, input7, input8, input9, input10, input11, input12, input13, input14 };
        
        network = this.getThreeLayerNetwork3(5);
        for(int i = 0; i < 14; i++) {
            network[1][i].addInput(network[0][i], 1.0);
            network[1][i].setOutput(Neuron.INACTIVE);
        }
        network[2] = new Neuron[] { hidden1, hidden2, hidden3, hidden4, hidden5 };
        network[3] = new Neuron[] { output };
        for(int i = 0; i < 5; i++) {
            network[2][i].setOutput(Neuron.INACTIVE);
        }
        assertEquals(Neuron.INACTIVE, output.calculateOutput(), 0.0);
        network = this.instance.recalculateNetworkOutput(network);
        assertEquals(Neuron.ACTIVE, output.calculateOutput(), 0.0);
    }
    
    public Neuron[][] getThreeLayerNetwork3(int hiddenLayerSize) {
        
        Neuron[][] network = new Neuron[4][];
        Neuron[][] inputs = this.getSensorsAndFirstLayer();
        network[0] = inputs[0];
        network[1] = inputs[1];
        network[2] = new Neuron[hiddenLayerSize];
        network[3] = new Neuron[1];
        
        int numConnections = network[1].length * hiddenLayerSize + hiddenLayerSize;
        
        for(int j = 0; j < hiddenLayerSize; j++) {
            network[2][j] = new Neuron();
        }
        for(int i = 0; i < inputs[1].length; i++) {
            for(int j = 0; j < hiddenLayerSize; j++) {
                //weights.get(i * (hiddenLayerSize) + j);
                network[2][j].addInput(network[1][i], 1.0);
            }
        }
        
        Neuron output = new Neuron();
        int start = inputs[1].length * hiddenLayerSize;
        for(int j = 0; j < hiddenLayerSize; j++) {
            output.addInput(network[2][j], 1.0);
        }
        network[3][0] = output;
        return network;
    }
    
    public Neuron[][] getSensorsAndFirstLayer() {
        Neuron[][] network = new Neuron[2][];
        Neuron input1 = new Neuron(new ArrayList<Neuron>(), new ArrayList<Double>(), 0);
        Neuron input2 = new Neuron(new ArrayList<Neuron>(), new ArrayList<Double>(), 0);
        Neuron input3 = new Neuron(new ArrayList<Neuron>(), new ArrayList<Double>(), 1.0);
        Neuron input4 = new Neuron(new ArrayList<Neuron>(), new ArrayList<Double>(), 0);
        Neuron input5 = new Neuron(new ArrayList<Neuron>(), new ArrayList<Double>(), 1.0);
        Neuron input6 = new Neuron(new ArrayList<Neuron>(), new ArrayList<Double>(), 0);
        Neuron input7 = new Neuron(new ArrayList<Neuron>(), new ArrayList<Double>(), 0);
        Neuron input8 = new Neuron(new ArrayList<Neuron>(), new ArrayList<Double>(), 1);
        Neuron input9 = new Neuron(new ArrayList<Neuron>(), new ArrayList<Double>(), 0);
        Neuron input10 = new Neuron(new ArrayList<Neuron>(), new ArrayList<Double>(), 0);
        Neuron input11 = new Neuron(new ArrayList<Neuron>(), new ArrayList<Double>(), 0);
        Neuron input12 = new Neuron(new ArrayList<Neuron>(), new ArrayList<Double>(), 0);
        Neuron input13 = new Neuron(new ArrayList<Neuron>(), new ArrayList<Double>(), 1);
        Neuron input14 = new Neuron(new ArrayList<Neuron>(), new ArrayList<Double>(), 0);
        
        network[0] = new Neuron[] { new Neuron(0.0), new Neuron(0.0), new Neuron(1.0), new Neuron(0.0), new Neuron(1.0), new Neuron(0.0), new Neuron(0.0), new Neuron(1.0), new Neuron(0.0), new Neuron(0.0), new Neuron(0.0), new Neuron(0.0), new Neuron(1.0), new Neuron(0.0) };
        network[1] = new Neuron[] { input1, input2, input3, input4, input5, input6, input7, input8, input9, input10, input11, input12, input13, input14 };
        
        return network;
    }
    
    public Neuron[][] recalculateNetworkOutput(Neuron[][] network) {
        if(network == null) {
            return new Neuron[4][];
        }
        for(int i = 1; i < network.length; i++) {
            if(network[i] == null) {
                continue;
            }
            for(int j = 0; j < network[i].length; j++) {
                //System.out.println("before network[" + i + "][" + j + "].calculateOutput():  " + network[i][j].getCachedOutput());System.out.flush();
                //network[i][j].setOutput(network[i][j].calculateOutput());       //the problem seems to be with this line
                System.out.println(ListArrayUtil.listToString(network[i][j].getWeights()));
                network[i][j].calculateOutput();
                System.out.println("after network[" + i + "][" + j + "].calculateOutput():  " + network[i][j].getCachedOutput());System.out.flush();
            }
        }
        return network;
    }
}