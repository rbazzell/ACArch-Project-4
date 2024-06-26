package tomasulogui;

import tomasulogui.IssuedInst.INST_TYPE;

public class ReorderBuffer {
  public static final int size = 30;
  int frontQ = 0;
  int rearQ = 0;
  ROBEntry[] buff = new ROBEntry[size];
  int numRetirees = 0;
                                                                
  PipelineSimulator simulator;
  RegisterFile regs;
  boolean halted = false;

  public ReorderBuffer(PipelineSimulator sim, RegisterFile registers) {
    simulator = sim;
    regs = registers;
  }

  public ROBEntry getEntryByTag(int tag) {
    return buff[tag];
  }

  public int getInstPC(int tag) {
    return buff[tag].getInstPC();
  }

  public boolean isHalted() {
    return halted;
  }

  public boolean isFull() {
    return (frontQ == rearQ && buff[frontQ] != null);
  }

  public int getNumRetirees() {
    return numRetirees;
  }

  public boolean acceptStore(IssuedInst inst) {
    // TODO: Read value for reference register.
    if (isFull()) {
      throw new MIPSException("updateInstForIssue: no ROB slot avail");
    }
    ROBEntry newEntry = new ROBEntry(this);
    
    newEntry.copyInstData(inst, rearQ);
    if (inst.regSrc1Valid && inst.regSrc2Valid) {
      buff[rearQ] = newEntry;
      newEntry.storeMemoryLocation = inst.regSrc1Value + inst.immediate;
      
      rearQ = (rearQ + 1) % size;
      newEntry.writeValue = inst.regSrc2Value;
      newEntry.complete = true;

      
      return true;
    }
    return false;

  }

  public boolean retireInst() {
    // This function returns true when HALT is reached, NOT when an instruction
    //  is retired.
    // 3 cases
    // 1. regular reg dest inst
    // 2. isBranch w/ mispredict
    // 3. isStore
    ROBEntry retiree = buff[frontQ];

    if (retiree == null) {
      return false;
    }

    if (retiree.isHaltOpcode()) {
      halted = true;
      return true;
    }

    boolean shouldAdvance = true;

    // TODO - this is where you look at the type of instruction and
    // figure out how to retire it properly
    if (!retiree.isComplete()) {
      shouldAdvance = false;
    }
    else {
      switch (retiree.getOpcode()) {
        case JAL:
        case JALR:
          regs.setReg(31, retiree.instPC + 4);
          regs.setSlotForReg(31, -1);
        case BEQ:
        case BNE:
        case BLTZ:
        case BLEZ:
        case BGEZ:
        case BGTZ:
        case J:
        case JR:
        
          if (retiree.branchMispredicted()) {
            // This is a mispredicted branch instruction.
            //  When this happens, need to clear the buffer
            //  and commit real branch target to PC.
            //  Advance is also handled here.
            // Need an accepting function here?

            // Clear entire ROB and set new values for ROB
            numRetirees++;
            for (int i=0; i < size; i++) {
              buff[i] = null;
            }
            shouldAdvance = false;

            if (retiree.opcode == INST_TYPE.JR || retiree.opcode == INST_TYPE.JALR) {
              simulator.setPC(retiree.rob.regs.getReg(retiree.writeValue));
            } else if (!retiree.predictTaken) {
            //if taken
              simulator.setPC(retiree.address);
            } else {
              //if not taken:
              simulator.setPC(retiree.instPC + 4);
            }
            simulator.squashAllInsts();
            frontQ = 0;
            rearQ = 0;
            return false;
          }
          break;
        case STORE:
          // This is a store instruction. It's like the default but writes 
          //  to memory instead of to the register file.
          int storeAddress = retiree.getStoreAddress();
          simulator.getMemory().setIntDataAtAddr(storeAddress, retiree.getWriteValue());
          break;
        case NOP:
          break;
        default:
          // This is everything else.
          int destinationReg = retiree.getWriteReg();
          regs.setReg(destinationReg, retiree.getWriteValue());
          // Clear recorded register slot.
          // but only if this is the most recent tag
          if (regs.getSlotForReg(destinationReg) == frontQ) {
            regs.setSlotForReg(destinationReg, -1);
          }
      }
    }

    // if mispredict branch, won't do normal advance
    if (shouldAdvance) {
      numRetirees++;
      buff[frontQ] = null;
      frontQ = (frontQ + 1) % size;
    }

    return false;
  }

  public void readCDB(CDB cdb) {
    // check entire CDB for someone waiting on this data
    // could be destination reg
    // could be store address source

    // TODO body of method
    if (cdb.getDataValid()) {
      ROBEntry tagEntry = buff[cdb.getDataTag()];
      // Check if tag points to active entry.
      if (tagEntry != null && tagEntry.complete == false) {
        tagEntry.setWriteValue(cdb.getDataValue());
        tagEntry.complete = true;
      }
      // TODO: Handle stores
    }
  }

  public void updateInstForIssue(IssuedInst inst) {
    // the task is to simply annotate the register fields
    // the dest reg will be assigned a tag, which is just our slot#
    // all src regs will either be assigned a tag, read from reg, or forwarded from ROB

    // TODO - possibly nothing if you use my model
    // I use the call to copyInstData below to do 2 things:
    // 1. update the Issued Inst
    // 2. fill in the ROB entry

    // first get a ROB slot
    if (buff[rearQ] != null) {
      throw new MIPSException("updateInstForIssue: no ROB slot avail");
    }
    ROBEntry newEntry = new ROBEntry(this);
    buff[rearQ] = newEntry;
    newEntry.copyInstData(inst, rearQ);

    rearQ = (rearQ + 1) % size;
  }

  public int getTagForReg(int regNum) {
    return (regs.getSlotForReg(regNum));
  }

  public int getDataForReg(int regNum) {
    return (regs.getReg(regNum));
  }

  public void setTagForReg(int regNum, int tag) {
    regs.setSlotForReg(regNum, tag);
  }

}
