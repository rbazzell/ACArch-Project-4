package tomasulogui;

public class IntDivide extends FunctionalUnit {

    public static final int EXEC_CYCLES = 7;

    public IntDivide(PipelineSimulator sim) {
        super(sim);
    }

    public int calculateResult(int station) {
        ReservationStation rest = stations[station];
        return rest.getData1() / rest.getData2();
    }

    public int getExecCycles() {
        return EXEC_CYCLES;
    }
}
