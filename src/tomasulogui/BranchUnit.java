package tomasulogui;

public class BranchUnit
    extends FunctionalUnit {
  public static final int EXEC_CYCLES = 1;

  public BranchUnit(PipelineSimulator sim) {
    super(sim);
  }

  public int calculateResult(int station) {
    // todo fill in
    ReservationStation rest = stations[station];
    switch (rest.getFunction()) {
      case BEQ:
        if (rest.getData1() == rest.getData2()) {
          rest.getDestTag();
          return 1;
        }
        break;
      case BNE:
        if (rest.getData1() != rest.getData2()) {
          return 1;
        }
        break;
      case BLTZ:
        if (rest.getData1() < 0) {
          return 1;
        }
        break;
      case BLEZ:
        if (rest.getData1() <= 0) {
          return 1;
        }
        break;
      case BGTZ:
        if (rest.getData1() > 0) {
          return 1;
        }
        break;
      case BGEZ:
        if (rest.getData1() >= 0) {
          return 1;
        }
        break;
      case J:
      case JAL:
      case JR:
      case JALR:
        return 1;
      default:
        return 0;
    }
    return 0;
  }

  public int getExecCycles() {
      return EXEC_CYCLES;
  }
}
