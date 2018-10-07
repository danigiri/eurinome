package cat.calidos.eurinome.runtime.api;


public interface FinishedTask extends Task {


default public boolean failed() { 
	return !isOK(); 
}


public int result();


}
