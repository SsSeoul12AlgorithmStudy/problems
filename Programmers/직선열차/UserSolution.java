import java.util.*;

class UserSolution {
	/*
	 * 2026-8-27 12:53 ~ 14:56
	 * 
	 * 
	 * ===
	 * 제약조건
	 * - 역간의 간격 >= 3
	 * - 열차의 수 <= 50
	 * - 열차 역의 수 <= 10만
	 * 
	 * ===
	 * 기억할 점
	 * - 열차는 일정 간격마다 정차한다
	 * - 열차는 역에서 바로 타고 양방향으로 이동 가능한 것으로 보인다
	 * 
	 * === 
	 * calculate를 어떻게 구현?
	 * - 다익스트라는 시간초과
	 * - 노선을 정점으로 보고, 노선을 최대한 적게 갈아타서 도착역을 포함하는 노선으로 갈 수 있는지 판정해야 한다
	 * 		-> 이러면 BFS로도 풀 수 있다.
	 * - 근데 두 노선이 연결되어 있음을 어떻게 아나?
	 * 	- 정차역의 규칙이 수열이다 -> 정수론 개념을 잘 이용하면 최적화 가능!
	 * 		- 최대공약수 개념 공부 필요... 
	 * */
	
	int N, K;
	List<Train> train;
	
	/*
	 * N개의 열차역을 지나는 K개의 열차의 정보를 입력받는 함수
	 * - ** 테케 시작 시에만 호출
	 * 
	 * */
	public void init(int N, int K, int mId[], int sId[], int eId[], int mInterval[]) {
		this.N = N;
		this.K = K;
		
		this.train = new ArrayList<>(K);
		for(int i = 0; i < K; i++) {
			train.add(new Train(mId[i], sId[i], eId[i], mInterval[i]));
		}
	}

	/*
	 * 새로운 열차를 추가하는 함수
	 * - 이전에 없는 열차임이 보장
	 * 
	 * - ** 최대 150회 호출
	 * */
	public void add(int mId, int sId, int eId, int mInterval) {
		train.add(new Train(mId, sId, eId, mInterval));
		K++;
	}

	/*
	 * 존재하는 열차를 제거하는 함수
	 * 
	 * - ** 최대 100회 호출
	 * */
	public void remove(int mId) {
		for(int i = 0; i < K; i++) {
			if(train.get(i).id == mId) {
				train.remove(i);
				break;
			}
		}
		K--;
	}

	/*
	 * 시작 역에서 도착 역까지 환승해야하는 최소 횟수를 리턴하는 함수
	 * - 방법이 없다면 -1을 리턴
	 * 
	 * - ** 최대 50회 호출
	 * */
	public int calculate(int sId, int eId) {
//		System.out.printf("calc : sId = %d eId = %d\n", sId, eId);
		
		// 연결된 노선 사이에 엣지를 추가
		List<List<Integer>> graph = new ArrayList<>();
		for(int i = 0; i < K; i++) {
			graph.add(new ArrayList<>());
		}
		
		for(int i = 0; i < K; i++) {
			for(int j = i + 1; j < K; j++) {
				if(isConnectedLines(i, j)) {
					graph.get(i).add(j);
					graph.get(j).add(i);
				}
			}
		}
		
		// 노선을 정점으로 bfs 수행 
		Queue<Node> qu = new LinkedList<>();
		boolean[] visited = new boolean[K];
		
		for(int start = 0; start < K; start++) {
			if(sId >= train.get(start).start && sId <= train.get(start).end && ((sId - train.get(start).start) % train.get(start).interval == 0)) {
				qu.offer(new Node(start, 0));
				visited[start] = true;
			}
		}
		
		Set<Integer> endLines = new HashSet<>();
		for(int i = 0; i < K; i++) {
			if(eId >= train.get(i).start && eId <= train.get(i).end && ((eId - train.get(i).start) % train.get(i).interval == 0)) {
				endLines.add(i);
			}
		}
		
		System.out.printf("calc : qu = %s\n", qu);
		System.out.printf("calc : end = %s\n", endLines);

		while(!qu.isEmpty()) {
			Node cur = qu.poll();
			
			if(endLines.contains(cur.vertex)) {
				System.out.printf("calc : ret = %d\n", cur.vertex);
				return cur.dist;
			}
			
			for(int next : graph.get(cur.vertex)) {
				if(visited[next]) continue;
				qu.offer(new Node( next, cur.dist + 1 ));
				visited[next] = true;
			}
		}
		
		System.out.printf("calc : ret = -1\n");
		return -1;
	}
	
	boolean isConnectedLines(int a, int b) {
//		System.out.printf("isConnnected : %d %d\n", a, b);
//		System.out.printf("isConnnected : %s %s\n", train.get(a), train.get(b));
		// 연결되어 있는지
		if((train.get(a).start - train.get(b).start) % gcd(train.get(a).interval, train.get(b).interval) != 0) {
			return false;
		}
		
		// 공통 정차역이 구간 내에 존재하는지
		int L = Math.max(train.get(a).start, train.get(b).start);
		int R = Math.min(train.get(a).end, train.get(b).end);
		
		int diff = L - train.get(a).start;
		int k = (diff + train.get(a).interval - 1) / train.get(a).interval;
		int aInStart = train.get(a).start + train.get(a).interval * k;
		
		for(int station = aInStart; station <= R; station += train.get(a).interval) {
			if((station - train.get(b).start) % train.get(b).interval == 0) {
				return true;
			}
		}
		
		return false;
	}
	
	int gcd(int a, int b) {
		if(b == 0) {
			return -1;
		}
		
		while(b > 0) {
			int temp = a % b;
			a = b;
			b = temp;
		}
		
		return a;
	}
	
	static class Node{
		int vertex;
		int dist;
		public Node(int vertex, int dist) {
			super();
			this.vertex = vertex;
			this.dist = dist;
		}
		@Override
		public String toString() {
			return "Node [vertex=" + vertex + ", dist=" + dist + "]";
		}
		
		
	}
	
	static class Train{
		int id;
		int start;
		int end;
		int interval;
		
		public Train(int id, int start, int end, int interval) {
			super();
			this.id = id;
			this.start = start;
			this.end = end;
			this.interval = interval;
		}

		@Override
		public String toString() {
			return "Train [id=" + id + ", start=" + start + ", end=" + end + ", interval=" + interval + "]";
		}	
	}
}