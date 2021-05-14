package GeneticAlgorithm.PSO;

import java.util.*;

import GeneticAlgorithm.GA.Chromosome;

public class Particle extends Chromosome {
	private  List<Double> velocity;
	private  List<Double> position;
	
	
	public void updateVelocity (Particle par) {
		
	}
	
	public void updatePosition (Particle par) {
		
	}
	
	
	public List<Double> getPosition() {
		return position;
	}

	public void setPosition(List<Double> position) {
		this.position = position;
	}

	public  List<Double> getVelocity() {
		return velocity;
	}

	public void setVelocity(List<Double> velocity) {
		this.velocity = velocity;
	}

	
}
 