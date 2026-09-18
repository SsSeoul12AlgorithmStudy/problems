import java.util.*;

/*
 * 시작점이 고정되어 있다.
 * 그러면 각 노드별로 시뮬레이션을 미리 진행하면서 미리 값을 구하기?
 * 우선순위 큐를 사용한 BFS?
 * 
 * 근데 이러면 도로가 추가되거나 삭제될 때마다 다시 시뮬레이션을 해야 될 것 같기도..
 * 
 * 도로 정보는 ArrayList를 사용해서 저장하는 게 나을 것 같다.
 * 근데 또 도로 삭제가 id로 판별이 됨.. 아오;;;
 * 도시의 개수가 최대 5000으로 적지도 않음. -> 이차원 배열을 쓰기에는 좀..
 * 
 * 근데 그렇다고 ArrayList 배열을 사용하기에는 Mapping 정보를 저장하기 애매하다.
 * Map<도로id, 출발도시> 이렇게 저장해야 되나? 그래서 출발 도시 List에서 직접 찾기?
 * 
 * 영향을 받는 애들만 다시 계산하기?
 * 그게 되나?
 * 
 * calculate : 인접 도로들을 PriorityQueue에 넣으면서 진행.
 * 가장 비용이 작은 도로들을 계속 따라감.
 * 
 * 민철이 형의 아이디어 : MST?
 * 확실히 위 알고리즘을 MST에 적용하면 모든 노드에 대해서 답을 얻을 수 있다.
 * 
 * 이동할 수 있는 경로가 없으면 -1 반환..
 * 
 * 1번만 맞는데 왜 틀렸는지 모르겠음..
 * 아 최단 경로로 가면서 가는 도중에 거치는 가장 긴 도로의 길이가 최소가 되도록...
 * 
 * 다익스트라(+ 현재까지의 최대 도로 길이)로 했는데 마지막 하나에서 시간초과..
 * 
 * remove 함수가 1000 이하밖에 안됨 -> remove 시에만 재계산하면 시간초과 안 날 듯..!
 * remove 시에만 재계산해도 시간초과가 뜬다...
 * 흠... remove 시에 MST가 파괴되는 부분부터만 다시 dijkstra 진행?
 * 
 * tree 구조를 만들고, 각 노드별로 List 관리 {도로 id, 목표 노드}
 * tree 구조에서 BFS/DFS 돌면서 도로를 찾고, 그 도로의 시작 도시, 그 도시부터 BFS/DFS 하면서 그 도시 포함 서브 트리에 포함된 도시들의 vis 배열을 MX로 초기화시키고,
 * 
 * <remove>
 * dijkstra (new Node(해당 도로의 startCity, vis[startCity][0], vis[startCity][1]))
 * 
 * 
 * 
 */

class Road {
	int id, endCity, dist;

	Road(int id, int endCity, int dist) {
		this.id = id;
		this.endCity = endCity;
		this.dist = dist;
	}
}

class Node {
	int city, sumD, maxD;

	Node(int city, int sumdist, int maxdist) {
		this.city = city;
		this.sumD = sumdist;
		this.maxD = maxdist;
	}
}

class UserSolution {
	List<Road>[] edges;
	Map<Integer, Integer> map;
	int N, capital;
	int[][] vis;
	final int MX = 100 * 10000;
	PriorityQueue<Node> PQ;
	
	boolean dirty = false;
	
	public void init(int N, int mCapital, int K, int mId[], int sCity[], int eCity[], int mDistance[]) {
		this.N = N;
		this.capital = mCapital;
		edges = new List[N];
		map = new HashMap<>();
		vis = new int[N][2];
		PQ = new PriorityQueue<>((a, b) -> {
			if (a.sumD == b.sumD) {
				return Integer.compare(a.maxD, b.maxD);
			}
			return Integer.compare(a.sumD, b.sumD);
		});
		
		for (int i = 0; i < N; i++) {
			edges[i] = new ArrayList<>();
		}

		for (int i = 0; i < K; i++) {
			edges[sCity[i]].add(new Road(mId[i], eCity[i], mDistance[i]));
			map.put(mId[i], sCity[i]);
		}
		for (int i = 0; i < N; i++) {
			vis[i] = new int[] {MX, MX};
		}
		dijkstra(new Node(capital, 0, 0));

		return;
	}

	public void add(int mId, int sCity, int eCity, int mDistance) {
		edges[sCity].add(new Road(mId, eCity, mDistance));
		map.put(mId, sCity);
		dijkstra(new Node(eCity, vis[sCity][0] + mDistance, Math.max(vis[sCity][1], mDistance)));

		return;
	}

	public void remove(int mId) {

		for (Road r : edges[map.get(mId)]) {
			if (r.id == mId) {
				edges[map.get(mId)].remove(r);
				break;
			}
		}

		map.remove(mId);
		dirty = true;
		
		return;
	}
	
	public int calculate(int mCity) {
		if (dirty) {
			for (int i = 0; i < N; i++) {
				vis[i] = new int[] {MX, MX};
			}
			dijkstra(new Node(capital, 0, 0));
			dirty = false;
		}
//		System.out.println(vis[mCity][0] + "/" + vis[mCity][1]);
		return vis[mCity][1] == MX ? -1 : vis[mCity][1];
	}

	public void dijkstra(Node node) {
		PQ.add(node);		

		while (!PQ.isEmpty()) {
			// 지금까지 도달 가능한 곳 중 가장 비용이 적은 도로 뽑기.
			Node cur = PQ.poll();
			int cur_city = cur.city;
			if (vis[cur_city][0] < cur.sumD)	continue;
			if (vis[cur_city][0] == cur.sumD && vis[cur_city][1] < cur.maxD)	continue;
			// 아니라면 방문 처리 하기.
			vis[cur_city] = new int[] {cur.sumD, cur.maxD};

			// 방문처리한 노드에서 방문 가능한 도로 탐색.
			for (Road r : edges[cur_city]) {
				int n_sumD = vis[cur_city][0] + r.dist;
				int n_maxD = Math.max(vis[cur_city][1], r.dist);
				if (vis[r.endCity][0] < n_sumD) continue;
				if (vis[r.endCity][0] == n_sumD && vis[r.endCity][1] < n_maxD)	continue;
				PQ.add(new Node(r.endCity, n_sumD, n_maxD));
			}
		}
	}
}
