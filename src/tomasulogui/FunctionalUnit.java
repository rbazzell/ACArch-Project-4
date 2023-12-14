package tomasulogui;

public abstract class FunctionalUnit {
  private static final int STATION_COUNT = 2;

  PipelineSimulator simulator;
  ReservationStation[] stations = new ReservationStation[STATION_COUNT];
  
  public FunctionalUnit(PipelineSimulator sim) {
    simulator = sim;
    
  }

 
  public void squashAll() {
    // todo fill in
  }

  public abstract int calculateResult(int station);

  public abstract int getExecCycles();

  public void execCycle(CDB cdb) {
    //todo - start executing, ask for CDB, etc.
  }



  public void acceptIssue(IssuedInst inst) {
  // todo - fill in reservation station (if available) with data from inst
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
