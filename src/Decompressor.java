import java.io.File;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Decompressor {
	private Map<Integer, String> dictionary;
	private int size;
	private List<Integer> indexes;
	private StringBuffer output;
	private byte[] file;

	
	/*Constructor for a file given*/
	Decompressor(File file){
		dictionary = new HashMap<>();
		initializeDictionary();
		indexes = new ArrayList<Integer>();
		output = new StringBuffer();
		
		try{
			this.file = Files.readAllBytes(file.toPath());
		}
		catch(Exception e){
			System.out.println("Error: file not found");
			System.exit(0);
		}
	}	
	
	/*Constructor for a fileName given*/
	Decompressor(String fileName){
		this(new File(fileName));
	}
	
	/*Initialises the dictionary with all of the ASCII symbols (0 - 255)*/
	private void initializeDictionary(){
		size = 0;
		for(int i=0; i < 256; i++){
			dictionary.put(i, String.valueOf((char)i));		
			size++;
		}
	}
	
	/*Unpacks a set of three bytes into two 12-bits and Stores them for later decoding*/
	private void unpack(int offSet){
		
		int mask4bits = 0x0F;
		int wordA;
		int wordB;	
		
		for(int position = 0; position < file.length - offSet; position += 3){
			int a = Byte.toUnsignedInt(file[position]);
			int b = Byte.toUnsignedInt(file[position + 1]);
			int c = Byte.toUnsignedInt(file[position + 2]);
			
			wordA =  a << 4;
			wordA =  wordA | (b >> 4 & mask4bits);

			wordB = (mask4bits & b) << 8;
			wordB = wordB | c;
			
			indexes.add(wordA);
			indexes.add(wordB);
		}
		
		//Case when there are odd number of codes, it packs the last pair
		if(offSet == 2 || offSet == 1){
			int a = Byte.toUnsignedInt(file[file.length-1]);
			int b = Byte.toUnsignedInt(file[file.length-2]);
			
			wordA = ((b << 8) & 0xf00) | a;
			indexes.add(wordA);
		}
	}
	

	private void decompress(){
		String prevSymbol = "" + (char)(int)indexes.remove(0);
		output.append(prevSymbol);
		
		for (int currIndex : indexes) {
	        String entry = "";
	        if (dictionary.containsKey(currIndex))
	            entry = dictionary.get(currIndex);
	        else
	        	entry = prevSymbol + prevSymbol.charAt(0);	// when size == currIndex
     
	        //Adding the code to the dictionary
	        dictionary.put(size++, prevSymbol + entry.charAt(0));
	        
	        output.append(entry);
	        prevSymbol = entry;
	        
	        //When all the possible codes have been used, the dictionary reinitialises 
			if(size == 4096){
				dictionary = new HashMap<>();
				initializeDictionary();
			}
	    }
	}
	
	/*Main function to be called for decoding*/
	public void decode(){
		
		//System.out.println("Decompressing...");
			
		int offSet = file.length % 3;
		unpack(offSet);
		decompress();
		
		//System.out.println("Decompressing - finished");	
	}
	
	/*Returns the decoded file as a string*/
	public String getPrint(){
		if(output.length() == 0) {
			return "Error: the file is not decoded";
		}
		return output.toString();
	}
}

























