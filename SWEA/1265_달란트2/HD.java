import java.io.*;
import java.util.StringTokenizer;
/*
 @@ 베스트 조합은 N/P 값(소수점x)으로 일정량을 배치하고 N/P값보다 큰 값들 몇개의 조합으로 만들어짐
 -> 이 식을 구현해보려했는데 잘 안됐음. -> N의 범위가 100이하니까 완탐 시도해봄 
 
 1) 완전탐색 시도 -> 10초 걸려서 통과
 
 */
public class Solution {

	static int N, P;
	static long max;
	static int[] comb;

	public static void main(String[] args) throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

		int T = Integer.parseInt(br.readLine());

		StringBuilder result = new StringBuilder();
		for (int tc = 1; tc <= T; tc++) {
			StringTokenizer st = new StringTokenizer(br.readLine());

			N = Integer.parseInt(st.nextToken());
			P = Integer.parseInt(st.nextToken());
			comb = new int[P];
			max = 0;
			findComb(1, 0, 0);

			result.append("#" + tc + " " + max + "\n");
		}

		System.out.println(result);
	}

	private static long getCandy() {
		long candy = 1;
		for (int c : comb) {
			candy *= c;
		}

		return candy;
	}

	private static void findComb(int node, int depth, int sum) {

		if (depth == P) {
			if (sum == N) {
				long now = getCandy();
				max = max > now ? max : now;
			}
			return;
		}

		for (int i = node; i < N; i++) {
			if (sum + i <= N) {
				comb[depth] = i;
				findComb(i, depth + 1, sum + i);
			} else {
				return;
			}
		}
	}
}
