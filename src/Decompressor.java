import java.io.File;
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

	Decompressor(File file){
		
		dictionary = new HashMap<>();
		initializeDictionary();
		values = new ArrayList<Integer>();
		
		try{
			this.file = Files.readAllBytes(file.toPath());
		}
		catch(Exception e){
			System.out.println("Error: file not found");
		}
		
	}	
	
	private void initializeDictionary(){
		size = 0;
		for(int i=0; i < 256; i++){
			dictionary.put(i, String.valueOf((char)i));		
			size++;
		}
	}
	
	public void decode(){
		
		System.out.println("Decompressing");
			
		int offSet = file.length % 3;
		unpack(offSet);
		decompress();
		
	}
	
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
			
			values.add(wordA);
			values.add(wordB);
		}
		
		if(offSet == 2){
			int a = Byte.toUnsignedInt(file[file.length-1]);
			int b = Byte.toUnsignedInt(file[file.length-2]);
			
			wordA = ((b << 8) & 0xf00) | a;
			
			values.add(wordA);
		}
	}
	

	private void decompress(){
		String prevSymbol = "" + (char)(int)values.remove(0);
		StringBuffer output = new StringBuffer(prevSymbol);
		
		for (int currIndex : values) {	// starting from 1, because we removed 0
	        String entry = "";
	        if (dictionary.containsKey(currIndex))
	            entry = dictionary.get(currIndex);
	        else if (currIndex == size)
	        	entry = prevSymbol + prevSymbol.charAt(0);	// when size == index
     
	        dictionary.put(size++, prevSymbol + entry.charAt(0));
	        
	        output.append(entry);
	        prevSymbol = entry;
	        
			if(size == 4096){
				dictionary = new HashMap<>();
				initializeDictionary();
			}
	    }
		
		System.out.println(output);
	}
}

























