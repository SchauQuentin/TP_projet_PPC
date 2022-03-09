import lombok.Getter;
import lombok.Setter;
import org.chocosolver.solver.Model;
import org.chocosolver.solver.variables.IntVar;

public class Activity {

    @Getter @Setter
    private IntVar start;
    @Getter @Setter
    private IntVar end;
    @Getter @Setter
    private IntVar proc;

    public Activity(int index_of_activity, int r_i, int u_b, int p_i, Model model) {
        start = model.intVar("start_" + index_of_activity, r_i, u_b - p_i);
        end = model.intVar("end_" + index_of_activity, r_i + p_i, u_b);
        proc = model.intVar("proc_" + index_of_activity, p_i);
    }
}
