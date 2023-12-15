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

  IssuedInst.INST_TYPE opcode;

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
    
    //grab info from instruction to modify entry
    instPC = inst.getPC();
    


    //update inst with data from entry
    inst.setRegDestTag(frontQ);

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
