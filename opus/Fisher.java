package opus;

import java.util.ArrayList;
import java.util.List;

public class Fisher {

	static List<Double> lf = new ArrayList<Double>();
	private static Fisher fisherInstance;
	private Fisher(){
		//private constructor
	}
	
	public static Fisher getInstance(){
		if (fisherInstance == null){
			fisherInstance = new Fisher();
		}
		
		return fisherInstance;
	}
	
	static double logfact(final int n){
		
		for (int i = lf.size(); i <= n; i++){
			if (i == 0) lf.add(0.0);
			else lf.add(lf.get(i - 1) + Math.log((double)i));
		}
		
		return lf.get(n);
		
	}
	public double log_combin(final int n, final int k){
		return logfact(n) - logfact(k) - logfact(n - k);
	}
	
	public static double fisherTest(int a, int b, int c, int d){
		double p = 0.0;
		
		if (b < c){
			int t = b;
			b = c;
			c = t;
		}
		
		//Use log here to scale the data range.
		/**
		 * The actual p value in Fisher Exact Test is
		 * [(a+b)!(c+d)!(a+c)!(b+d)!] / (a!b!c!d!n!)
		 */
		double invariant = -logfact(a+b+c+d)+logfact(a+b)+logfact(c+d)+logfact(a+c)+logfact(b+d);
		
		do{
			p += Math.exp(invariant-logfact(a)-logfact(b)-logfact(c)-logfact(d));
			a++;
			b--;
			c--;
			d++;
		}while (c >= 0);
		
		return p;
	}
}
