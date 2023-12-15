package tomasulogui;

public class ROBEntry {
  ReorderBuffer rob;

  // TODO - add many more fields into entry
  // I deleted most, and only kept those necessary to compile GUI
  boolean complete = false;
  boolean predictTaken = false;
  boolean mispredicted = false;
  int instPC = -1;
  int writeReg = -1;
  int writeValue = -1;
  // Register storing memory location for stores
  // MIGHT NOT NEED THIS IF IT'S JUST FROM CDB
  int storeMemoryLocation = -1;
  // NEED BRANCH TARGET LOCATION?

  IssuedInst.INST_TYPE opcode = null;

  public ROBEntry(ReorderBuffer buffer) {
    rob = buffer;
  }

  public boolean isComplete() {
    return complete;
  }

  public boolean branchMispredicted() {
    return mispredicted;
  }

  public boolean getPredictTaken() {
    return predictTaken;
  }

  public int getInstPC() {
    return instPC;
  }

  public IssuedInst.INST_TYPE getOpcode () {
    return opcode;
  }

  public boolean isHaltOpcode() {
    return (opcode == IssuedInst.INST_TYPE.HALT);
  }

  public void setBranchTaken(boolean result) {
    // TODO - maybe more than simple set
  }

  public int getWriteReg() {
    return writeReg;
  }

  public int getStoreAddress() {
    return storeMemoryLocation;
  }

  public void setStoreAddress(int addr) {
    storeMemoryLocation = addr;
  }
  
  public int getWriteValue() {
    return writeValue;
  }

  public void setWriteValue(int value) {
    writeValue = value;
  }

  public void copyInstData(IssuedInst inst, int frontQ) {



    //update inst with data from entry
    // all src regs will either be assigned a tag, read from reg, or forwarded from ROB
    

    // VVV this for loop is funny looking, but does what I want it to
    // VVV hahaha it's only 1:45am - this will be rough
    // now it's 4:15am - it has been rough
    for (int tag = rob.frontQ; tag != rob.rearQ; tag = (tag + 1) % ReorderBuffer.size) {
      ROBEntry entry = rob.buff[tag];
      if (inst.regSrc1Used && entry.writeReg == inst.regSrc1 && entry.isComplete()) { //if regSrc1 in rob and valid, forward
        inst.setRegSrc1Value(entry.writeValue);
        inst.setRegSrc1Valid();
      }
      if (inst.regSrc2Used && entry.writeReg == inst.regSrc2 && entry.isComplete()) { //if regSrc2 in rob and valid, forward
        inst.setRegSrc2Value(entry.writeValue);
        inst.setRegSrc2Valid();
      }
    }

    if (inst.regSrc1Used && !inst.getRegSrc1Valid()) {
      inst.setRegSrc1Tag(rob.getTagForReg(inst.regSrc1));
    }
    if (inst.regSrc2Used && !inst.getRegSrc2Valid()) {
      inst.setRegSrc2Tag(rob.getTagForReg(inst.regSrc2));
    }

    if (inst.regSrc1Used && !inst.getRegSrc1Valid() && inst.regSrc1Tag == -1) { // if src1 not in rob (complete or tagged), go to reg
      inst.setRegSrc1Value(rob.getDataForReg(inst.regSrc1));
      inst.setRegSrc1Valid();
    }
    if (inst.regSrc2Used && !inst.getRegSrc2Valid() && inst.regSrc2Tag == -1) { // if src2 not in rob (complete or tagged), go to reg
      inst.setRegSrc2Value(rob.getDataForReg(inst.regSrc2));
      inst.setRegSrc2Valid();
    } 
    
    if (inst.regDestUsed) {
      inst.setRegDestTag(frontQ);
    }
        
    //grab info from instruction to modify entry
    instPC = inst.getPC();
    writeReg = inst.getRegDest();
    opcode = inst.getOpcode();
    if (writeReg > -1) { //for any insts that don't modify the registers
      rob.setTagForReg(writeReg, frontQ);
    }
    // TODO - This is a long and complicated method, probably the most complex
    // of the project.  It does 2 things:
    // 1. update the instruction, as shown in 2nd line of code above
    //    2nd line above sets the tag to the entry number in this ROB (frontQ here)
    //     
    // 2. update the fields of the ROBEntry, as shown in the 1st line of code above
    //    Need to get PC of instruction (done above)
    //    Need to set destination register (writeReg)
    //    C
    // In the case of jumps based on registers, it potentially is marked as 
    //  mispredicted instantly if the target value is found in ROB.
    //  This may be handled in branch functional unit?
  }

}
