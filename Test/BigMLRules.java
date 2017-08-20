package Test;

public class BigMLRules {
	
	private int ruleSize;
	private String ruleID;
	private String ant;
	private String con;
	
	private float antCovP;
	private int antCov;

	private float supP;
	private int sup;
	
	private float confidence;
	private float leverage;
	private float lift;
	private double pVal;
	private float conCovP;
	private int conCov;
	public int getRuleSize() {
		return ruleSize;
	}
	public void setRuleSize(int ruleSize) {
		this.ruleSize = ruleSize;
	}
	public String getRuleID() {
		return ruleID;
	}
	public void setRuleID(String ruleID) {
		this.ruleID = ruleID;
	}
	public String getAnt() {
		return ant;
	}
	public void setAnt(String ant) {
		this.ant = ant;
	}
	public String getCon() {
		return con;
	}
	public void setCon(String con) {
		this.con = con;
	}
	public float getAntCovP() {
		return antCovP;
	}
	public void setAntCovP(float antCovP) {
		this.antCovP = antCovP;
	}
	public int getAntCov() {
		return antCov;
	}
	public void setAntCov(int antCov) {
		this.antCov = antCov;
	}
	public float getSupP() {
		return supP;
	}
	public void setSupP(float supP) {
		this.supP = supP;
	}
	public int getSup() {
		return sup;
	}
	public void setSup(int sup) {
		this.sup = sup;
	}
	public float getConfidence() {
		return confidence;
	}
	public void setConfidence(float confidence) {
		this.confidence = confidence;
	}
	public float getLeverage() {
		return leverage;
	}
	public void setLeverage(float leverage) {
		this.leverage = leverage;
	}
	public float getLift() {
		return lift;
	}
	public void setLift(float lift) {
		this.lift = lift;
	}
	public double getpVal() {
		return pVal;
	}
	public void setpVal(double pVal) {
		this.pVal = pVal;
	}
	public float getConCovP() {
		return conCovP;
	}
	public void setConCovP(float conCovP) {
		this.conCovP = conCovP;
	}
	public int getConCov() {
		return conCov;
	}
	public void setConCov(int conCov) {
		this.conCov = conCov;
	}
	
	
}
