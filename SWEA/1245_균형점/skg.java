import java.util.StringTokenizer;
import java.io.BufferedReader;
import java.io.InputStreamReader;

// 핵심 아이디어 : 이분 탐색
// n개의 자성체가 있으면 자성체 사이사이 구간으로 n-1 개의 균형점이 생긴다.
// 1. float로 했다가 실패
// -> 이거 왜 이래요?
// 2. while 문을 오차가 특정 수 이하로 줄어들 때까지로 두니까 무한반복 도는 케이스가 생겨버림 
// -> 무한반복문으로 두고, 더 이상 값의 변화가 없을 때 종료

class Solution {
	static int N;
	static double[] coor;
	static double[] m;

	public static void main(String args[]) throws Exception {
		StringTokenizer st = null;
		try (BufferedReader br = new BufferedReader(new InputStreamReader(System.in))) {
			int T;
			T = Integer.parseInt(br.readLine());

			for (int test_case = 1; test_case <= T; test_case++) {
				N = Integer.parseInt(br.readLine());
				
				coor = new double[N];
				m = new double[N];
				st = new StringTokenizer(br.readLine());
				for (int i = 0; i < N; i++) {
					coor[i] = Double.parseDouble(st.nextToken());
				}
				for (int i = 0; i < N; i++) {
					m[i] = Double.parseDouble(st.nextToken());
				}
				
				StringBuilder sb = new StringBuilder();
				sb.append("#" + test_case + " ");
				double l, r, mid;
				for (int i = 0; i < N-1; i++) {
					l = coor[i];
					r = coor[i+1];
					mid = (l + r) / 2;
					double res = force(i, mid);
					while(true) {
						if (mid == l || mid == r)	break;
						if (res < 0) {
							l = mid;
						} else {
							r = mid;
						} 
						mid = (l + r) / 2;
						res = force(i, mid);
					}
					sb.append(String.format("%.10f ", mid));
				}
				
				System.out.println(sb.toString());
				
			}
		}
		

	}
	
	// k와 k+1 사이에 있는 x 위치에 미치는 인력의 총합 (음수면 왼쪽에서 끌어당기는 힘이 더 크다는 의미)
	static double force(int k, double x) {
		double left = 0f, right = 0f;
		for (int i = 0; i <= k; i++) {
			double dist = x - coor[i];
			left += m[i] / (dist * dist);
		}
		for (int i = k+1; i < N; i++) {
			double dist = x - coor[i];
			right += m[i] / (dist * dist);
		}
		return right - left;
	}
}
