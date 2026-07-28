// DFS로 했지롱
import java.io.*;
import java.util.*;

class Place {
	char type;
	int cost, rate;

	Place(char type, int cost, int rate) {
		this.type = type;
		this.cost = cost;
		this.rate = rate;
	}
}

public class Solution {
	final static int MAX_T = 9 * 60;

	// 정답 저장용
	static int ans;
	static Deque<Integer> ans_path;

	static Deque<Integer> path;
	static int N, M;
	// 장소 정보 저장용 배열
	static Place[] pcs;
	// 이동 비용 저장용 배열
	static int[][] edge;
	// 확정 기록용 배열
	static boolean[] fix;

	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		try (BufferedReader br = new BufferedReader(new InputStreamReader(System.in))) {
			StringTokenizer st = null;
			int T = Integer.parseInt(br.readLine());
			for (int test_case = 1; test_case <= T; test_case++) {
				ans = 0;
				int airport = -1;
				path = new ArrayDeque<>();

				st = new StringTokenizer(br.readLine());
				N = Integer.parseInt(st.nextToken());
				M = Integer.parseInt(st.nextToken());

				pcs = new Place[N];
				edge = new int[N][N];
				fix = new boolean[N];
				for (int i = 0; i < N - 1; i++) {
					st = new StringTokenizer(br.readLine());
					for (int j = i + 1; j < N; j++) {
						int c = Integer.parseInt(st.nextToken());
						edge[i][j] = c;
						edge[j][i] = c;
					}
				}
				for (int i = 0; i < N; i++) {
					st = new StringTokenizer(br.readLine());
					char type;
					type = st.nextToken().toCharArray()[0];
					switch (type) {
					case 'A':
						// 시작점인 공항 넣어주기.
						airport = i;
					case 'H':
						pcs[i] = new Place(type, 0, 0);
						break;
					case 'P':
						int cost = Integer.parseInt(st.nextToken());
						int rate = Integer.parseInt(st.nextToken());
						pcs[i] = new Place(type, cost, rate);
						break;
					}
				}

				DFS(0, 0, 0, false, airport);

				System.out.printf("#%d %d", test_case, ans);
				if (ans != 0)
					ans_path.forEach(v -> System.out.print(" " + (v + 1)));
				System.out.println();
			}
		}
	}

	static void DFS(int day, int used_t, int total_rate, boolean goHome, int last_come) {
//		System.out.printf("DFS! day: %d, remain time: %d, total rate: %d, last_come: %d\n", day, used_t, total_rate,
//				last_come);
		if (used_t > MAX_T || day == M)
			return;

		if (goHome) {
			if (total_rate > ans) {
				ans = total_rate;
				ans_path = new ArrayDeque<>();
				for (Integer k : path) {
					ans_path.offerLast(k);
				}
			}
			return;
		}

		for (int i = 0; i < N; i++) {
			if (fix[i] || i == last_come)
				continue;
			int new_t = used_t + edge[last_come][i] + (pcs[i].type == 'P' ? pcs[i].cost : 0);
			if (new_t > MAX_T)
				continue;
			switch (pcs[i].type) {
			case 'P':
				fix[i] = true;
				path.offerLast(i);
				DFS(day, new_t, total_rate + pcs[i].rate, false, i);
				path.removeLast();
				fix[i] = false;
				break;
			case 'H':
				path.offerLast(i);
				DFS(day + 1, 0, total_rate, false, i);
				path.removeLast();
				break;
			case 'A':
				path.offerLast(i);
				DFS(day, new_t, total_rate, true, i);
				path.removeLast();
				break;
			}
		}
	}

}
