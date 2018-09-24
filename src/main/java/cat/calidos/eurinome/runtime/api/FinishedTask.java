package cat.calidos.eurinome.runtime;


public interface FinishedTask extends Task {



public boolean wasOk();


default public boolean failed() { 
	return !wasOk(); 
}


public int result();


}
