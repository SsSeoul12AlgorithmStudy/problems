import java.io.*;
import java.util.*;

/*

소요시간 - 3시간 초과
- 문제이해 1시간 소요
- 이분탐색 까먹어서 구현방법 다시 찾아봄

 */
public class Solution {

	static int N;
	static List<Node> nodes;

	public static void main(String[] args) throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

		int T = Integer.parseInt(br.readLine());

		StringBuilder result = new StringBuilder();

		for (int tc = 1; tc <= T; tc++) {
			N = Integer.parseInt(br.readLine());
			StringTokenizer st = new StringTokenizer(br.readLine());

			int[] xArr = new int[N];
			for (int i = 0; i < N; i++) {
				xArr[i] = Integer.parseInt(st.nextToken());
			}

			nodes = new ArrayList<>();
			for (int i = 0; i < N; i++) {
				int m = Integer.parseInt(st.nextToken());
				nodes.add(new Node(xArr[i], m));
			}

			Collections.sort(nodes, (a, b) -> Integer.compare(a.x, b.x));

			result.append("#").append(tc).append(" ");

			findBalancePoints(result);

			result.append("\n");
		}

		System.out.print(result);
	}

	private static void findBalancePoints(StringBuilder sb) {
		for (int i = 0; i < N - 1; i++) {
			double balancePoint = binarySearch(i);

			sb.append(String.format("%.10f", balancePoint));
			if (i < N - 2) {
				sb.append(" ");
			}
		}
	}

	private static double binarySearch(int k) {
		double low = nodes.get(k).x;
		double high = nodes.get(k + 1).x;

		for (int iter = 0; iter < 100; iter++) {
			double mid = (low + high) / 2.0;

			double leftForce = 0.0;
			double rightForce = 0.0;

			for (int i = 0; i <= k; i++) {
				double d = mid - nodes.get(i).x;
				leftForce += (double) nodes.get(i).m / (d * d);
			}

			for (int j = k + 1; j < N; j++) {
				double d = nodes.get(j).x - mid;
				rightForce += (double) nodes.get(j).m / (d * d);
			}

			if (leftForce > rightForce) {
				low = mid;
			} else {
				high = mid;
			}
		}

		return low;
	}

	static class Node {
		int x, m;

		public Node(int x, int m) {
			this.x = x;
			this.m = m;
		}
	}
}