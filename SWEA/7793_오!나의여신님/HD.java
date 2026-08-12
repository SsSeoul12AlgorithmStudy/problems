import java.io.*;
import java.util.*;

public class Solution {

	static int N, M;
	static char[][] map;

	public static void main(String[] args) throws Exception {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

		int T = Integer.parseInt(br.readLine());

		StringBuilder result = new StringBuilder();
		for (int tc = 1; tc <= T; tc++) {
			StringTokenizer st = new StringTokenizer(br.readLine());
			N = Integer.parseInt(st.nextToken());
			M = Integer.parseInt(st.nextToken());

			List<Point> devils = new ArrayList<>();
			map = new char[N][M];
			int syr = 0, syc = 0;
			for (int r = 0; r < N; r++) {
				map[r] = br.readLine().toCharArray();
				for (int c = 0; c < M; c++) {
					if (map[r][c] == 'S') {
						syr = r;
						syc = c;
					} else if (map[r][c] == '*') {
						devils.add(new Point(r, c, 0));
					}
				}
			}

			int min = bfs(syr, syc, devils);

			if (min < 0) {
				result.append("#" + tc + " GAME OVER" + "\n");
				continue;
			}

			result.append("#" + tc + " " + min + "\n");

		}

		System.out.println(result);
	}

	private static int bfs(int syr, int syc, List<Point> devils) {

		boolean[][] visited = new boolean[N][M];
		int[][] affected = new int[N][M];
		for (int i = 0; i < N; i++) {
			Arrays.fill(affected[i], Integer.MAX_VALUE);
		}

		int[] dr = { -1, 0, 1, 0 }, dc = { 0, -1, 0, 1 };

		Queue<Point> q = new LinkedList<>();
		for (Point d : devils) {
			affected[d.r][d.c] = d.d;
			visited[d.r][d.c] = true;
			q.offer(d);
		}

		while (!q.isEmpty()) {
			Point np = q.poll();

			for (int i = 0; i < 4; i++) {
				int nr = np.r + dr[i];
				int nc = np.c + dc[i];

				if (nr < 0 || nc < 0 || nr >= N || nc >= M)
					continue;

				if (!visited[nr][nc] && (map[nr][nc] != 'X' && map[nr][nc] != 'D')) {
					visited[nr][nc] = true;
					int nd = np.d + 1;
					affected[nr][nc] = nd;
					q.offer(new Point(nr, nc, nd));
				}
			}
		}

		visited = new boolean[N][M];
		visited[syr][syc] = true;
		q.offer(new Point(syr, syc, 0));
		while (!q.isEmpty()) {
			Point np = q.poll();

			if (map[np.r][np.c] == 'D')
				return np.d;

			for (int i = 0; i < 4; i++) {
				int nr = np.r + dr[i];
				int nc = np.c + dc[i];

				if (nr < 0 || nc < 0 || nr >= N || nc >= M)
					continue;

				if (!visited[nr][nc] && (map[nr][nc] == '.' || map[nr][nc] == 'D') && affected[nr][nc] > np.d + 1) {
					visited[nr][nc] = true;
					q.offer(new Point(nr, nc, np.d + 1));
				}
			}
		}

		return -1;
	}

	static class Point {
		int r, c, d;

		public Point(int r, int c, int d) {
			this.r = r;
			this.c = c;
			this.d = d;
		}
	}
}
