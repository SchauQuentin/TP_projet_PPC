import org.chocosolver.solver.Model;
import org.chocosolver.solver.Solution;
import org.chocosolver.solver.variables.IntVar;

public class Main {

    public static void main(String[] args) {

        Instance instance = new Instance("test");
        instance.println();

        Scheduling scheduling = new Scheduling(instance);

        Solution solution = scheduling.getModel().getSolver().findSolution();
        if(solution != null){
            System.out.println(solution.toString());
        }else{
            System.out.println("Aucune solution trouvee");
        }

    }


    public static void carre_magique(int n) {
        Model model = new Model("Carre magique " + n + "*" + n);
        //variable
        IntVar[] vars = new IntVar[n];
        for (int var = 0; var < n; var++) {
            vars[var] = model.intVar("X" + var, 1, 9);
        }

        //contrainte
        model.allDifferent(vars).post();
        model.sum(new IntVar[]{vars[0],vars[1],vars[2]},"=",15 ).post();
        model.sum(new IntVar[]{vars[3],vars[4],vars[5]},"=",15 ).post();
        model.sum(new IntVar[]{vars[6],vars[7],vars[8]},"=",15 ).post();
        model.sum(new IntVar[]{vars[0],vars[3],vars[6]},"=",15 ).post();
        model.sum(new IntVar[]{vars[1],vars[4],vars[7]},"=",15 ).post();
        model.sum(new IntVar[]{vars[2],vars[5],vars[8]},"=",15 ).post();
        model.sum(new IntVar[]{vars[0],vars[4],vars[8]},"=",15 ).post();
        model.sum(new IntVar[]{vars[6],vars[4],vars[2]},"=",15 ).post();

        Solution solution = model.getSolver().findSolution();
        if(solution != null){
            System.out.println(solution.toString());
        }else{
            System.out.println("Aucun solution trouver");
        }
    }
}
