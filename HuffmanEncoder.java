import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import javax.swing.JFileChooser;
import java.util.*;

public class HuffmanEncoder {
	HashMap<Character,Integer> counts = new HashMap<Character,Integer>();
	HashMap<Character,String> charCodeMap = new HashMap<Character,String>();
	BinaryTree<Singleton> huffmanTree; 

	
	/**
   * Constructor for Huffman Encoder
   * Makes a Hashmap with character counts as keys, frequencies as values
   */
	public HuffmanEncoder(BufferedReader inputFile) throws IOException {
	counts=putCharInMap(inputFile);
	}
	
	
	/**
   * Puts up a fileChooser and gets the path name for the file to be opened.
   * Returns an empty string if the user clicks Cancel.
   * @return path name of the file chosen
   */
 public static String getFilePath() {
   // Create a file chooser.
   JFileChooser fc = new JFileChooser();
   int returnVal = fc.showOpenDialog(null);
   if (returnVal == JFileChooser.APPROVE_OPTION) {
     File file = fc.getSelectedFile();
     String pathName = file.getAbsolutePath();
     return pathName;
   }
   else
     return "";
  }
 
 /**
  * Creates frequency map where characters are keys and frequencies are values.   
  * Called in the constructor of the Huffman Encoder
  */
 public HashMap<Character, Integer> putCharInMap(BufferedReader inputFile) throws IOException{
 try {
   int cInt = inputFile.read();   // read the next character's integer representation
   
   while (cInt != -1) {
     // Code to process the character whose Unicode value is cInt. 
     char letter = (char) cInt;
     if (counts.containsKey(letter)){
    	 counts.put(letter, counts.get(letter)+1);
     }
     else {
    	 counts.put(letter, 1);
     }
     cInt = inputFile.read(); // read the next character's integer representation
   }
   return counts;
 }
 finally {
   inputFile.close();
 } 
 }

 /**
  * Makes a Huffman Tree which we use later for compression/ decompression.   
  */
 
 public BinaryTree<Singleton> makeHuffmanTree() {
	 // priority queue for singleton trees. Size is size of map. 
 
 	 if (counts.size()==0)
 		 return null;
	 PriorityQueue<BinaryTree<Singleton>> minQueue = new PriorityQueue<BinaryTree<Singleton>>(counts.size(), new TreeComparator<Object>());
 
	 for (char characterKey : counts.keySet()) {
		 Singleton singleton = new Singleton(characterKey, counts.get(characterKey));
		 BinaryTree<Singleton> Tree = new BinaryTree<Singleton>(singleton);
		 minQueue.add(Tree);

	 }
	 
	
	 
	 while (minQueue.size() > 1) {
		 BinaryTree<Singleton> minTree1 = minQueue.remove();
		 BinaryTree<Singleton> minTree2 = minQueue.remove();

		 int countSum = minTree1.getValue().myCount + minTree2.getValue().myCount;
		 Singleton newSingleton = new Singleton(countSum);
		 BinaryTree<Singleton> newSingletonTree = new BinaryTree<Singleton>(newSingleton);
		 
		 newSingletonTree.setLeft(minTree1);
		 newSingletonTree.setRight(minTree2);

		 minQueue.add(newSingletonTree);
		 
	 }
	 huffmanTree = minQueue.remove();
	 
	 if (counts.size()==1){
		 for (Character letter:counts.keySet()){
			 BinaryTree<Singleton> newTree = new BinaryTree<Singleton>(new Singleton(letter, 0), huffmanTree, huffmanTree);
			 huffmanTree=newTree;
			 return huffmanTree;
	 }
	 
 }
	 return huffmanTree;
 }
 
 
 /**
  * Generates a map where the character is a key, and its bit string is the value.
  * @returns hashmap with character as key, bitString is value    
  */

 public HashMap<Character, String> generateCodes() throws NullPointerException{
 String code="";

 // if there's only one character in the file, have its code be 1 
 if (huffmanTree != null) {
 		generateCodesHelper(huffmanTree,code);
 		}
 return charCodeMap;
 }
 	
 /**
  * Helper method to generateCodes()
  * Generates a map where the character is a key, and its bit string is the value.
  * 
  */
 public void generateCodesHelper(BinaryTree<Singleton> Node,String growingCode){
	
 	if(Node.isLeaf()) {
 		charCodeMap.put(Node.getValue().myCharacter, growingCode);
 	}
 	else {
 		generateCodesHelper(Node.getRight(), growingCode + "1");
 		generateCodesHelper(Node.getLeft(), growingCode + "0");
 	}
 }
 
 

 /**
  * Compresses a file at the given path. 
  * @throws IOException
  */
 
public String compressFile(String Path) throws IOException {
	makeHuffmanTree();
	generateCodes();
	BufferedReader inputFile = new BufferedReader(new FileReader(Path));
	String outputPath = Path.substring(0,Path.length()-4);
	BufferedBitWriter outputFile =  new BufferedBitWriter(outputPath+"compressed.txt");
	
	try {
	 
   int cInt = inputFile.read();   // read the next character's integer representation
   while (cInt != -1) {
     // Code to process the character whose Unicode value is cInt.
     char letter = (char) cInt;
     // Get the code for that letter 

     String code = charCodeMap.get(letter);
     
     // Go through it, bit by bit and add either an 0 or a 1 to the output file
     for (int i=0; i< code.length();i++){
        if(code.charAt(i)=='0')
               outputFile.writeBit(0);
        if(code.charAt(i)=='1')
               outputFile.writeBit(1);
        
     }
     cInt = inputFile.read(); // read the next character's integer representation
   }
	}

	finally {
		inputFile.close();
		outputFile.close();
	}
	return (outputPath +"compressed.txt");
	}
 
/**
 * Decompresses the file that was just compressed. 
 * @param compressedPath, the path of the compressed file
 * @throws IOException 
 *    
 */

public void decompressFile(String compressedPath) throws IOException {
	BufferedBitReader inputFile = new BufferedBitReader(compressedPath);
	String outputPath = compressedPath.substring(0,compressedPath.length()-4);
	BufferedWriter outputFile =  new BufferedWriter(new FileWriter(outputPath+"decompressed.txt"));
	
	try {
   int bitInt = 0;   // initialize next character's integer representation
   
   BinaryTree<Singleton> current;
   current = huffmanTree;
   
   while (bitInt != -1 && huffmanTree !=null) {
  	 bitInt = inputFile.readBit(); // read the next character

  	 if (current.isLeaf()) {
  		 outputFile.write(current.getValue().myCharacter);
  		 current = huffmanTree;
  	 }

  	 if (bitInt == 0) {
  		 current = current.getLeft();

  	 }
  	 else {
  		 current = current.getRight();

  	 }
   }
	}

	finally {
		inputFile.close();
		outputFile.close();
	}
	}



 public static void main(String [] args) throws IOException {

 // Set up an input files and make an encoder object with it
 String originalFilePath = getFilePath();
 BufferedReader inputFile = new BufferedReader(new FileReader(originalFilePath));   
 HuffmanEncoder myEncoder = new HuffmanEncoder(inputFile); 
 
 try {
	 // Compress the file and save its path 
	 String compressedFilePath = myEncoder.compressFile(originalFilePath);
	 
	 // De-compress the compressed file 
	 myEncoder.decompressFile(compressedFilePath);
	 inputFile.close();
	 
 } 
 // Catch any exceptions that the file choosers threw
 catch (IOException ex) {
	 System.out.println("IO Exception" + ex);
 }
  }
	
}

