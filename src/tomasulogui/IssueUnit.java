package tomasulogui;

public class IssueUnit {
  private enum EXEC_TYPE {
    NONE, LOAD, ALU, MULT, DIV, BRANCH} ;

  PipelineSimulator simulator;
  IssuedInst issuee;
  Object fu;


  public IssueUnit(PipelineSimulator sim) {
    simulator = sim;
  }

  public void execCycle() {
    MemoryModel mem = simulator.getMemory();
    ProgramCounter pc = simulator.getPCStage();
    Instruction inst = mem.getInstAtAddr(pc.getPC());
    ReorderBuffer rob = simulator.getROB();
    // an execution cycle involves:
    // 1. checking if ROB and Reservation Station avail
    // 2. issuing to reservation station, if no structural hazard
    issuee = IssuedInst.createIssuedInst(inst);
    issuee.setPC(pc.getPC());

      // VVV Tells where to accept what VVV
    switch (getExecType(inst)) {
      case LOAD:
        fu = simulator.getLoader();
        if (((LoadBuffer)fu).isReservationStationAvail() && !rob.isFull()) {
          rob.updateInstForIssue(issuee);
          ((LoadBuffer)fu).acceptIssue(issuee);
          pc.incrPC();
        }
        break;
      case ALU:
        fu = simulator.getALU();
        if (((IntAlu)fu).isReservationStationAvail() && !rob.isFull()) {
          rob.updateInstForIssue(issuee);
          ((IntAlu)fu).acceptIssue(issuee);
          pc.incrPC();
        }
        break;
      case MULT:
        fu = simulator.getMult();
        if (((IntMult)fu).isReservationStationAvail() && !rob.isFull()) {
          rob.updateInstForIssue(issuee);
          ((IntMult)fu).acceptIssue(issuee);
          pc.incrPC();
        }
        break;
      case DIV:
        fu = simulator.getDivider();
        if (((IntDivide)fu).isReservationStationAvail() && !rob.isFull()) {
          rob.updateInstForIssue(issuee);
          ((IntDivide)fu).acceptIssue(issuee);
          pc.incrPC();
        }
        break;
      case BRANCH:
        fu = simulator.getBranchUnit();
        if (((BranchUnit)fu).isReservationStationAvail() && !rob.isFull()) {
          rob.updateInstForIssue(issuee);
          ((BranchUnit)fu).acceptIssue(issuee);
          // In theory, this sets the PC, but if not use the code below it.
          simulator.btb.predictBranch(issuee);
          /*if (issuee.getBranchPrediction() == true) {
            pc.setPC(issuee.getBranchTgt());
          }
          else {
            pc.incrPC();
          }*/
        }
        break;
      case NONE:
        if (issuee.getOpcode() == IssuedInst.INST_TYPE.STORE) {
          rob.updateInstForIssue(issuee);
          pc.incrPC();
        }
        if (issuee.getOpcode() == IssuedInst.INST_TYPE.HALT) {
          rob.updateInstForIssue(issuee);
          pc.incrPC();
        }
      default:
        return; 
        //TODO - implement halting/no op operation && store
    }
    // to issue, we make an IssuedInst, filling in what we know
    //DONE


    /* Branch prediction stuff
     * We check the BTB, and put prediction if branch, updating PC
     *     if pred taken, incr PC otherwise
     * DONE
     * 
     * 
     * We then send this to the ROB, which fills in the data fields
     * TODO
     * 
     * 
     * We then check the CDB, and see if it is broadcasting data we need,
     *    so that we can forward during issue
     * TODO
    */
  }
    

  private EXEC_TYPE getExecType(Instruction inst) {
    switch (Instruction.getNameFromOpcode(inst.getOpcode())) {
      case "LW":
        return EXEC_TYPE.LOAD;
      case "MUL":
        return EXEC_TYPE.MULT;
      case "DIV":
        return EXEC_TYPE.DIV;
      case "ADD":
      case "ADDI":
      case "SUB":
      case "AND":
      case "ANDI":
      case "OR":
      case "ORI":
      case "XOR":
      case "XORI":
      case "SLL":
      case "SRL":
      case "SRA":
        return EXEC_TYPE.ALU;
      case "BEQ":
      case "BNE":
      case "BLTZ":
      case "BLEZ":
      case "BGTZ":
      case "BGEZ":
      case "J":
      case "JAL":
      case "JR":
      case "JALR":
        return EXEC_TYPE.BRANCH;
      case "NOP":
      case "HALT":
      case "SW":
      default:
        return EXEC_TYPE.NONE;
    }
  }
}