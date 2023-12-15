package tomasulogui;

public class BranchUnit extends FunctionalUnit {
  public static final int EXEC_CYCLES = 1;

  public BranchUnit(PipelineSimulator sim) {
    super(sim);
  }

  public int calculateResult(int station) {
    // todo fill in
    ReservationStation rest = stations[station];
    switch (rest.getFunction()) {
      case BEQ:
        return rest.getData1() == rest.getData2() ? 1 : 0;
      case BNE:
        return rest.getData1() != rest.getData2() ? 1 : 0;
      case BLTZ:
        return rest.getData1() < 0 ? 1 : 0;
      case BLEZ:
        return rest.getData1() <= 0 ? 1 : 0;
      case BGTZ:
        return rest.getData1() > 0 ? 1 : 0;
      case BGEZ:
        return rest.getData1() >= 0 ? 1 : 0;
      case J:
      case JAL:
      case JR:
      case JALR:
        return writeData;
      default:
        return 0;
    }
  }

  public int getExecCycles() {
      return EXEC_CYCLES;
  }
}
