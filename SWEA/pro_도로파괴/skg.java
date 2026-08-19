import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.HashMap;
import java.util.Map;
import java.util.PriorityQueue;

/**
 * 실수들
 * 1. Map -> EntrySet 쓰는 법
 * 2. 다익스트라가 아니라 프림을 써버림
 * 3. ArrayList의 addFirst는 JDK21에서부터 추가됨
 */

/**
 * 시간초과 발생 해결
 * 1. s,e가 주어졌을 때 id를 반환하는 배열 생성 -> 여전히 시간초과
 * 2. 연결 정보를 ArrayList 이차원 배열로 대체 -> 해결
 */

/**
 * id_map에 사용되는 클래스
 */
class Edge {
	int sCity, eCity;

	Edge(int sCity, int eCity) {
		this.sCity = sCity;
		this.eCity = eCity;
	}
}

/**
 * PriorityQueue에 사용되는 클래스
 */
class DstCost {
	int pre, dst, cost;

	DstCost(int pre, int dst, int cost) {
		this.pre = pre;
		this.dst = dst;
		this.cost = cost;
	}
}

/**
 * 커스텀 함수인 dijkstra()의 반환값 형식
 */
class dijkstraDto {
	Deque<Integer> path;
	int cost;

	dijkstraDto(Deque<Integer> path, int cost) {
		this.path = path;
		this.cost = cost;
	}
}

class UserSolution {
	final int INF = 1000001;
	int N, K;
	// id 별로 (시작 노드, 끝 노드) 기억
	Map<Integer, Edge> id_map = new HashMap<Integer, Edge>();
	// (시작 노드, 끝 노드) -> id
	int[][] id_arr;
	// (시작 노드, 끝 노드) -> 간선 비용
	int[][] edge;
	// 노드 연결 여부만 갖고 있는 ArrayList 배열 
	ArrayList<ArrayList<Integer>> con;

	public void init(int N, int K, int mId[], int sCity[], int eCity[], int mTime[]) {
		this.N = N;
		this.K = K;
		edge = new int[N][N];
		id_arr = new int[N][N];
		con = new ArrayList<>();
		for (int i = 0; i < N; i++) {
			con.add(new ArrayList<>());
		}

		for (int i = 0; i < K; i++) {
			int id = mId[i];
			int s = sCity[i];
			int e = eCity[i];
			int t = mTime[i];
			id_map.put(id, new Edge(s, e));
			id_arr[s][e] = id;
			edge[s][e] = t;
			con.get(s).add(e);
		}

		return;
	}

	public void add(int mId, int sCity, int eCity, int mTime) {
		id_map.put(mId, new Edge(sCity, eCity));
		id_arr[sCity][eCity] = mId;
		edge[sCity][eCity] = mTime;
		con.get(sCity).add(eCity);

		return;
	}

	public void remove(int mId) {
		Edge eg = id_map.get(mId);
		edge[eg.sCity][eg.eCity] = 0;
		id_map.remove(mId);
		id_arr[eg.sCity][eg.eCity] = 0;
		con.get(eg.sCity).remove(Integer.valueOf(eg.sCity));

		return;
	}

	// 최단 경로에서 지나치는 각 다리에 대해서 remove -> 최단 경로 계산 -> add로 복원 반복
	public int calculate(int sCity, int eCity) {
		dijkstraDto result = dijkstra(sCity, eCity);
		int minCost = result.cost;
		int maxCost = 0;
		while (result.path.size() > 1) {
			int s = result.path.pollLast();
			int e = result.path.peekLast();
			int t = edge[s][e];
			// 그 도로에 해당하는 id 찾기
			int id = id_arr[s][e];

			remove(id);
			dijkstraDto new_res = dijkstra(sCity, eCity);
			maxCost = Math.max(maxCost, new_res.cost == 0 ? INF : new_res.cost);
			add(id, s, e, t);
		}

		if (maxCost == 0 || maxCost == INF) {
			return -1;
		}
		return maxCost - minCost;
	}

	// custom 함수 : 두 도시 사이 최단 경로를 반환하는 함수 (다익스트라? 사용)
	public dijkstraDto dijkstra(int sCity, int eCity) {
		Deque<Integer> path = new ArrayDeque<>();
		PriorityQueue<DstCost> PQ = new PriorityQueue<>((a, b) -> Integer.compare(a.cost, b.cost));
		boolean[] vis = new boolean[N];
		// 이전 노드 기록
		int[] pre = new int[N];

		// 시작점으로부터 갈 수 있는 경로 PQ에 넣기
		PQ.add(new DstCost(-1, sCity, 0));
		while (!PQ.isEmpty()) {
			DstCost cur = PQ.poll();
			int nxt = cur.dst;
			if (vis[nxt])
				continue;
			vis[nxt] = true;
			pre[nxt] = cur.pre;

			if (nxt == eCity) {
				int p = nxt;
				while (p != -1) {
					path.add(p);
					p = pre[p];
				}

				return new dijkstraDto(path, cur.cost);
			}

			// 아래 반복문을 기존에는 j = 0 ~ N까지 모두 살펴보아서 시간초과 발생
			for (int j : con.get(nxt)) {
				if (edge[nxt][j] == 0 || vis[j])
					continue;
				PQ.add(new DstCost(nxt, j, cur.cost + edge[nxt][j]));
			}
		}

		return new dijkstraDto(path, 0);
	}
}

