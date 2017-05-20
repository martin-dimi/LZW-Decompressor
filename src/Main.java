import java.io.File;
import java.io.IOException;

public class Main {
	public static void main (String[] args) throws IOException{
		File file = new File("C:\\Users\\Martin\\workspace\\Decompressor\\src\\compressedfile3.z");
		Decompressor decompressor = new Decompressor(file);
		
		decompressor.printFile();
	}
}
