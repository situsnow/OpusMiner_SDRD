package opus;


public class Test {

	public static void main(String[] args) {

		int n = 8416;
		int count = 3472;
		
		int count1 = 4176;
		
		int count2 = 3808;
		
		int a = n - count1 - count2 + count;
		int b = count1 - count;
		int c = count2 - count;
		int d = count;
		
		System.out.println(Fisher.fisherTest(a, b, c, d));
		
	}

}
