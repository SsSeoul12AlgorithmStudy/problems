import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.PriorityQueue;

class UserSolution {

	int capital, N;
	HashMap<Integer, Route> routes; // 도로 ID : (출발도시, 도착도시, 거리길이)
	ArrayList<Integer>[] graph; // 도시[i]에 이동가능한 도로 ID 리스트
	int dist[], maxEdge[]; // 최단거리, 가장 긴 도로길이 중 최소

	public void init(int N, int mCapital, int K, int mId[], int sCity[], int eCity[], int mDistance[]) {

		this.N = N;
		capital = mCapital;
		routes = new HashMap<>();
		graph = new ArrayList[N];
		for (int i = 0; i < N; i++) {
			graph[i] = new ArrayList<>();
		}

		// K개의 도로
		for (int k = 0; k < K; k++) {
			routes.put(mId[k], new Route(sCity[k], eCity[k], mDistance[k]));
			graph[sCity[k]].add(mId[k]);
		}

		return;
	}

	public void add(int mId, int sCity, int eCity, int mDistance) {
		routes.put(mId, new Route(sCity, eCity, mDistance));
		graph[sCity].add(mId);
		return;
	}

	public void remove(int mId) {
		Route rmRouteId = routes.get(mId);
		graph[rmRouteId.s].remove(Integer.valueOf(mId));
		routes.remove(mId);
		return;
	}

	public int calculate(int mCity) {

		// capital -> 목적지 mCity로 가는 최단경로 구하기
		return dijkstra(mCity);
	}

	private int dijkstra(int mCity) {

		dist = new int[N];
		maxEdge = new int[N];

		Arrays.fill(dist, Integer.MAX_VALUE);
		Arrays.fill(maxEdge, Integer.MAX_VALUE);

		// 총거리 우선, 총거리가 같으면 가장 긴 도로 길이 우선
		PriorityQueue<State> pq = new PriorityQueue<>();

		dist[capital] = 0;
		maxEdge[capital] = 0;
		pq.offer(new State(capital, 0, 0));

		while (!pq.isEmpty()) {
			State now = pq.poll();

			int nowCity = now.city;
			int nowDist = now.dist;
			int nowMaxEdge = now.maxEdge;

			// 이미 더 좋은 정보로 갱신된 이전 후보는 무시
			if (nowDist != dist[nowCity] || nowMaxEdge != maxEdge[nowCity]) {
				continue;
			}

			// 최단경로의 도로 중 최장도로길이
			if (nowCity == mCity) {
				return nowMaxEdge;
			}

			for (int roadId : graph[nowCity]) {
				// 다음 도로의 정보
				Route next = routes.get(roadId);

				int nextDist = nowDist + next.w; // (누적 도로 + 다음 도로) 길이
				int nextMaxEdge = Math.max(nowMaxEdge, next.w);

				// 최단경로인가 || 최단경로와 같은 경우 가장 긴 도로길이의 값보다 작은가
				if (nextDist < dist[next.e] || (nextDist == dist[next.e] && nextMaxEdge < maxEdge[next.e])) {

					dist[next.e] = nextDist;
					maxEdge[next.e] = nextMaxEdge;

					pq.offer(new State(next.e, nextDist, nextMaxEdge));
				}
			}
		}

		return -1;
	}
}

class Route implements Comparable<Route> {

	// 출발지, 도착지, 거리
	int s, e, w;

	public Route(int s, int e, int w) {
		this.s = s;
		this.e = e;
		this.w = w;
	}

	@Override
	public int compareTo(Route other) {
		return Integer.compare(this.w, other.w);
	}
}

class State implements Comparable<State> {

	int city, dist, maxEdge;

	public State(int city, int dist, int maxEdge) {
		this.city = city;
		this.dist = dist;
		this.maxEdge = maxEdge;
	}

	@Override
	public int compareTo(State other) {
		// 1순위 누적 도로길이가 최단거리
		int dist = Integer.compare(this.dist, other.dist);
		if (dist != 0)
			return dist;

		// 2순위 최단경로 도로 중 최장도로가 최소
		return Integer.compare(this.maxEdge, other.maxEdge);
	}
}
