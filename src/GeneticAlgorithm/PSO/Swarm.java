package GeneticAlgorithm.PSO;

import java.util.ArrayList;
import java.util.List;

public class Swarm {
	List<Particle> listOfParticles;

	public List<Particle> getListOfParticles() {
		return listOfParticles;
	}

	public void setListOfParticles(List<Particle> listOfParticles) {
		this.listOfParticles = listOfParticles;
	}

	public Swarm() {
		listOfParticles = new ArrayList<Particle>();
	}
}
