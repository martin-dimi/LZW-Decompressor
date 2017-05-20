
public class test {
	public static void main(String[] args){
		
		int a = 0xFFFFFF73;
		long b;
		if(a < 0){
			b = Integer.toUnsignedLong(a); 	
			System.out.println(b);

		}
		
		
		System.out.println(Integer.MAX_VALUE);
	}
}
