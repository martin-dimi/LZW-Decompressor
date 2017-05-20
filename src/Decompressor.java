import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Decompressor {
	private int size;
	private byte[] file;
	private Map<Integer, String> dictionary;
	
	private List<Integer> values;

	Decompressor(File file) throws IOException{
		
		dictionary = new HashMap<>();
		initializeDictionary();
		
		values = new ArrayList<Integer>();
		
		this.file = Files.readAllBytes(file.toPath());
	}
	
	public void printFile() throws IOException{
		System.out.println("The length of the file is: " + file.length);
		decode();
	}
	
	private void initializeDictionary(){
		size = 0;
		for(int i=0; i < 256; i++){
			dictionary.put(i, String.valueOf((char)i));		
			size++;
		}
	}
	
	public void decode() throws FileNotFoundException{

		int mask8bits = 0xF0;
		int mask4bits = 0x0F;
	
		int wordA;
		int wordB;
		
		int index = 0;
		
		if(file.length % 3 == 0)
			for(int position = 0; position < file.length; position += 3){
				int a = Byte.toUnsignedInt(file[position]);
				int b = Byte.toUnsignedInt(file[position + 1]);
				int c = Byte.toUnsignedInt(file[position + 2]);
				
				wordA =  a << 4;
				wordA =  (b >> 4 & mask4bits) | wordA;
	
				
				wordB = (mask4bits & b) << 8;
				wordB = wordB | c;
	
				
				values.add(index, wordA);
				index++;
				values.add(index, wordB);
				index++;
			}
		
		else {
			for(int position = 0; position < file.length - 2; position += 3){
				int a = Byte.toUnsignedInt(file[position]);
				int b = Byte.toUnsignedInt(file[position + 1]);
				int c = Byte.toUnsignedInt(file[position + 2]);
				
				wordA =  a << 4;
				wordA =  (b >> 4 & mask4bits) | wordA;
	
				
				wordB = (mask4bits & b) << 8;
				wordB = wordB | c;
	
				
				values.add(index, wordA);
				index++;
				values.add(index, wordB);
				index++;
			}
			int a = Byte.toUnsignedInt(file[file.length-1]);
			int b = Byte.toUnsignedInt(file[file.length-2]);
			
			wordA = ((a & mask4bits) << 8) | b;
			values.add(index, wordA);
			index++;
		}
		
		
	//	System.out.println(values.toString());
		updateDictionary();
	}
	
	private void printValues(){
		String whole = "";
		for(int cur : values){
			whole += dictionary.get(cur);
		}
		System.out.println(whole);
	}
	
private void updateDictionary() throws FileNotFoundException{
		
		String entry = "";
		String output = "";
		String word = "";
		char ch;
		int prevcode, currcode;

		prevcode = values.get(0);
		output += dictionary.get(prevcode);
		
		
		for(int i = 1; i<values.size();i++){
			currcode = values.get(i);
			
			entry = dictionary.get(currcode);
			output += entry;
			System.out.println("the size: " + size + "the id: " + currcode + " and the character: " + entry);
			ch = entry.charAt(0);
			word = dictionary.get(prevcode) + ch;
			if(!dictionary.containsValue(word)){
				dictionary.put(size, word);
				size++;
			}
			
			prevcode = currcode;
			
			if(size == 4096){
				dictionary = new HashMap<>();
				initializeDictionary();
				
				size = 256;
			}
		}
		
		
		System.out.println(output);
	}
	
}

























