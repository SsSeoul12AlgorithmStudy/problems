package 빙하이동;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Queue;

class UserSolution {
	private final static int MAX_N = 100;

	class RESULT {
		int[][] heights;

		RESULT() {
			heights = new int[MAX_N][MAX_N];
		}
	}

	class Glacier {
		int x, y, d;

		public Glacier(int x, int y, int d) {
			this.x = x;
			this.y = y;
			this.d = d;
		}
	}

	// 상 우 하 좌 순서
	int[] dx = { -1, 0, 1, 0 }, dy = { 0, 1, 0, -1 };

	// 빙하 높이 정보
	int[][] map;

	// 빙하 별 좌표 정보
	ArrayList<ArrayList<Glacier>> gp;

	// 0. 빙하 하나의 모든 좌표 찾기
	void bfs0(int X, int Y, int d, int id, int N) {

		Queue<int[]> q = new ArrayDeque<>();
		q.offer(new int[] { X, Y, d });
		gp.get(id).add(new Glacier(X, Y, d));

		boolean[][] visited = new boolean[N][N];
		visited[X][Y] = true;

		while (!q.isEmpty()) {
			int[] now = q.poll();

			int x = now[0], y = now[1];

			for (int i = 0; i < 4; i++) {
				int nx = (x + dx[i] + N) % N;
				int ny = (y + dy[i] + N) % N;

				if (visited[nx][ny] || map[nx][ny] == 0)
					continue;

				visited[nx][ny] = true;
				gp.get(id).add(new Glacier(nx, ny, d));
				q.offer(new int[] { nx, ny, d });
			}
		}
	}

	// 1. 융해
	void bfs1() {

		int N = map.length;

		Queue<int[]> eg = new ArrayDeque<>(); // 바다와 인접한 -1 감소할 빙하

		// 빙하 수
		for (ArrayList<Glacier> ng : gp) {

			// 빙하 별 좌표 수
			for (Glacier p : ng) {

				boolean isAroundSea = false;
				// 4방
				for (int i = 0; i < 4; i++) {
					int nx = (p.x + dx[i] + N) % N;
					int ny = (p.y + dy[i] + N) & N;

					if (map[nx][ny] == 0) {
						isAroundSea = true;
						break;
					}
				}

				// 바다인접
				if (isAroundSea) {
					eg.add(new int[] { p.x, p.y });
				}
			}
		}

		// 0이 된 녀석은? 이동 할 때 체크하면서 map[x][y] = 0
		while (!eg.isEmpty()) {
			int[] now = eg.poll();

			int x = now[0], y = now[1];
			map[x][y]--;
		}
	}

	// 2. 이동
	

	// 3. 병합

	// init
	void init(int N, int M, int mIceBlock[][], int mIceGroup[][]) {
		// mIceBlock - 좌표별 빙하 높이 정보
		// mIceGroup - 빙하 구성 얼음덩어리 중 1개 x,y,d

		map = new int[N][N];

		for (int x = 0; x < N; x++) {
			for (int y = 0; y < N; y++) {
				map[x][y] = mIceBlock[x][y];
			}
		}

		gp = new ArrayList<>();// 빙하별 좌표 정보

		// 초기 상태 분류
		for (int i = 0; i < M; i++) {
			int x = mIceGroup[i][1], y = mIceGroup[i][0], d = mIceGroup[i][2];
			gp.add(new ArrayList<>());
			bfs0(x, y, d, i, N);
		}
	}

	// 1년 후 빙하 상태 반환
	RESULT oneYearLater() {
		RESULT res = new RESULT();

		// 1. 융해 - 상하좌우 바다와 인접한 얼음덩어리는 높이 -1
		bfs1();

		// 2. 이동 - 상하좌우 중 빙하의 방향으로 1칸씩 이동

		// 3. 병합 - 빙하 충돌 or 상하좌우 인접 경우 => 하나의 빙하로 병합

		return res;
	}

}
