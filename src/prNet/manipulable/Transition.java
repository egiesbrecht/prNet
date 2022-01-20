package prNet.manipulable;

public interface Transition<T> {
	
	/**
	 * Applies the given lambda-expression to manipulate an element.
	 * 
	 * @param toChange The element that should be manipulated
	 * @param patternPart The reference of a pattern that contains the new information that changes the toChange variable
	 */
	public void apply(T toChange, T patternPart);
	
}
