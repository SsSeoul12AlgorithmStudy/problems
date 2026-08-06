package samsung01;
import java.util.StringTokenizer;
import java.io.BufferedReader;
import java.io.InputStreamReader;

// 뭔가 당연히 균등하게 나눠서 곱하는 게 최대가 될 것 같은데 증명을 딱 잘라서 못하겠음;;
// 귀류법으로 해야하나? N * N * N vs. N * N-1 * N+1 = N^3 - N
class Solution {
	static int N, P;

	public static void main(String args[]) throws Exception {
		StringTokenizer st = null;
		try (BufferedReader br = new BufferedReader(new InputStreamReader(System.in))) {
			int T;
			T = Integer.parseInt(br.readLine());

			for (int test_case = 1; test_case <= T; test_case++) {
				long answer = 1;
				st = new StringTokenizer(br.readLine());
				N = Integer.parseInt(st.nextToken());
				P = Integer.parseInt(st.nextToken());
				
				int[] pocket = new int[P];
				int mod = N % P;
				for (int i = 0; i < P; i++) {
					pocket[i] = N / P + (i < mod ? 1 : 0);
				}
				
				for (int i = 0; i < P; i++) {
					answer *= (long)pocket[i];
				}
				
				System.out.println("#" + test_case + " " + answer);
				
			}
		}
		

	}
	
	
}