package cat.calidos.eurinome.runtime;

import java.util.function.Function;
import java.util.function.Predicate;

import org.zeroturnaround.exec.stream.LogOutputStream;


public abstract class ExecOutputProcessor extends LogOutputStream {

private Function<String, Integer> matcher;

public ExecOutputProcessor(Function<String, Integer> matcher) {
	this.matcher = matcher;
}


/**	@return given a log line, it returns how much is remaining in the current state,
* 	or negative if there is an error or some kind of problem. The remaining time is undefined for long running tasks.
* 	Values are from 100 to 1 for percentage, 0 for stage completed and negative for errors
*/
public int process(String line) {
	return matcher.apply(line);
}

}
