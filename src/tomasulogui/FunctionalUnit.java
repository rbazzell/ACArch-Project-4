package tomasulogui;

public abstract class FunctionalUnit {
  protected static final int STATION_COUNT = 2;
  protected boolean canWriteback = false;
  protected boolean requestWriteback = false;
  protected int executing = 0;
  int writebackEntry = -1;
  int writeTag = -1;
  int writeData = -1;

  PipelineSimulator simulator;
  ReservationStation[] stations = new ReservationStation[STATION_COUNT];
  
  public FunctionalUnit(PipelineSimulator sim) {
    simulator = sim;
  }

  public int getWriteTag() {
    return writeTag;
  }

  public int getWriteData() {
    return writeData;
  }

  public int getWritebackEntry() {
    return writebackEntry;
  }

  public void setWriteTag(int value) {
    writeTag = value;
  }
  public void setWriteData(int value) {
    writeData = value;
  }
  public void setWritebackEntry(int value) {
    writebackEntry = value;
  }
  

  public void squashAll() {
    for (int i = 0; i < STATION_COUNT; i++) {
      stations[i] = null;
    }
    executing = 0;
    requestWriteback = false;
    writebackEntry = -1;
  }

  public abstract int calculateResult(int station);

  public abstract int getExecCycles();

  public void execCycle(CDB cdb) {
    //clear if written back
    if (canWriteback) {
      stations[writebackEntry] = null;
      writebackEntry = -1;
      requestWriteback = false;
      executing = 0;
    }
    //if not waiting to write back, execute
    if (!requestWriteback) {
      //if done, calculate result & request writeback
      if (executing == getExecCycles()) {
        requestWriteback = true;
        writeData = calculateResult(writebackEntry);
      //if haven't started, start execution
      } else if (executing == 0) {
        for (int i = 0; i < STATION_COUNT; i++) {
          ReservationStation rest = stations[i];
          if (rest != null) {
            if (rest.isReady()) {
              writebackEntry = i;
              writeTag = rest.getDestTag();
              executing ++;

              break;
            }
          }
        }
      //if executing, keep executing
      } else {
        executing ++;
      }
    }
    
    //fill in RSes with cdb data
    if (cdb.getDataValid()) {
      for (int i = 0; i < STATION_COUNT; i++) {
        if (stations[i] != null) {
          stations[i].snoop(cdb);
        }
      }
    }

    canWriteback = false;
  }

  public void setCanWriteback() {
    canWriteback = true;
  }

  public boolean isRequestingWriteback() {
    return requestWriteback;
  }

  public void acceptIssue(IssuedInst inst) {
    int slot;
    for (slot = 0; slot < STATION_COUNT; slot++) {
      if (stations[slot] == null) {
        break;
      }
    }
    if (slot == STATION_COUNT) {
      throw new MIPSException("FU accept issue: slot not available");
    }

    ReservationStation rest = new ReservationStation(simulator);
    stations[slot] = rest;

    rest.loadInst(inst);
  }

  public boolean isReservationStationAvail() {
    for (int i=0; i < STATION_COUNT; i++) {
      if (stations[i] == null) {
        return true;
      }
    }
    return false;
  }
}
