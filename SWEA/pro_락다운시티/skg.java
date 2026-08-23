import java.util.ArrayDeque;
import java.util.Queue;

// 노력했다는 이쁜 마음 알아주세요 *^^*

/**
 * 3차원 vis 배열을 쓰는 BFS?
 * 사용횟수를 vis 배열에 기록하고 Q에도 특정 위치에 어떤 보안등급으로 왔는지 기록
 * vis[x][y][보안등급]
 * 보안등급 -> 숫자 변환? 'A': 1 ~ 'Z': 26, 'AA' : 27 = 26* 1 + 1 ~ 'ZZZ' : 26 * 26 * 26 + 26 * 26 + 26이 최대.
 * 'AZ' : 26 * 1 + 26, 'BA' : 26 * 2 + 1 = 53
 */

class Node {
	int x, y;
	String S;
	Node(int x, int y, String S) {
		this.x = x;
		this.y = y;
		this.S = S;
	}
}

class UserSolution {
	int MX_W = 26 * 26 * 26 + 26 * 26 + 26 + 1;
	final int MX_N = 100;
	final int[][] dir = {{0,1}, {0,-1}, {1,0}, {-1,0}};
	int N, M;
	int[][][] vis;
	String[][] board;
	
	void init(int N, int M, String mGrade[][]) {
		this.N = N;
		this.M = M;
		MX_W = 0;
		for (int i = 0; i < M; i++) {
			MX_W *= 26;
			MX_W += 26;
		}
		
		this.board = mGrade;
	}

	void change(int mRow, int mCol, int mDir, int mLength, String mGrade) {
		for (int i = 0; i < mLength; i++) {
			board[mRow + (mDir == 0 ? i : 0)][mCol + (mDir == 1 ? i : 0)] = mGrade;
		}
	}

	String calculate(int L, int sRow, int sCol, int eRow, int eCol) {
		vis = new int[N][N][MX_W+1];
		Queue<Node> Q = new ArrayDeque<Node>();
		// 시작점 vis Q 초기화
		Q.add(new Node(sRow, sCol, board[sRow][sCol]));
		vis[sRow][sCol][mGradeToNum(board[sRow][sCol])] = 1;
		// ret : 도착칸에 도달할 수 있는 가장 사전순서가 느린 보안등급
		String ret = "9";
		while (!Q.isEmpty()) {
			Node cur = Q.poll();
//			System.out.println("== this is " + cur.x + ", " + cur.y + ", " + cur.S);
			if (cur.x == eRow && cur.y == eCol) {
//				System.out.println(cur.S + " is arrived at " + vis[cur.x][cur.y][mGradeToNum(cur.S)]);
				ret = (cur.S.compareTo(ret) < 0 ? ret : cur.S);
				continue;
			}
			for (int[] d : dir) {
				int dx = d[0] + cur.x;
				int dy = d[1] + cur.y;
				if (dx < 0 || dy < 0 || dx >= N || dy >= N)	continue;
				// 진행하려는 칸이 사전순으로 더 높으면 그게 필요함.
				String req = (cur.S.compareTo(board[dx][dy]) > 0 ? board[dx][dy] : cur.S);
//				System.out.println("req is " + req + " and ret is " + ret + " and res is " + (vis[dx][dy][mGradeToNum(req)] > 0 ? "true" : "false"));
				if (vis[dx][dy][mGradeToNum(req)] > 0)	continue;
				// ret보다 사전순으로 더 빠르거나 같으면 탈락
				if (req.compareTo(ret) <= 0)	continue;
				// 만약 L+1 보다 커지면 탈락
				if (vis[cur.x][cur.y][mGradeToNum(cur.S)] + 1 > L + 1)	continue;
//				System.out.println("moved to " + dx + "," + dy + " as " + req);
//				System.out.println("board: " + board[dx][dy] + " and cur.S is " + cur.S + " // result is " + board[dx][dy].compareTo(cur.S));
				Q.add(new Node(dx, dy, req));
				vis[dx][dy][mGradeToNum(req)] = vis[cur.x][cur.y][mGradeToNum(cur.S)] + 1;
			}
		}
//		printBoard();
//		System.out.println("ret is " + ret);
		
		return ret;
	}
	
	int r_vis(int x, int y) {
		return vis[x][y][mGradeToNum(board[x][y])];
	}
	
	// 보안등급 -> 숫자 전환 함수
	int mGradeToNum(String mGrade) {
		char[] m = mGrade.toCharArray();
		int res = 0;
		for (int i = 0; i < m.length; i++) {
			res *= 26;
			res += m[i] - 'A' + 1;
		}
		return res;
	}
	
	void printBoard() {
		for (int i = 0; i < N; i++) {
			for (int j = 0; j < N; j++) {
				System.out.print( board[i][j] + ' ');
			}
			System.out.println();
		}
	}

}
