package tomasulogui;

public class IntAlu extends FunctionalUnit{
  public static final int EXEC_CYCLES = 1;

  public IntAlu(PipelineSimulator sim) {
    super(sim);
  }

  public void execCycle(CDB cdb) {
    if (canWriteback) {
      stations[writebackEntry] = null;
    }
  }
 
  public int calculateResult(int station) {
    ReservationStation rest = stations[station];
    switch (rest.getFunction()) {
      case ADD:
      case ADDI:
        return rest.getData1() + rest.getData2();
      case SUB:
        return rest.getData1() - rest.getData2();
      case AND:
      case ANDI:
        return rest.getData1() & rest.getData2();
      case OR:
      case ORI:
        return rest.getData1() | rest.getData2();
      case XOR:
      case XORI:
        return rest.getData1() ^ rest.getData2();
      case SLL:
        return rest.getData1() << rest.getData2();
      case SRL:
        return rest.getData1() >>> rest.getData2();
      case SRA:
        return rest.getData1() >> rest.getData2();
      default:
        return -1;
    }
  }

  public int getExecCycles() {
    return EXEC_CYCLES;
  }


}
