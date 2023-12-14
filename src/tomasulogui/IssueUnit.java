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
      switch (getExecType(inst)) {
        case LOAD:
          fu = simulator.getLoader();
          if (((LoadBuffer)fu).isReservationStationAvail() && !rob.isFull()) {
            ((LoadBuffer)fu).acceptIssue(issuee);
            pc.incrPC();
          }
          break;
        case ALU:
          fu = simulator.getALU();
          if (((IntAlu)fu).isReservationStationAvail() && !rob.isFull()) {
            ((IntAlu)fu).acceptIssue(issuee);
            pc.incrPC();
          }
          break;
        case MULT:
          fu = simulator.getMult();
          if (((IntMult)fu).isReservationStationAvail() && !rob.isFull()) {
            ((IntMult)fu).acceptIssue(issuee);
            pc.incrPC();
          }
          break;
        case DIV:
          fu = simulator.getDivider();
          if (((IntDivide)fu).isReservationStationAvail() && !rob.isFull()) {
            ((IntDivide)fu).acceptIssue(issuee);
            pc.incrPC();
          }
          break;
        case BRANCH:
          fu = simulator.getBranchUnit();
          if (((BranchUnit)fu).isReservationStationAvail() && !rob.isFull()) {
            ((BranchUnit)fu).acceptIssue(issuee);
            pc.incrPC();
          }
          break;
        case NONE:
        default:
          return; 
          //TODO - implement halting/no op operation
      }

      
      }
      // to issue, we make an IssuedInst, filling in what we know
      //DONE



      // We check the BTB, and put prediction if branch, updating PC
      //     if pred taken, incr PC otherwise
      // TODO



      // We then send this to the ROB, which fills in the data fields
      // TODO INSIDE FU



      // We then check the CDB, and see if it is broadcasting data we need,
      //    so that we can forward during issue
      // TODO INSIDE FU

      // We then send this to the FU, who stores in reservation station
      // TODO INSIDE FU

    private EXEC_TYPE getExecType(Instruction inst) {
      switch (Instruction.getNameFromOpcode(inst.getOpcode())) {
        case "LW":
        case "SW":
          return EXEC_TYPE.LOAD;
        case "ADD":
        case "ADDI":
        case "SUB":
          return EXEC_TYPE.ALU;
        case "MUL":
          return EXEC_TYPE.MULT;
        case "DIV":
          return EXEC_TYPE.DIV;
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
        default:
          return EXEC_TYPE.NONE;
      }
    }
  }
