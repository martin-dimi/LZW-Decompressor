package decompressor;

public class Main {
	public static void main (String[] args){
		
		Decompressor f1 = new Decompressor("compressedfile1.z");
		Decompressor f2 = new Decompressor("compressedfile2.z");
		Decompressor f3 = new Decompressor("compressedfile3.z");
		Decompressor f4 = new Decompressor("compressedfile4.z");
		
		f1.decode();
		f2.decode();
		f3.decode();
		f4.decode();
		
		System.out.println(f3.getPrint());
	}
}
