/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package titanic;

import learning.genetic.DoubleGenome;
import java.io.IOException;
import toolbox.io.CSVReader;
import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;

import org.apache.log4j.*;
import toolbox.util.*;
import toolbox.stats.Histogram;
import learning.neural.Neuron;

/**
 *
 * @author paul
 */
public class TitanicNeural {
    
    private Logger logger;
    private static final int MAX_LINES = 1000;
    private static final int TRAINING_FILE_LENGTH = 891;
    public static final String TITANIC_FILE_NAME = "titanic.csv";
    public static final int SURVIVED_INDEX = 1;
    public static final String COLUMN_SEPARATOR = ",";
    public static final int UNKNOWN_SURVIVED = 2;
    
    public static void main(String[] args) {
        TitanicNeural tn = new TitanicNeural();
        try {
            //tn.doTwoLayerAnalysis("titanic.csv");
            tn.doThreeLayerAnalysis("titanic.csv");
        } catch(IOException e) {
            System.out.println(e.getClass() + " " + e.getMessage());
        }
    }
    
    public TitanicNeural() {
        logger = ListArrayUtil.getLogger(this.getClass(), Level.INFO);
    }
    
    public void doTwoLayerAnalysis(String filename) throws IOException {
        //List<List<String>> columns = CSVReader.getColumns(filename, new int[] { 0, 1, 2, 5, 7, 8, 12, 13 }, ",");
        List<List<String>> rows = CSVReader.getRowsAsLists(filename, new int[] { 1, 2, 5, 6, 7, 8, 10, 12, 13 }, 900);
        DoubleGenome bestGenome = new DoubleGenome(14);
        bestGenome.generateRandom();
        logger.debug(bestGenome.getRawData());
        //DoubleGenome previous = bestGenome.clone();
        
        Neuron[][] network = this.getTwoLayerNetwork(bestGenome);
        
        int numRuns = 1000;
        int currentNumCorrect = MathUtil.sum(doOneRun(rows, network));
        int bestNumCorrect = currentNumCorrect;
        logger.info("numCorrect = " + currentNumCorrect);
        DoubleGenome copy = bestGenome.clone();
        for(int i = 0; i < numRuns; i++) {
            if(i % 1000 == 0) {
                logger.info(i);
            }
            List<Double> mutated = bestGenome.mutate();
            logger.debug("genome is " + mutated);
            copy.setRawData(mutated);
            network = this.getTwoLayerNetwork(copy);
            currentNumCorrect = MathUtil.sum(doOneRun(rows, network));
            logger.info("numCorrect = " + currentNumCorrect);
            if(currentNumCorrect > bestNumCorrect) {
                bestNumCorrect = currentNumCorrect;
                logger.debug("use mutated genome");
                bestGenome.setRawData(mutated);
                copy = bestGenome.clone();
            }
        }
        logger.info("best was " +  bestNumCorrect + " (" + (double)(bestNumCorrect * 100.0) / (double) rows.size() + "%) correct with " + ListArrayUtil.listToString(bestGenome.getRawData()));
    }
    
    public void doThreeLayerAnalysis(String filename) throws IOException {
        Variable[] variables = new Variable[] { Variable.SURVIVED, Variable.CLASS, Variable.SEX, Variable.AGE, Variable.SIBSP, Variable.PARCH, Variable.FARE, Variable.EMBARKED, Variable.ISCHILD };
        List<List<String>> rows = this.getRowsAsLists(filename, variables );
        int[] survived = this.getSurvived();    //list of who survived, to compare against the results of each run
        logger.debug(rows.size() + " rows");
        int hiddenLayerSize = 20;
        DoubleGenome bestGenome = new DoubleGenome(300);
        bestGenome.generateRandom();
        Neuron[][] network = this.getThreeLayerNetwork2(bestGenome, hiddenLayerSize);
        
        int numRuns = 3000;
        int[] record = this.doOneRun(rows, this.getThreeLayerNetwork(bestGenome));
        int currentNumCorrect = survived.length - ListArrayUtil.findNumDiffs(record, survived);
        int bestNumCorrect = currentNumCorrect;
        logger.debug("numCorrect = " + currentNumCorrect);
        DoubleGenome copy = bestGenome.clone();
        for(int i = 0; i < numRuns; i++) {
            if(i % 1000 == 0) {
                logger.info(i);
                logger.info("best num correct = " + bestNumCorrect);
                logger.info("current num correct = " + currentNumCorrect);
            }
            List<Double> mutated = bestGenome.mutate();
            copy.setRawData(mutated);
            network = this.getThreeLayerNetwork(copy);
            record = this.doOneRun(rows, network);
            currentNumCorrect = survived.length - ListArrayUtil.findNumDiffs(record, survived);
            if(currentNumCorrect > bestNumCorrect) {
                bestNumCorrect = currentNumCorrect;
                bestGenome.setRawData(mutated);
                copy = bestGenome.clone();
            }
        }
        record = this.doOneRun(rows, this.getThreeLayerNetwork(bestGenome));
        Histogram resultHist = new Histogram(record);
        logger.info("best was " +  bestNumCorrect + " (" + (double)(bestNumCorrect * 100.0) / rows.size() + "%, " + (double)bestNumCorrect * 100.0/(double)(resultHist.getCountOf(0) + resultHist.getCountOf(1)) + "% of processable) correct with " + ListArrayUtil.listToString(bestGenome.getRawData()));
        //showRecord(rows, record);
    }
    
    protected List<List<String>> getRowsAsLists(String filename, Variable[] variables) throws IOException {
        if(filename == null || variables == null || filename.equals("") || variables.length == 0) {
            return new ArrayList<List<String>>();
        }
        //convert variables to an int[]
        int[] ints = new int[variables.length];
        for(int i = 0; i < variables.length; i++) {
            ints[i] = variables[i].intValue();
        }
        return CSVReader.getRowsAsLists(filename, ints, MAX_LINES);
    }
    
    protected int[] getSurvived() throws IOException {
        List<String> row = CSVReader.getSingleColumn(TITANIC_FILE_NAME, SURVIVED_INDEX, COLUMN_SEPARATOR).getData();
        int[] survived = new int[row.size()];
        for(int i = 0; i < survived.length; i++) {
            try {
                survived[i] = Integer.parseInt(row.get(i));
            } catch(NumberFormatException e) {
                if("0".equals(row.get(i))) {
                    survived[i] = 0;
                } else if("1".equals(row.get(i))) {
                    survived[i] = 1;
                } else {
                    survived[i] = UNKNOWN_SURVIVED;
                }
            }
        }
        return survived;
    }
    
    protected int[] doOneRun(List<List<String>> rows, Neuron[][] network) {
        int survived;
        int pclass;
        String sex;
        int age;
        int sibsp;
        int parch;
        double fare;
        String port;
        boolean isChild;
        
        String sep = "\t";
        int numCorrect = 0;
        int[] record = new int[rows.size()];
        //for(List<String> row : rows) {
        for(int i = 0; i < rows.size(); i++) {
            //first, reset neurons to inactive
            network = this.resetNetwork(network);
            
            //now, go through the rows
            List<String> row = rows.get(i);
            try {
                survived = Integer.parseInt(row.get(0));
                pclass = Integer.parseInt(row.get(1));
                sex = row.get(2);
                age = Integer.parseInt(row.get(3));
                sibsp = Integer.parseInt(row.get(4));
                parch = Integer.parseInt(row.get(5));
                fare = Double.parseDouble(row.get(6));
                port = row.get(7);
                isChild = Boolean.parseBoolean(row.get(8));
                logger.debug(pclass + sep + sep + sep + sex + sep + sep + age + sep + sibsp + sep + parch + sep + fare + sep + port + sep + sep + sep + isChild + sep + sep + " -> " + survived);
                
                switch(pclass) {
                    case 1:
                        network[0][0].setOutput(Neuron.ACTIVE);
                        break;
                    case 2:
                        network[0][1].setOutput(Neuron.ACTIVE);
                        break;
                    case 3:
                        network[0][2].setOutput(Neuron.ACTIVE);
                        break;
                }
                if("male".equals(sex)) {
                    network[0][3].setOutput(Neuron.ACTIVE);
                } else if("female".equals(sex)) {
                    network[0][4].setOutput(Neuron.ACTIVE);
                }
                network[0][5].setOutput((double)age / 100.0);
                network[0][6].setOutput((double)sibsp / 10.0);
                network[0][7].setOutput((double)parch / 10.0);
                network[0][8].setOutput(fare / 200.0);
                if("S".equals(port)) {
                    network[0][9].setOutput(Neuron.ACTIVE);
                } else if("Q".equals(port)) {
                    network[0][10].setOutput(Neuron.ACTIVE);
                } else if("C".equals(port)) {
                    network[0][11].setOutput(Neuron.ACTIVE);
                }
                if(isChild) {
                    network[0][12].setOutput(Neuron.ACTIVE);
                } else {
                    network[0][13].setOutput(Neuron.ACTIVE);
                }
                
                StringBuilder sb = new StringBuilder("");
                for(int j = 1; j < network.length; j++) {
                    sb = new StringBuilder("");
                    for(int k = 0; k < network[j].length; k++) {
                        network[j][k].calculateOutput();
                        sb.append(network[j][k].getCachedOutput()).append("\t");
                    }
                    logger.debug("output for network[" + j + "][] is " + sb);
                }
                sb = new StringBuilder("");
                //if((int)(network[2][0].getCachedOutput()) == survived) {
                if((int)(network[network.length - 1][0].getCachedOutput()) == survived) {
                //if((int)(network[3][0].getCachedOutput()) == survived) {
                    logger.debug("was correct");
                    numCorrect++;
                    //record[i] = 1;
                }
                record[i] = (int)(network[network.length - 1][0].getCachedOutput());
            } catch(NumberFormatException e) {
                //for now, just go on the the next row I guess
                //TODO:  handle
                record[i] = 2;
            }
        }
        return record;
    }
    
    //TODO: check length of weights
    public Neuron[][] getTwoLayerNetwork(DoubleGenome weights) {
        Neuron[][] network = new Neuron[3][];
        Neuron[][] inputs = this.getSensorsAndFirstLayer();
        network[0] = inputs[0];
        network[1] = inputs[1];
        Neuron output = new Neuron();
        
        //DoubleGenome weights = new DoubleGenome(14);
        
        output.addInput(inputs[1][0], weights.get(0));
        output.addInput(inputs[1][1], weights.get(1));
        output.addInput(inputs[1][2], weights.get(2));
        output.addInput(inputs[1][3], weights.get(3));
        output.addInput(inputs[1][4], weights.get(4));
        output.addInput(inputs[1][5], weights.get(5));
        output.addInput(inputs[1][6], weights.get(6));
        output.addInput(inputs[1][7], weights.get(7));
        output.addInput(inputs[1][8], weights.get(8));
        output.addInput(inputs[1][9], weights.get(9));
        output.addInput(inputs[1][10], weights.get(10));
        output.addInput(inputs[1][11], weights.get(11));
        output.addInput(inputs[1][12], weights.get(12));
        output.addInput(inputs[1][13], weights.get(13));
        
        network[2] = new Neuron[] { output };
        return network;
    }
    
    public Neuron[][] getThreeLayerNetwork(DoubleGenome weights) {
        return getThreeLayerNetwork1(weights);
    }
    
    //This one seems to work worse than the two layer network and is worse than guessing all deaths and sometimes worse than random guessing.
    public Neuron[][] getThreeLayerNetwork1(DoubleGenome weights) {
        Neuron[][] network = new Neuron[4][];
        Neuron[][] inputs = this.getSensorsAndFirstLayer();
        network[0] = inputs[0];
        network[1] = inputs[1];
        
        Neuron hidden0 = new Neuron();
        Neuron hidden1 = new Neuron();
        Neuron hidden2 = new Neuron();
        Neuron hidden3 = new Neuron();
        Neuron hidden4 = new Neuron();
        Neuron hidden5 = new Neuron();
        Neuron hidden6 = new Neuron();
        Neuron hidden7 = new Neuron();
        Neuron hidden8 = new Neuron();
        Neuron hidden9 = new Neuron();
        Neuron hidden10 = new Neuron();
        Neuron hidden11 = new Neuron();
        Neuron hidden12 = new Neuron();
        Neuron hidden13 = new Neuron();
        Neuron hidden14 = new Neuron();
        Neuron hidden15 = new Neuron();
        Neuron hidden16 = new Neuron();
        Neuron hidden17 = new Neuron();
        Neuron hidden18 = new Neuron();
        Neuron hidden19 = new Neuron();
        Neuron hidden20 = new Neuron();
        Neuron hidden21 = new Neuron();
        Neuron hidden22 = new Neuron();
        Neuron hidden23 = new Neuron();
        Neuron hidden24 = new Neuron();
        Neuron hidden25 = new Neuron();
        Neuron hidden26 = new Neuron();
        Neuron hidden27 = new Neuron();
        
        hidden0.addInput(network[1][0], weights.get(0));
        hidden0.addInput(network[1][7], weights.get(1));
        hidden1.addInput(network[1][1], weights.get(2));
        hidden1.addInput(network[1][8], weights.get(3));
        hidden2.addInput(network[1][2], weights.get(4));
        hidden2.addInput(network[1][9], weights.get(5));
        hidden3.addInput(network[1][3], weights.get(6));
        hidden3.addInput(network[1][10], weights.get(7));
        hidden4.addInput(network[1][4], weights.get(8));
        hidden4.addInput(network[1][11], weights.get(9));
        hidden5.addInput(network[1][5], weights.get(10));
        hidden5.addInput(network[1][12], weights.get(11));
        hidden6.addInput(network[1][6], weights.get(12));
        hidden6.addInput(network[1][13], weights.get(13));
        hidden7.addInput(network[1][0], weights.get(14));
        hidden7.addInput(network[1][7], weights.get(15));
        hidden8.addInput(network[1][1], weights.get(16));
        hidden8.addInput(network[1][8], weights.get(17));
        hidden9.addInput(network[1][2], weights.get(18));
        hidden9.addInput(network[1][9], weights.get(19));
        hidden10.addInput(network[1][3], weights.get(20));
        hidden10.addInput(network[1][10], weights.get(21));
        hidden11.addInput(network[1][4], weights.get(22));
        hidden11.addInput(network[1][11], weights.get(23));
        hidden12.addInput(network[1][5], weights.get(24));
        hidden12.addInput(network[1][12], weights.get(25));
        hidden13.addInput(network[1][6], weights.get(26));
        hidden13.addInput(network[1][13], weights.get(27));
        
        network[2] = new Neuron[28];
        network[2][0] = hidden0;
        network[2][1] = hidden1;
        network[2][2] = hidden2;
        network[2][3] = hidden3;
        network[2][4] = hidden4;
        network[2][5] = hidden5;
        network[2][6] = hidden6;
        network[2][7] = hidden7;
        network[2][8] = hidden8;
        network[2][9] = hidden9;
        network[2][10] = hidden10;
        network[2][11] = hidden11;
        network[2][12] = hidden12;
        network[2][13] = hidden13;
        network[2][14] = hidden14;
        network[2][15] = hidden15;
        network[2][16] = hidden16;
        network[2][17] = hidden17;
        network[2][18] = hidden18;
        network[2][19] = hidden19;
        network[2][20] = hidden20;
        network[2][21] = hidden21;
        network[2][22] = hidden22;
        network[2][23] = hidden23;
        network[2][24] = hidden24;
        network[2][25] = hidden25;
        network[2][26] = hidden26;
        network[2][27] = hidden27;
        
        Neuron output = new Neuron();
        output.addInput(hidden0, weights.get(28));
        output.addInput(hidden1, weights.get(29));
        output.addInput(hidden2, weights.get(30));
        output.addInput(hidden3, weights.get(31));
        output.addInput(hidden4, weights.get(32));
        output.addInput(hidden5, weights.get(33));
        output.addInput(hidden6, weights.get(34));
        output.addInput(hidden7, weights.get(35));
        output.addInput(hidden8, weights.get(36));
        output.addInput(hidden9, weights.get(37));
        output.addInput(hidden10, weights.get(38));
        output.addInput(hidden11, weights.get(39));
        output.addInput(hidden12, weights.get(40));
        output.addInput(hidden13, weights.get(41));
        /*output.addInput(hidden14, weights.get(42));
        output.addInput(hidden15, weights.get(43));
        output.addInput(hidden16, weights.get(44));
        output.addInput(hidden17, weights.get(45));
        output.addInput(hidden18, weights.get(46));
        output.addInput(hidden19, weights.get(47));
        output.addInput(hidden20, weights.get(48));
        output.addInput(hidden21, weights.get(49));
        output.addInput(hidden22, weights.get(50));
        output.addInput(hidden23, weights.get(51));
        output.addInput(hidden24, weights.get(52));
        output.addInput(hidden25, weights.get(53));
        output.addInput(hidden26, weights.get(54));
        output.addInput(hidden27, weights.get(55));*/
        
        network[3] = new Neuron[1];
        network[3][0] = output;
        return network;
    }
    //TODO:  how to handle weights that is two small?
    public Neuron[][] getThreeLayerNetwork2(DoubleGenome weights, int hiddenLayerSize) {
        Neuron[][] network = new Neuron[4][];
        Neuron[][] inputs = this.getSensorsAndFirstLayer();
        network[0] = inputs[0];
        network[1] = inputs[1];
        network[2] = new Neuron[hiddenLayerSize];
        network[3] = new Neuron[1];
        
        for(int j = 0; j < hiddenLayerSize; j++) {
            network[2][j] = new Neuron();
        }
        for(int i = 0; i < inputs[1].length; i++) {
            for(int j = 0; j < hiddenLayerSize; j++) {
                weights.get(i * (hiddenLayerSize) + j);
                network[2][j].addInput(network[1][i], weights.get(i * (hiddenLayerSize) + j));
            }
        }
        
        Neuron output = new Neuron();
        int start = inputs[1].length * hiddenLayerSize;
        for(int j = 0; j < hiddenLayerSize; j++) {
            output.addInput(network[2][j], weights.get(start + j));
        }
        network[3][0] = output;
        return network;
    }
  
    //TODO:  input a list of variables and have this determine how many need to be created for each variable
    public Neuron[][] getSensorsAndFirstLayer() {
        Neuron firstClassSensor = new Neuron();
        Neuron secondClassSensor = new Neuron();
        Neuron thirdClassSensor = new Neuron();
        Neuron maleSensor = new Neuron();
        Neuron femaleSensor = new Neuron();
        Neuron ageSensor = new Neuron();
        Neuron sibspSensor = new Neuron();
        Neuron parchSensor = new Neuron();
        Neuron fareSensor = new Neuron();
        Neuron southamptonSensor = new Neuron();
        Neuron queenstownSensor = new Neuron();
        Neuron cherbourgSensor = new Neuron();
        Neuron childSensor = new Neuron();
        Neuron adultSensor = new Neuron();
        
        Neuron firstClassNeuron = new Neuron();
        Neuron secondClassNeuron = new Neuron();
        Neuron thirdClassNeuron = new Neuron();
        Neuron maleNeuron = new Neuron();
        Neuron femaleNeuron = new Neuron();
        Neuron ageNeuron = new Neuron();
        Neuron sibspNeuron = new Neuron();
        Neuron parchNeuron = new Neuron();
        Neuron fareNeuron = new Neuron();
        Neuron southamptonNeuron = new Neuron();
        Neuron queenstownNeuron = new Neuron();
        Neuron cherbourgNeuron = new Neuron();
        Neuron childNeuron = new Neuron();
        Neuron adultNeuron = new Neuron();
        
        firstClassNeuron.addInput(firstClassSensor, 1.0);
        secondClassNeuron.addInput(secondClassSensor, 1.0);
        thirdClassNeuron.addInput(thirdClassSensor, 1.0);
        maleNeuron.addInput(maleSensor, 1.0);
        femaleNeuron.addInput(femaleSensor, 1.0);
        ageNeuron.addInput(ageSensor, 1.0);
        sibspNeuron.addInput(sibspSensor, 1.0);
        parchNeuron.addInput(parchSensor, 1.0);
        fareNeuron.addInput(fareSensor, 1.0);
        southamptonNeuron.addInput(southamptonSensor, 1.0);
        queenstownNeuron.addInput(queenstownSensor, 1.0);
        cherbourgNeuron.addInput(cherbourgSensor, 1.0);
        childNeuron.addInput(childSensor, 1.0);
        adultNeuron.addInput(adultSensor, 1.0);
        
        Neuron[] sensors = new Neuron[14];
        sensors[0] = firstClassSensor;
        sensors[1] = secondClassSensor;
        sensors[2] = thirdClassSensor;
        sensors[3] = maleSensor;
        sensors[4] = femaleSensor;
        sensors[5] = ageSensor;
        sensors[6] = sibspSensor;
        sensors[7] = parchSensor;
        sensors[8] = fareSensor;
        sensors[9] = southamptonSensor;
        sensors[10] = queenstownSensor;
        sensors[11] = cherbourgSensor;
        sensors[12] = childSensor;
        sensors[13] = adultSensor;
        
        Neuron[] firstLayer = new Neuron[14];
        firstLayer[0] = firstClassNeuron;
        firstLayer[1] = secondClassNeuron;
        firstLayer[2] = thirdClassNeuron;
        firstLayer[3] = maleNeuron;
        firstLayer[4] = femaleNeuron;
        firstLayer[5] = ageNeuron;
        firstLayer[6] = sibspNeuron;
        firstLayer[7] = parchNeuron;
        firstLayer[8] = fareNeuron;
        firstLayer[9] = southamptonNeuron;
        firstLayer[10] = queenstownNeuron;
        firstLayer[11] = cherbourgNeuron;
        firstLayer[12] = childNeuron;
        firstLayer[13] = adultNeuron;
        
        Neuron[][] network = new Neuron[2][13];
        network[0] = sensors;
        network[1] = firstLayer;
        return network;
    }
    
    public Neuron[][] resetNetwork(Neuron[][] network) {
        if(network == null) {
            return new Neuron[0][0];
        }
        for(int j = 0; j < network.length; j++) {
            for(int k = 0; k < network[j].length; k++) {
                network[j][k].setOutput(Neuron.INACTIVE);
            }
        }
        return network;
    }
    
    public void showRecord(List<List<String>> rows, int[] record) {
        logger.info("rows.size() == " + rows.size());
        logger.setLevel(Level.DEBUG);
        
        int passengerId;
        int survived;
        int pclass;
        String sex;
        int age;
        int sibsp;
        int parch;
        double fare;
        String port;
        boolean isChild;
        String sep = COLUMN_SEPARATOR;
        List<String> row = null;
        String correct;
        logger.debug("passengerid" + sep + "survived" + sep + "guess" + sep + "correct" + sep + "pclass" + sep + "sex" + sep + "age" + sep + "sibsp" + sep + "parch" + sep + "fare" + sep + "port" + sep + "isChild");
        for(int i = 0; i < rows.size(); i++) {
            row = rows.get(i);
            try {
                survived = Integer.parseInt(row.get(0));
                pclass = Integer.parseInt(row.get(1));
                sex = row.get(2);
                age = Integer.parseInt(row.get(3));
                sibsp = Integer.parseInt(row.get(4));
                parch = Integer.parseInt(row.get(5));
                fare = Double.parseDouble(row.get(6));
                port = row.get(7);
                isChild = Boolean.parseBoolean(row.get(8));
                correct = (record[i] == survived)?"true":"false";
                //logger.debug(record[i] + sep + survived + sep + pclass + sep + sep + sep + sex + sep + sep + age + sep + sibsp + sep + parch + sep + fare + sep + port + sep + sep + sep + isChild);
                logger.debug(i+1 + sep + survived + sep + record[i] + sep + correct + sep + pclass + sep + sex + sep + age + sep + sibsp + sep + parch + sep + fare + sep + port + sep + isChild);
            } catch(NumberFormatException e) {
                //for now, just go on the the next row I guess
                //TODO:  handle
                //logger.error(e.getClass() + " at " + i + ":  " + e.getMessage());
                logger.debug(i+1 + sep+ "unknown" + sep + record[i] + sep + "unknown" + sep + ListArrayUtil.listToString(row, COLUMN_SEPARATOR, "", ""));
            }
        }
    }
}
