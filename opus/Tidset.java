package opus;

import java.util.ArrayList;

public class Tidset extends ArrayList<Long>{

	private static final long serialVersionUID = 1L;

	public void add(long TID){
		super.add(TID);		 
	}
	
	// count the size of the intersection
	// relies on the sets both being stored in ascending order
	public static int count_intersection(Tidset s1, Tidset s2){
		
		if (s1.size() == 0 || s2.size() == 0){
			return 0;
		}
		
		int it1 = 0;
		long v1 = s1.get(it1);
		int end1 = s1.size() - 1;
		
		int it2 = 0;
		long v2 = s2.get(it2);
		int end2 = s2.size() - 1;
		
		int count = 0;
		while (true){
			if (v1 == v2){
				count++;
				it1++;
				if (it1 == end1) break;
				v1 = s1.get(it1);
				it2++;
				if (it2 == end2) break;
				v2 = s2.get(it2);
			}else if (v1 < v2){
				it1++;
				if (it1 == end1) break;
				v1 = s1.get(it1);
			}else{
				it2++;
				if (it2 == end2) break;
				v2 = s2.get(it2);
			}
		}
		return count;
	}
	
	// find the intersection of two tidsets
	// relies on the sets both being stored in ascending order
	public static void intersection(Tidset result, Tidset s1, Tidset s2){
		result.clear();
		result.ensureCapacity(Math.min(s1.size(), s2.size()));
		
		if (s1.size() == 0 || s2.size() == 0){
			return;
		}
		
		int it1 = 0;
		long v1 = s1.get(it1);
		int end1 = s1.size();
		
		int it2 = 0;
		long v2 = s2.get(it2);
		int end2 = s2.size();
		
		while (true){
			if (v1 == v2){
				result.add(v1);
				it1++;
				if (it1 == end1) break;
				v1 = s1.get(it1);
				it2++;
				if (it2 == end2) break;
				v2 = s2.get(it2);
			}else if (v1 < v2){
				it1++;
				if (it1 == end1) break;
				v1 = s1.get(it1);
			}else{
				it2++;
				if (it2 == end2) break;
				v2 = s2.get(it2);
			}
		}
		
	}
	
	// destructively update s1 to its intersection with s2
	public static void dintersection (Tidset s1, Tidset s2){
		if (s1.size() == 0){
			return;
		}
		
		if (s2.size() == 0){
			s1.clear();
			return;
		}
		
		//TODO unsigned int
		int from = 0;
		int to = 0;
		
		long v1 = s1.get(0);
		//int end1 = s1.size() - 1;
		
		int it2 = 0;
		long v2 = s2.get(it2);
		int end2 = s2.size() - 1;
		
		while (true){
			if (v1 == v2){
				s1.set(to++, s1.get(from++));
				if (from == s1.size()) break;
				v1 = s1.get(from);
				it2++;
				if (it2 == end2) break;
				v2 = s2.get(it2);
			}else if (v1 < v2){
				from++;
				if (from == s1.size()) break;
				v1 = s1.get(from);
			}else{
				it2++;
				if (it2 == end2) break;
				v2 = s2.get(it2);
			}
		}
		s1.ensureCapacity(to);
	}
	
	// destructively update s1 to its union with s2
	public static void dunion(Tidset s1, Tidset s2){
		Tidset result = new Tidset();
		
		int it1 = 0;
		int it2 = 0;
		
		while (true){
			if (it1 == s1.size()){
				while (it2 != s2.size()){
					result.add(s2.get(it2));
					it2++;
				}
				break;
			}else if (it2 == s2.size()){
				while (it1 != s1.size()){
					result.add(s1.get(it1));
					it1++;
				}
				break;
			}else if (s1.get(it1) == s2.get(it2)){
				result.add(s1.get(it1));
				it1++;
				it2++;
			}else if (s1.get(it1) < s2.get(it2)){
				result.add(s1.get(it1));
				it1++;
			}else{
				result.add(s2.get(it2));
				it2++;
			}
		}
		
		s1 = result;
	}
	
}
