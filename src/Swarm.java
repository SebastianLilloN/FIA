import java.util.ArrayList;

public class Swarm {

  private ArrayList<Agent> group = null;
  private int quantityAgents = 100;
  private int T = 100;
  private double stepSize;
  private int exit = 0;
  private Agent a = new Agent();

  protected void execute() {
    init();
    iterations();
  }

  private void init() {
    group = new ArrayList<>();
    for (int i = 1; i <= quantityAgents; i++) {
      group.add(new Agent());
    }
  }

  private void iterations() {
    long start = System.currentTimeMillis();
    int t = 1;
    while (t <= T) {
      int best = 0;
      int j;
      for (int i = 1; i < quantityAgents; i++) {
        if (group.get(best).isBetterThan(group.get(i))) {
          group.get(best).setBest(true);
          group.get(i).setBest(false);
        } else {
          group.get(best).setBest(false);
          group.get(i).setBest(true);
          best = i;
        }
      }
      for (int i = 0; i < quantityAgents; i++) {
        if (group.get(i).isBest() && exit != 1) {
          a.copy(group.get(i));
          group.get(i).fitness();
          exit = 1;
        }
      }
      stepSize = 2 - 1.7 * ((t - 1) / (T - 1));
      for (int i = 0; i < quantityAgents; i++) {
        if (group.get(i).isBest()) {

          group.get(i).move1(group.get(i)); // Mandar el mejor agente
        } else {
          do {
            j = StdRandom.uniform(quantityAgents);
          } while (j != i);

          int[] idea = new int[a.idea.length];

          for (int k = 0; k < idea.length; k++) {
            int sum = 0;
            for (int l = 0; l < quantityAgents; l++) {
              sum += group.get(l).idea[k];
            }
            idea[k] = binary(sum / quantityAgents);
          }
          Agent prom = new Agent();
          prom.setIdea(idea);

          group.get(i).move2(a, group.get(j), prom, stepSize);
        }
      }
      // System.out.println("Iteracion n°: " + t);
      t = t + 1;
    }
    long end = System.currentTimeMillis();
    for (int i = 0; i < quantityAgents; i++) {
      if (group.get(i).isBest()) {
    	  if(group.get(i).typeController == true) {
    		  System.out.println("-------------------------------------KP---------------------------------------------------");
    	  }else {
    		  System.out.println("-------------------------------------MKP---------------------------------------------------");
    	  }
        System.out.println("Numero de iteraciones: " + T);
        System.out.println("Tiempo de iteracion: " + ((end - start)) + " milisegundos");
        System.out.println("Numero de agentes dentro de la poblacion: " + quantityAgents);
        System.out.println("Numero de dimension de la idea de cada agente: " + a.idea.length);
        System.out.println("El mejor fitness con su solucion al momento de crear la poblacion");
        System.out.println(a);
        System.out.println("El mejor fitness con su solucion al momento de realizar las n iteraciones");
        System.out.println(group.get(i));
        System.out.println("----------------------------------------------------------------------------------------");
      }
    }
  }

  private int binary(double x) {
      return StdRandom.uniform() <= (1 / (1 + Math.pow(Math.E, -x))) ? 1 : 0;
  }
}