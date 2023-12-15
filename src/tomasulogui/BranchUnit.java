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
        if (rest.getData1() == rest.getData2()) {
          return rest.address;
        }
        break;
      case BNE:
        if (rest.getData1() != rest.getData2()) {
          return rest.address;
        }
        break;
      case BLTZ:
        if (rest.getData1() < 0) {
          return rest.address;
        }
        break;
      case BLEZ:
        if (rest.getData1() <= 0) {
          return rest.address;
        }
        break;
      case BGTZ:
        if (rest.getData1() > 0) {
          return rest.address;
        }
        break;
      case BGEZ:
        if (rest.getData1() >= 0) {
          return rest.address;
        }
        break;
      case J:
      case JAL:
      case JR:
      case JALR:
        return rest.address;
      default:
        return 0;
    }
    return 0;
  }

  public int getExecCycles() {
      return EXEC_CYCLES;
  }
}
