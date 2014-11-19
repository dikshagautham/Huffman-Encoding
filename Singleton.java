/**
 * Singleton Tree data structure 
 * Keeps track of two values: a character and a count 
 */

public class Singleton {	
	char myCharacter;
	int myCount; 
	
	// constructor 
	public Singleton(char character, int count) {
		myCharacter = character;
		myCount = count; 
	}
	
	// constructor 
	public Singleton(int count) {
		myCount = count; 
	}
	
	public char getCharacter() {
		return myCharacter;
	}

	public int getCount() {
		return myCount;
	}	

	public String toString() {
		return "Character is " + myCharacter + "," + "Count is "+ myCount;
	}	

}