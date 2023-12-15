package tomasulogui;

public class ReservationStation {
  PipelineSimulator simulator;

  int tag1;
  int tag2;
  int data1;
  int data2;
  boolean data1Valid = false;
  boolean data2Valid = false;
  // destTag doubles as branch tag
  int destTag;
  IssuedInst.INST_TYPE function = IssuedInst.INST_TYPE.NOP;

  // following just for branches
  int addressTag;
  boolean addressValid = false;
  int address;
  boolean predictedTaken = false;

  public ReservationStation(PipelineSimulator sim) {
    simulator = sim;
  }

  public int getDestTag() {
    return destTag;
  }

  public int getData1() {
    return data1;
  }

  public int getData2() {
    return data2;
  }

  public boolean isPredictedTaken() {
    return predictedTaken;
  }

  public IssuedInst.INST_TYPE getFunction() {
    return function;
  }

  public void snoop(CDB cdb) {
    //snoop for data1
    if (!data1Valid) {
      if (cdb.getDataValid() && cdb.getDataTag() == tag1) {
        data1 = cdb.getDataValue();
        data1Valid = true;
      }
    }

    //snoop for data2
    if (!data2Valid) {
      if (cdb.getDataValid() && cdb.getDataTag() == tag2) {
        data2 = cdb.getDataValue();
        data2Valid = true;
      }
    }
  }

  public boolean isReady() {
    return data1Valid && data2Valid;
  }

  public void loadInst(IssuedInst inst) {
    function = inst.getOpcode();
    switch (inst.getOpcode()) {
      case ADD:
      case SUB:
      case MUL:
      case DIV:
      case AND:
      case OR:
      case XOR:
      case SLL:
      case SRL:
      case SRA:
        destTag = inst.getRegDestTag();
        if (inst.getRegSrc1Valid()) {
          data1 = inst.getRegSrc1Value();
          data1Valid = true;
        } else {
          tag1 = inst.getRegSrc1Tag();
        }

        if (inst.getRegSrc2Valid()) {
          data2 = inst.getRegSrc2Value();
          data2Valid = true;
        } else if (inst.regSrc2Used) {
          tag2 = inst.getRegSrc2Tag();
        } else {
          data2 = inst.getImmediate();
          data2Valid = true;
        }
        break;
      case LOAD:
      case STORE:
      case ADDI:
      case ANDI:
      case ORI:
      case XORI:
      case BEQ:
      case BNE:
      case BLTZ:
      case BLEZ:
      case BGTZ:
      case BGEZ:
      case JR:
      case JALR:
        //grabs first reg
        if (inst.getRegSrc1Valid()) {
          data1 = inst.getRegSrc1Value();
          data1Valid = true; 
        } else {
          tag1 = inst.getRegSrc1Tag();
        }
        data2 = inst.getImmediate(); //always use immed
        data2Valid = true;


        //if src2used, then it is a control op, so no dest
        destTag = inst.getRegDestTag();

        if (inst.determineIfBranch()) {
          address = inst.getBranchTgt();
        }
        break;
      case J:
      case JAL:
        data1 = inst.getImmediate();
        data1Valid = true;
        data2 = 0;
        data2Valid = true;
        destTag = inst.getRegDestTag();
        address = inst.branchTgt;
        break;
      default:
        break;
    }
  }
}
