import java.io.File;

public class Main {
	public static void main (String[] args){
		Decompressor d = new Decompressor("compressedfile4.z");
		
		d.decode();
		System.out.println(d.getPrint());
	}
}
