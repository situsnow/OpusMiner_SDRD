package Test;

public class AprioriRules {

	private int index;
	private String antecedent;
	private int ant_cov;
	private String consequent;
	private int con_cov;
	private float confidence;
	public int getIndex() {
		return index;
	}
	public void setIndex(int index) {
		this.index = index;
	}
	public String getAntecedent() {
		return antecedent;
	}
	public void setAntecedent(String antecedent) {
		this.antecedent = antecedent;
	}
	public int getAnt_cov() {
		return ant_cov;
	}
	public void setAnt_cov(int ant_cov) {
		this.ant_cov = ant_cov;
	}
	public String getConsequent() {
		return consequent;
	}
	public void setConsequent(String consequent) {
		this.consequent = consequent;
	}
	public int getCon_cov() {
		return con_cov;
	}
	public void setCon_cov(int con_cov) {
		this.con_cov = con_cov;
	}
	public float getConfidence() {
		return confidence;
	}
	public void setConfidence(float confidence) {
		this.confidence = confidence;
	}
	
	
}
