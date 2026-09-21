import java.util.*;

/*
 * union-find로 구현하면 될 것 같은데..
 * N이 최대 45000 
 * union-find 시에는 루트에 가장 작은 id를 가진 블록이 오도록
 * 
 * 그룹별 크기를 Map에 저장하는 게 날까 List에 저장하는 게 나을까..
 * Map에 저장하는 게 union-find 할 때는 좋겠지만 정렬 함수에서는 List를 사용하는 게 나을 것.
 * 
 * 당연히 저번처럼 아이디는 순차 증가라고 생각했는데 랜덤이라..
 * 그러면 그룹 별로 구역 크기 정보를 가지고 있어야 겠네..
 * 15000 크기 배열 만들고 하기? -> 이건 나중에 정렬할 때 막힘
 * 
 * 루트 id를 key로, 구역 크기를 value로 가지는 Map?
 * 근데 이러면 if (더 작은 id의 블록이 합쳐졌을 때) 루트 id가 바뀌면서 Map의 원래 루트 키를 지우고,
 * Map에 바뀐 루트 id를 추가해줘야 함.
 * 
 * 뭐 이렇게 구현한다고 치고, 정렬은 어떻게 처리해야 할까
 * 
 * 생각해보니까 한 구역에 다른 블록 하나만 추가되는 경우 말고도,
 * 기존 두 개 이상의 구역이 하나의 블록 추가로 한 구역이 되어버리는 경우도 있을 수 있음.
 * 
 * board 배열에 (0,0) ~ (N-1,N-1)까지 블록 id 저장.
 * => (0,0) 값은 (0,0)오른쪽 아래 칸을 의미.
 * -> 너무 개초딩 접근인가? => N도 너무 커서 안됨.. 근데 이미 구현해버림;;
 * 
 * 정렬...정렬...정렬...쌰갈...
 * 아니면 각 구역의 루트 id만 셋으로 저장했다가 함수 불릴 때만 정렬?
 * 
 * 뭐.. 근데 시간초과 날 것 같네요 백퍼 ㅋㅋㅈㅅ;
 * 
 * 
 */

class UserSolution {

//	Map<Integer, Integer> map;
	final int MX_BLOCKS = 15000;
	
	int N;
	int[] root;
	int[] size;
	int[][] board;
	// 정렬 용도의 Set
	List<Integer> sectors;
	
	final int[][] dir = {{0,1}, {0,-1}, {1,0}, {-1,0}};
	
	void init(int N) {
//		map = new HashMap<Integer, Integer>();
		this.N = N;
		root = new int[MX_BLOCKS];
		board = new int[N][N];
		size = new int[MX_BLOCKS];
		sectors = new ArrayList<>();
		
		return;
	}

	 int addBlock(int index, int leftTopX, int leftTopY, int rightBottomX, int rightBottomY) {
		 Set<Integer> adj = new HashSet<Integer>();
		 
		 int s = (rightBottomX - leftTopX) * (rightBottomY - leftTopY);
		 
		 // 위쪽, 아래쪽 테두리
		 for (int c = leftTopY; c < rightBottomY; c++) {
			 // 위쪽
			 board[leftTopX][c] = index;
			 int nx = leftTopX - 1;
			 if (!(nx < 0 || nx >= N || board[nx][c] == 0))
				 adj.add(board[nx][c]);
			 
			 if (leftTopX == rightBottomX - 1)	continue;
			 // 아래쪽
			 board[rightBottomX - 1][c] = index;
			 nx = rightBottomX;
			 if (!(nx < 0 || nx >= N || board[nx][c] == 0))
				 adj.add(board[nx][c]);
		 }
		 
		// 왼쪽, 오른쪽 테두리
		 for (int r = leftTopX; r < rightBottomX; r++) {
			 // 왼쪽
			 board[r][leftTopY] = index;
			 int ny = leftTopY - 1;
			 if (!(ny < 0 || ny >= N || board[r][ny] == 0))
				 adj.add(board[r][ny]);
			 
			 if (leftTopY == rightBottomY - 1)	continue;
			 // 오른쪽
			 board[r][rightBottomY - 1] = index;
			 ny = rightBottomY;
			 if (!(ny < 0 || ny >= N || board[r][ny] == 0))
				 adj.add(board[r][ny]);
		 }
		 
		 size[index] = s;
		 sectors.add(index);
		 root[index] = index;
		 
		 
		 // 인접한 블록이 없으면 끝.
		 if (adj.isEmpty()) {
			 return size[index];
		 }
		 
		 Set<Integer> adj_roots = new HashSet<Integer>();
		 
		 // 인접한 블록들의 루트 블록 셋에 추가.
		for (int i : adj) {
			adj_roots.add(find(i));
		}
		
		 // union 진행.
		for (int i : adj_roots) {
			union(index, i);
		}
		 
		 return size[find(index)];
	}

	 // find 함수로 두 블록의 루트 아이디 비교
	 int isSameArea(int index1, int index2) {
		 if (find(index1) == find(index2))	return 1;
		 return 0;
	 }
	 
	 // find 함수로 알아낸 루트 아이디를 Map에서 찾아서 value 리턴
	 int queryAreaSize(int index) {
		 return size[find(index)];
	 }
	 
	 
	 Solution.Result getAreaSizeTop10() { 
		 sectors.sort((a, b) -> {
			 if (size[a] == size[b]) {
				 return Integer.compare(a, b);
			 }
			 return Integer.compare(size[b], size[a]);
		 });
		 
		 int cnt = Math.min(10, sectors.size());
		 int[] arr_index = new int[cnt];
		 int[] arr_size = new int[cnt];
		 
		 for (int i = 0; i < cnt; i++) {
			 arr_index[i] = sectors.get(i);
			 arr_size[i] = sectors.get(i);
		 }
		 
		 Solution sol = new Solution();
		 Solution.Result res = sol.new Result(cnt, arr_index, arr_size);
		 
		 return res;
	 }
	 
	 // 합치고 최종적으로 루트가 된 블럭의 id 반환.
	 void union(int index1, int index2) {
		 int r1 = find(index1);
		 int r2 = find(index2);
		 if (r1 == r2)	return;
		 if (r1 < r2) {
			 root[r2] = r1;
			 size[r1] += size[r2];
			 sectors.remove(r2);
		 } else {
			 root[r1] = r2;
			 size[r2] += size[r1];
			 sectors.remove(r1);
		 }
	 }
	 
	 int find(int index) {
		 if (root[index] == index) {
			 return index;
		 }
		 return root[index] = find(root[index]);
	 }

}
