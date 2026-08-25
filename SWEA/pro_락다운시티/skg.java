import java.util.*;

/*
 * 3차원 vis 배열을 쓰는 BFS?
 * 사용횟수를 vis 배열에 기록하고 Q에도 특정 위치에 어떤 보안등급으로 왔는지 기록
 * vis[x][y][보안등급]
 * 보안등급 -> 숫자 변환? 'A': 1 ~ 'Z': 26, 'AA' : 27 = 26* 1 + 1 ~ 'ZZZ' : 26 * 26 * 26 + 26 * 26 + 26이 최대.
 * 'AZ' : 26 * 1 + 26, 'BA' : 26 * 2 + 1 = 53
 */

/*
 * 익환쌤: 이분 탐색을 통해 보안등급을 찾는다!
 * 각 보안등급이 가능한지 여부는 매번 BFS를 돌아야 되긴 한다.
 */

/*
 * 사전순서가 빠를수록 더 높은 보안 등급 -> 'A'가 제일 높은 보안등급이고 (상한선) 'ZZZ'가 가장 낮은 보안 등급 (하한선)
 * 근데 출발 위치 기준 상하좌우 중 가장 낮은 보안등급이 하한선이 된다.
 * left: 'A', right: 상하좌우 중 가장 낮은 보안등급
 * 이분 탐색 -> mid BFS 후 도달 가능하면 mid = left
 * 			 	 	    	불가능하면 mid = right
 */

/*
 * 알파벳을 26진법으로 10진수로 변환한 것이 사전순일까? => 아니다..
 * A - AA - AAA - AAB - AAC - ... - AAZ - AB - ABA
 * 1	2	3	4	5
 * 아니면 칸이 최대 10000개밖에 안되니까 각 칸에 있는 모든 문자열에 대해서 Set에 집어넣고 정렬?
 * 정렬하는 데 시간이 더 오래 걸릴 것 같은데..
 */

/*
 * 실수들 정리
 * 1. 위와 같은 board에 나오는 애들만 정렬 방식은 -> board가 변경될 때마다 재정렬해줘야 하므로 부적절
 * => 가능한 모든 문자열에 대해서 미리 keys에 저장해놓고 이분탐색
 * 2. 가능한 모든 문자열 DFS 시에 DFS 자체가 사전순으로 진행되므로 따로 정렬하지 않고 바로바로 넣어주면 된다.
 * => 또한 keys에 추가되고 DFS를 돌기 때문에 종료 조건을 길이 == M 일 때로 정해야 한다.
 */

class UserSolution {
	int N, M;
	String[][] board;
	List<String> keys;
	PriorityQueue<String> PQ;
	void init(int N, int M, String mGrade[][]) {
		Set<String> keySet = new HashSet<>();
		this.N = N;
		this.M = M;
		this.board = mGrade;
		for (int i = 0; i < N; i++) {
			for (int j = 0; j < N; j++) {
				keySet.add(board[i][j]);
			}
		}
		keys = new ArrayList<>();
		DFS("");

	}
	
	// 최대 M개 문자열 List에 넣기.
	void DFS(String s) {
		if (s.length() == M)	return;
		for (int i = 0; i < 26; i++) {
			String next = s.concat(String.valueOf((char)('A' + i)));
			keys.add(next);
			DFS(next);
		}
	}

	void change(int mRow, int mCol, int mDir, int mLength, String mGrade) {
		for (int i = 0; i < mLength; i++) {
			board[mRow + (mDir == 0 ? i : 0)][mCol + (mDir == 1 ? i : 0)] = mGrade;
		}
	}

	int L, sRow, sCol, eRow, eCol;
	String calculate(int L, int sRow, int sCol, int eRow, int eCol) {
		this.L = L;
		this.sRow = sRow;
		this.sCol = sCol;
		this.eRow = eRow;
		this.eCol = eCol;
		// 불가능한 최초의 지점을 찾는 이분탐색
		int left = 0;
		int right = keys.size();
		int mid;
		while (left != right) {
			mid = (left + right) / 2;
			if (BFS(mid)) {
				left = mid + 1;
			} else {
				right = mid;
			}
		}
//		System.out.println("calc: " + keys.get(left - 1));
		return keys.get(left - 1);
	}
	
	final int[][] dir = {{0,1}, {0,-1}, {1,0}, {-1,0}};
	Boolean BFS(int idx) {
		String key = keys.get(idx);
		
		Queue<Node> Q = new ArrayDeque<>();
		int[][] vis = new int[N][N];
		Q.add(new Node(sRow, sCol));
		vis[sRow][sCol] = 1;
		
		while (!Q.isEmpty()) {
			Node cur = Q.poll();
			if (cur.x == eRow && cur.y == eCol) {
				return true;
			}
			
			for (int[] d : dir) {
				int dx = d[0] + cur.x;
				int dy = d[1] + cur.y;
				if (dx < 0 || dy < 0 || dx >= N || dy >= N)	continue;
				if (vis[dx][dy] > 0)	continue;
				if (vis[cur.x][cur.y] + 1 > L + 1)	continue;
				// 다음 진행할 칸이 key보다 사전순으로 빠르면 진행 불가능.
				if (board[dx][dy].compareTo(key) < 0)	continue;
				Q.add(new Node(dx, dy));
				vis[dx][dy] = vis[cur.x][cur.y] + 1; 
			}
		}
		
		return false;
	}
}

class Node {
	int x, y;
	Node(int x, int y) {
		this.x = x;
		this.y = y;
	}
}

