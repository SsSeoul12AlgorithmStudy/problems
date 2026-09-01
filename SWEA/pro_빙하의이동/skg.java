import java.util.*;

class UserSolution {
	private final static int MAX_N = 100;

	class RESULT {
		int[][] heights;

		RESULT() {
			heights = new int[MAX_N][MAX_N];
		}
	}

	final int[][] dir = { { -1, 0 }, { 0, 1 }, { 1, 0 }, { 0, -1 } };

	int N;
	int[][] h;
	int[][] d;
	int[][] g;
	List<Group> g_list;
	int[] root;
	int[][] next_h;
	int[][] next_d;
	int[][] next_g;
	
	void init(int N, int M, int mIceBlock[][], int mIceGroup[][]) {
		this.N = N;
		h = mIceBlock;
		d = new int[N][N];
		for (int i = 0; i < N; i++)
			Arrays.fill(d[i], -1);
		for (int i = 0; i < M; i++) {
			int x = mIceGroup[i][0];
			int y = mIceGroup[i][1];
			int dv = mIceGroup[i][2];
			BFS_dir(y, x, dv);
		}
	}

	RESULT oneYearLater() {
		RESULT res = new RESULT();

		// 융해
		next_h = new int[N][N];

		for (int i = 0; i < N; i++) {
			for (int j = 0; j < N; j++) {
				if (isMelt(i, j)) {
					next_h[i][j] = h[i][j] - 1;
				}
				next_h[i][j] = h[i][j] - (isMelt(i,j) ? 1 : 0);
			}
		}
		h = next_h;

		// 빙하 그룹을 만들고 정보 저장.
		// g : 이차원 배열, 칸 별 그룹정보
		// g_list : 그룹에 대한 정보 (부피, 면적, 좌상단 좌표, 방향)
		g = new int[N][N];
		for (int i = 0; i < N; i++)
			Arrays.fill(g[i], -1);
		g_list = new ArrayList<>();

		int g_cnt = 0;
		for (int i = 0; i < N; i++) {
			for (int j = 0; j < N; j++) {
				if (g[i][j] == -1 && h[i][j] > 0) {
					g_list.add(BFS_group(i, j, g_cnt));
					g_cnt++;
				}
			}
		}

		// 이동 : 충돌한 빙하끼리 하나의 그룹으로 묶기
		// union-find 식으로
		// find : root 찾기
		// union : a와 b의 root 중 우선순위 높은 녀석을 최상위 부모 노드로 설정.
		root = new int[g_cnt];
		for (int i = 0; i < g_cnt; i++)
			root[i] = i;

		next_h = new int[N][N];
		next_g = new int[N][N];
		for (int i = 0; i < N; i++)
			Arrays.fill(next_g[i], -1);

		for (int i = 0; i < N; i++) {
			for (int j = 0; j < N; j++) {
				if (h[i][j] == 0)	continue;
				int dv = d[i][j];
				int ni = (i + dir[dv][0] + N) % N;
				int nj = (j + dir[dv][1] + N) % N;
				// 충돌 처리
				if (next_h[ni][nj] > 0) {
					int other_gid = next_g[ni][nj];
					union(other_gid, g[i][j]);
					if (next_h[ni][nj] < h[i][j]) {
						next_h[ni][nj] = h[i][j];
						next_g[ni][nj] = g[i][j];
					}
				} else {
					next_h[ni][nj] = h[i][j];
					next_g[ni][nj] = g[i][j];
				}
			}
		}

		// 병합 : 인접한 빙하 하나의 그룹으로 묶기 (flood-fill + union)
		int[][] vis = new int[N][N];
		for (int i = 0; i < N; i++) {
			for (int j = 0; j < N; j++) {
				if (vis[i][j] == 0 && next_h[i][j] > 0) {
					BFS_adj_group(i, j, vis);
				}
			}
		}
		
		// 그룹별로 다음 방향 정보 공유
		next_d = new int[N][N];
		for (int i = 0; i < N; i++)
			Arrays.fill(next_d[i], -1);
		
		for (int i = 0; i < N; i++) {
			for (int j = 0; j < N; j++) {
				if (next_g[i][j] < 0)	continue;
				next_d[i][j] = g_list.get(find(next_g[i][j])).dir;
			}
		}
		
		// 방향/높이 정보 업데이트
		d = next_d;
		h = next_h;

		res.heights = h;
		
		return res;
	}
	
	// next_h를 BFS로 돌면서 cur의  next_g와  다음 좌표의 next_g를 union.
	void BFS_adj_group(int x, int y, int[][] vis) {
		Queue<Node> Q = new ArrayDeque<>();
		Q.add(new Node(x, y));
		vis[x][y] = 1;
		
		while (!Q.isEmpty()) {
			Node cur = Q.poll();
			for (int[] dir_xy : dir) {
				int dx = (cur.x + dir_xy[0] + N) % N;
				int dy = (cur.y + dir_xy[1] + N) % N;
				if (vis[dx][dy] > 0 || next_h[dx][dy] == 0)	continue;
				union(next_g[cur.x][cur.y], next_g[dx][dy]);
				vis[dx][dy] = 1;
				Q.add(new Node(dx, dy));
			}
		}
	}

	// root를 찾아주는 함수
	int find(int val) {
		while (root[val] != val) {
			val = root[val];
		}
		return val;
	}

	// 합치는 함수 (우선순위가 높은 그룹을 root로)
	void union(int a, int b) {
		a = find(a);
		b = find(b);
		if (g_list.get(a).compareTo(g_list.get(b)) < 0) {
			root[b] = a;
		} else {
			root[a] = b;
		}
	}

	// 그룹 flood-fill
	Group BFS_group(int x, int y, int g_id) {
		Queue<Node> Q = new ArrayDeque<>();
		Group ret = new Group();
		ret.x = x;
		ret.y = y;
		ret.dir = d[x][y];

		Q.add(new Node(x, y));
		g[x][y] = g_id;

		while (!Q.isEmpty()) {
			Node cur = Q.poll();
			ret.volume += h[cur.x][cur.y];
			ret.area++;
			for (int[] dir_xy : dir) {
				int dx = (cur.x + dir_xy[0] + N) % N;
				int dy = (cur.y + dir_xy[1] + N) % N;
				if (h[dx][dy] == 0 || g[dx][dy] >= 0)
					continue;
				Q.add(new Node(dx, dy));
				g[dx][dy] = g_id;
			}
		}

		return ret;
	}

	// 녹는지 확인하는 함수
	boolean isMelt(int x, int y) {
		if (h[x][y] == 0)
			return false;
		for (int[] dir_xy : dir) {
			int dx = (x + dir_xy[0] + N) % N;
			int dy = (y + dir_xy[1] + N) % N;
			if (h[dx][dy] == 0)
				return true;
		}
		return false;
	}

	// (x,y) flood-fill 하면서 방향 설정
	void BFS_dir(int x, int y, int dv) {
		Queue<Node> Q = new ArrayDeque<>();

		Q.add(new Node(x, y));
		d[x][y] = dv;

		while (!Q.isEmpty()) {
			Node cur = Q.poll();
			for (int[] dir_xy : dir) {
				int dx = (cur.x + dir_xy[0] + N) % N;
				int dy = (cur.y + dir_xy[1] + N) % N;
				if (h[dx][dy] == 0 || d[dx][dy] >= 0)
					continue;
				Q.add(new Node(dx, dy));
				d[dx][dy] = dv;
			}
		}
	}
}

class Group implements Comparable<Group> {
	int volume, area, x, y, dir;

	Group() {
		this.volume = 0;
		this.area = 0;
		this.x = 0;
		this.y = 0;
		this.dir = 0;
	}

	Group(int volume, int area, int x, int y, int dir) {
		this.volume = volume;
		this.area = area;
		this.x = x;
		this.y = y;
		this.dir = dir;
	}

	@Override
	public int compareTo(Group o) {
		if (volume == o.volume) {
			if (area == o.area) {
				if (x == o.x) {
					return Integer.compare(y, o.y);
				}

				return Integer.compare(x, o.x);
			}

			return Integer.compare(area, o.area);
		}

		return Integer.compare(o.volume, volume);
	}

}

class Node {
	int x, y;

	Node(int x, int y) {
		this.x = x;
		this.y = y;
	}
}
