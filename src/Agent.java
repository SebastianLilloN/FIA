public class Agent {
  protected final int dimension = Problem.dimension;
  protected final int[] prices = Problem.profit;
  protected final int[][] weigths = Problem.weigths;
  protected final int[] capacity = Problem.capacity;
  protected final int type = Problem.type;
  
  public boolean typeController = false;
  protected int[] idea = new int[dimension];
  private boolean best = false;

  public Agent() {
	if(type == 0) {
		typeController = true;
	}
    do {
      for (int j = 0; j < dimension; j++) {
        idea[j] = StdRandom.uniform(2);
      }
    } while (!isFeasible());
  }

  protected boolean isBetterThan(Agent bestGlobal) {
    return fitness() > bestGlobal.fitness();
  }

  public int fitness() {
    int sum = 0;
    for (int j = 0; j < dimension; j++) {
      sum = sum + (idea[j] * prices[j]);
    }
    return sum;
  }

  protected boolean isFeasible() {
    return type == 0 ? isFeasibleCapability() : isFeasibleMultidimentionalCapability();
  }

  protected boolean isFeasibleMultidimentionalCapability() {
    int[] sum = new int[dimension];
    boolean feasible = true;
    for (int h = 0; h < dimension; h++) {
      for (int j = 0; j < dimension; j++) {
        sum[h] = sum[h] + (idea[j] * weigths[h][j]);
      }
    }
    for (int k = 0; k < dimension; k++) {
      if (sum[k] > capacity[k]) {
        feasible = false;
      }
    }
    return (feasible);
  }

  protected boolean isFeasibleCapability() {
    int sum = 0;
    System.out.println(dimension);
    for (int j = 0; j < dimension; j++) {
      sum = sum + (idea[j] * weigths[0][j]);
    }
    return (sum <= capacity[0]);
  }
  
  public void setBest(boolean state) {
    best = state;
  }

  public boolean isBest() {
    return best;
  }

  protected void move1(Agent bestAgent) {
    int fitness = bestAgent.fitness();
    int fitnessFlag = fitness();
    boolean flag = false;
    while (flag == false) {
      for (int j = 0; j < dimension; j++) {
        idea[j] = binary(bestAgent.idea[j] + StdRandom.uniform());

      } // practicamente el dominio de idea[] será {0,1} ej:[0,1,1,0,0]
      fitnessFlag = fitness();
      if (fitnessFlag > fitness && isFeasible()) {
        flag = true;
      } else {
        if (fitnessFlag == fitness && isFeasible()) {
          flag = true;
        }
      }
    }
  }

  protected void move2(Agent best, Agent rand, Agent prom, double stepSize) {
    for (int j = 0; j < dimension; j++) {
      // (3) newX = Xi(t) + r * stepSize(t) * d0d0 = Xi(t) + phiP;
      int d0 = best.idea[j] - idea[j];
      idea[j] = binary(idea[j] + StdRandom.uniform() * stepSize * d0);

      int alpha1 = StdRandom.uniform(3) - 1; // {-1,0,1}
      int beta1 = StdRandom.uniform(3); // {0,1,2}

      int d1 = alpha1 * d0 + beta1 * (rand.idea[j] - idea[j]);

      // (4) newXi = newX + r * stepSize(t) * d1d1
      idea[j] = binary(idea[j] + StdRandom.uniform() * stepSize * d1);
      // (5) phiG=7 * (Xj(t)/N); -------- N es el total de conjuntos de ideas?

      // (6) newXi2 = newXi + r * stepSize(t) * d2
      int alpha2 = StdRandom.uniform(3) - 1; // {-1,0,1}
      int beta2 = StdRandom.uniform(3); // {0,1,2}
      int d2 = alpha2 * d1 + beta2 * (prom.idea[j] - idea[j]);

      idea[j] = binary(idea[j] + StdRandom.uniform() * stepSize * d2);

      // (7) newXi3 = newXi2 + r * stepSize(t) * d3
      int alpha3 = StdRandom.uniform(3) - 1; // {-1,0,1}
      int beta3 = StdRandom.uniform(3); // {0,1,2}
      int d3 = alpha3 * d2 + beta3 * (best.idea[j] - idea[j]);
      idea[j] = binary(idea[j] + StdRandom.uniform() * stepSize * d3);

      // (9) (r1 <= MFnewXi4) = newXi3newXip4 = LB(p) + r2 * (UB(p) - LB(p));
      if (StdRandom.uniform() <= 1) {
        do {
          for (int h = 0; h < dimension; h++) {
            idea[h] = StdRandom.uniform(2);
          }
        } while (!isFeasible());
      }
    }
  }

  protected void copy(Object obj) {
    if (obj instanceof Agent) {
      System.arraycopy(((Agent) obj).idea, 0, this.idea, 0, this.idea.length);
    }
  }

  @Override
  public String toString() {
    return String.format("fitness: %s, solucion: %s", fitness(), java.util.Arrays.toString(idea));
  }

  protected void setIdea(int[] idea) {
    System.arraycopy(idea, 0, this.idea, 0, this.idea.length);
  }

  private int binary(double x) {
      return StdRandom.uniform() <= (1 / (1 + Math.pow(Math.E, -x))) ? 1 : 0;
  }
}