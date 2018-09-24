package cat.calidos.eurinome.runtime.api;


public interface FinishedTask extends Task {



public boolean wasOk();


default public boolean failed() { 
	return !wasOk(); 
}


public int result();


}
