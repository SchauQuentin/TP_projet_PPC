import lombok.Getter;
import org.chocosolver.solver.Model;
import org.chocosolver.solver.variables.IntVar;

public class Scheduling
{
    @Getter
    private Model model;
    private Activity[] tasks;
    private Instance instance;
    private IntVar l_max;
    private IntVar[] l_activities;

    public Scheduling(Instance instance) {
        this.instance = instance;

        model = new Model("Scheduling");
        tasks = new Activity[instance.getN()];

        int min_dd_activity = Integer.MAX_VALUE; //the minimum of due date of all activity
        int max_lower_bound_l_activity = Integer.MIN_VALUE;

        //initialisation of activity and l_activity
        for (int activity = 0; activity < instance.getN(); activity++) {
            //activity
            tasks[activity] = new Activity(activity, instance.getR(activity),(instance.sumP() + instance.maxR()), instance.getP(activity),model);

            //l_activity
            int lower_bound_l_activity = instance.getR(activity) + instance.getP(activity) - instance.getDD(activity);
            int upper_bound_l_activity = instance.maxR() + instance.sumP() - instance.getP(activity) - instance.getDD(activity);
            l_activities[activity] = model.intVar("L_" + activity, lower_bound_l_activity, upper_bound_l_activity);

            //min dd_activity
            if (instance.getDD(activity) <= min_dd_activity) {
                min_dd_activity = instance.getDD(activity);
            }

            if (lower_bound_l_activity > max_lower_bound_l_activity) {
                max_lower_bound_l_activity = lower_bound_l_activity;
            }
        }

        //initialisation l_max
        l_max = model.intVar("l_max", max_lower_bound_l_activity, (instance.sumP() + instance.maxR()) - min_dd_activity);

        constraint_creation();
    }

    private void constraint_creation() {

        //disjunction constraints
        for (int i = 0; i < instance.getN(); i++) {
            for (int j = i + 1; i < instance.getN(); j++) {
                model.or(model.arithm(tasks[i].getEnd(),"<=",tasks[j].getStart()), model.arithm(tasks[j].getEnd(),"<=",tasks[i].getStart())).post();
            }
        }

        for (int i = 0; i < instance.getN(); i++) {
            //l_activity constraint 
            model.arithm(l_activities[i], "=", tasks[i].getEnd(),"-", instance.getDD(i)).post();

            //proc constraint
            model.arithm(tasks[i].getEnd(), "-", tasks[i].getStart(), "=", tasks[i].getProc()).post();
        }

        model.max(l_max, l_activities).post();
    }


}
