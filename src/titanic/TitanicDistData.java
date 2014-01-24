/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package titanic;

import learning.stats.ProbDist;
import learning.naivebayes.*;
import java.util.List;
import java.util.ArrayList;

/**
 *
 * @author paul
 */
public class TitanicDistData {
    
    private Classification survived;
    private Classification notSurvived;
    private ProbDist<Classification> classDist;
    private static final String NA = "NA";
    
    public TitanicDistData() {
        setUpData();
    }
    
    public ProbDist<Classification> getClassificationDist() {
        return this.classDist;
    }
    
    public void setUpData() {
        this.survived = new Classification("1");
        survived.setFeatureDists(getSurvivedDists());
        this.notSurvived = new Classification("0");
        notSurvived.setFeatureDists(getNotSurvivedDists());
        this.classDist = new ProbDist<Classification>();
        classDist.add(survived, 0.3838);
        classDist.add(notSurvived, 0.6162);
    }
    
    //order should be class, sex, child
    public List<ProbDist> getSurvivedDists() {
        List<ProbDist> list = new ArrayList<ProbDist>();
        list.add(getSurvivedClass());
        list.add(getSurvivedSex());
        list.add(getSurvivedIsChild());
        //list.add(getSurvivedClass());
        return list;
    }
    
    public List<ProbDist> getNotSurvivedDists() {
        List<ProbDist> list = new ArrayList<ProbDist>();
        list.add(getNotSurvivedClass());
        list.add(getNotSurvivedSex());
        list.add(getNotSurvivedIsChild());
        //list.add(getNotSurvivedClass());
        return list;
    }
    
    public ProbDist<String> getSurvivedSex() {
        ProbDist<String> dist = new ProbDist<String>();
        dist.add("female", 0.6812865);
        dist.add("male", 0.3187135);
        return dist;
    }
    
    public ProbDist<String> getNotSurvivedSex() {
        ProbDist<String> dist = new ProbDist<String>();
        dist.add("female", 0.1475410);
        dist.add("male", 0.8524590);
        return dist;
    }
    
    public ProbDist<String> getSurvivedIsChild() {
        ProbDist<String> dist = new ProbDist<String>();
        dist.add("FALSE", 0.68713450);
        dist.add("TRUE", 0.16081871);
        dist.add(NA, 0.15204678);
        return dist;
    }
    
    public ProbDist<String> getNotSurvivedIsChild() {
        ProbDist<String> dist = new ProbDist<String>();
        dist.add("FALSE", 0.69034608);
        dist.add("TRUE", 0.08196721);
        dist.add(NA, 0.22768670);
        return dist;
    }
    
    public ProbDist<String> getSurvivedClass() {
        ProbDist<String> dist = new ProbDist<String>();
        dist.add("1", 0.3976608);
        dist.add("2", 0.2543860);
        dist.add("3", 0.3479532);
        return dist;
    }
    
    public ProbDist<String> getNotSurvivedClass() {
        ProbDist<String> dist = new ProbDist<String>();
        dist.add("1", 0.1457195);
        dist.add("2", 0.1766849);
        dist.add("3", 0.6775956);
        return dist;
    }
}
