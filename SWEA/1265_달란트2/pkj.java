import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.StringTokenizer;

public class Solution {
	
	public static void main(String[] args) throws Exception {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		int T = Integer.parseInt(br.readLine());
		
		for(int test_case = 1; test_case <= T; test_case++) {
			long answer = 0;
			
			StringTokenizer st = new StringTokenizer(br.readLine());
			int N = Integer.parseInt(st.nextToken());
			int P = Integer.parseInt(st.nextToken());
			
			answer = findMax(N, P);
			
			System.out.println("#" + test_case + " " + answer);
		}
	}

	static long findMax(int N, int P) {
		long result = 1;
		
		int x = N / P;
		int y = N % P;

    //곱하는 값들끼리 차이가 적을수록 곱이 최대
		for(int i = 0; i < P; i++) {
			if(y!=0) {
				result *= (x+1);
				y--;
			}
			else result *= x;
		}
		
		return result;
	}
}
