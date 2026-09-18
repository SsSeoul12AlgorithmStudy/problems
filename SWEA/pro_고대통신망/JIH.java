package swea_pro_고대통신망;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;

class UserSolution {
	
	/*
	 26-9-17 10:07 ~ 11:10
	 
	 최단경로 상에서 가장 길이가 긴 도로의 길이를 최소화해야한다
	 
	 ===
	 
	 이 문제에서 calc 함수를 어떻게 구현할지가 중요해보임
	 
	 calc는 어떻게 구현?
	 - 이 상황에서 최단경로를 구하는 방법은 다익스트라
	 - 근데 내가 알고 싶은 것은 가장 긴 도로의 길이의 최솟값
	 	- 그냥 업데이트 메시지에 가장 긴 도로 길이를 전달하면 될 것 같다
 	 - 다익스트라를 이용하면 시간복잡도는?
 	 	- E log E 정도
 	 - 나머지는 시간복잡도가 1 또는 E 정도
 	 - calc 호출 때마다 다익스트라를 돌릴 때 최대 연산량
 	 	- 최대 5천번 호출
 	 	- E < 4만
 	 	- 시간초과 발생 가능성이 조냊
 	 - 더 최적화할 방법이 있나?
 	 	- 매번 다익스트라를 돌릴 필요 없이, 새로 추가된 엣지들을 모아서 새로운 메시지를 추기해서 업데이트를 해야할 것 같다
 	 		- 근데 이렇게 업데이트를 딜레이시켜도 되나?
 	 	- 근데 삭제에서는 업데이트 방법이 마땅히 없는 것 같다
 	 		- 삭제가 발생했다면, calc 시에 전체 다익스트라를 다시 수행해야 한다
 	 
	 */
	
	/*
	 N개의 도시, 수도, K개의 도로 정보를 입력받는 함수
	 - 도로는 단방향
	 - 도시 번호는 0부터 시작
	 
	 ===
	 제약
	 - 제한 시간 3초 -> 테케당 1천만 연산량
	 
	 - N = 도시의 수 <= 5000
	 - K = 도로의 수 <= 1만
	 
	 
	 */
	static class Road{
		int start;
		Node edge;
		
		public Road(int start, Node edge) {
			super();
			this.start = start;
			this.edge = edge;
		}
	}
	
	static class Node{
		int vertex;
		int dist;
		int longestRoad;
		
		public Node(int vertex, int dist, int longestRoad) {
			super();
			this.vertex = vertex;
			this.dist = dist;
			this.longestRoad = longestRoad;
		}

		@Override
		public String toString() {
			return "Node [vertex=" + vertex + ", dist=" + dist + ", longestRoad=" + longestRoad + "]";
		}
	}
	
	static List<List<Node>> graph;
	static Map<Integer, Road> roads;
	static int[] dist, minLongestRoad;
//	static int[] maxLongestRoad;
	static PriorityQueue<Node> pq;
	static int n, start, removedRoads;
	
	public void init(int N, int mCapital, int K, int mId[], int sCity[], int eCity[], int mDistance[]) {
		n = N;
		
		removedRoads = 0;
		
		// 그래프 초기화
		graph = new ArrayList<>();
		roads = new HashMap<>();
		
		for(int i = 0; i < N; i++) {
			graph.add(new ArrayList<>());
		}
		
		for(int i = 0; i < K; i++) {
			int s = sCity[i];
			int e = eCity[i];
			int dist = mDistance[i];
			int id = mId[i];
			
			Node edge = new Node(e, dist, 0);
			Road road = new Road(s, edge);
			
			graph.get(s).add(edge);
			roads.put(id, road);
		}
		
		// 다익스트라로 초기의 최단경로와 가장 긴 도로의 길이 중 최솟값을 저장
		start = mCapital;
		
		dist = new int[N];	
		minLongestRoad = new int[N];
//		maxLongestRoad = new int[N];
		pq = new PriorityQueue<>((a, b) -> a.dist - b.dist);
		
		Arrays.fill(dist, Integer.MAX_VALUE);
		Arrays.fill(minLongestRoad, Integer.MAX_VALUE);
		
		pq.offer(new Node(start, 0, 0)); 
		dist[start] = 0;
		
		System.out.printf("init() : pq = %s\n", pq);
		
		while(!pq.isEmpty()) {
			Node cur = pq.poll();
			
			if(dist[cur.vertex] < cur.dist) continue;
			
			for(Node next : graph.get(cur.vertex)) {
				int nextDist = cur.dist + next.dist;
				int nextLongestRoad = Math.max(cur.longestRoad, next.dist);
				
				if(nextDist < dist[next.vertex] || nextDist == dist[next.vertex] && minLongestRoad[next.vertex] > nextLongestRoad) {
					dist[next.vertex] = nextDist;
					minLongestRoad[next.vertex] = nextLongestRoad;
//					maxLongestRoad[next.vertex] = Math.max(maxLongestRoad[next.vertex], nextLongestRoad);
					pq.offer(new Node(next.vertex, nextDist, nextLongestRoad));
				}
			}
		}
		
//		System.out.printf("init() : dist = %s\n", Arrays.toString(dist));
//		System.out.printf("init() : minLongestRoad = %s\n", Arrays.toString(minLongestRoad));
//		System.out.printf("init() : maxLongestRoad = %s\n", Arrays.toString(maxLongestRoad));
	}
	
	/*
	도로를 추가하는 함수
	
	===
	제약
	- 호출 수 <= 1.4만
	*/
	public void add(int mId, int sCity, int eCity, int mDistance) {
		// 새로운 엣지를 누적해서 저장
		Node edge = new Node(eCity, mDistance, 0);
		Road road = new Road(sCity, edge);
		
		graph.get(sCity).add(edge);
		roads.put(mId, road);
		
//		int longestRoad = (minLongestRoad[sCity] == Integer.MAX_VALUE) ? 0 : minLongestRoad[sCity];
//		if(dist[sCity] != Integer.MAX_VALUE)
//			pq.offer(new Node(sCity, dist[sCity], longestRoad)); // WARN : 현재는 도달 불가능한 것은 pq에 넣지 않아야 한다
		
		if(dist[sCity] != Integer.MAX_VALUE) {
			int nextDist = dist[sCity] + mDistance;
			int nextRoad = mDistance;
			if(minLongestRoad[sCity] != Integer.MAX_VALUE && minLongestRoad[sCity] > nextRoad) {
				nextRoad = minLongestRoad[sCity];
			}
			
			if(dist[eCity] > nextDist || dist[eCity] == nextDist && minLongestRoad[eCity] > nextRoad) {
				pq.offer(new Node(eCity, nextDist, nextRoad));
				dist[eCity] = nextDist;
				minLongestRoad[eCity] = nextRoad;
			}
			
		}
			
	}

	/*
	도로를 제거하는 함수
	
	===
	제약
	- 호출 수 <= 1천
	 */
	public void remove(int mId) {
		// 삭제할 엣지를 누적해서 저장
		Road road = roads.get(mId);
		
		List<Node> nexts = graph.get(road.start);
		for(int i = 0; i < nexts.size(); i++) {
			if(nexts.get(i) == road.edge) {
				nexts.remove(i);
				break;
			}
		}
		
		removedRoads++;
		
		return;
	}

	/*
	최단 경로 중에서 거리가 가장 긴 도로의 최소 길이를 반환하는 함수
	- 최단경로가 없으면 -1 리턴
	
	===
	제약
	- 호출 <= 5천
	
	*/
	public int calculate(int mCity) {
		// 삭제할 엣지가 존재하면 전체에 대해서 다익스트라를 수행
		if(removedRoads > 0) {
			removedRoads = 0;
			
			dist = new int[n];	
			minLongestRoad = new int[n];
//			maxLongestRoad = new int[n];
			pq = new PriorityQueue<>((a, b) -> a.dist - b.dist);
			
			Arrays.fill(dist, Integer.MAX_VALUE);
			Arrays.fill(minLongestRoad, Integer.MAX_VALUE);
			
			pq.offer(new Node(start, 0, 0)); 
			dist[start] = 0;
			
			while(!pq.isEmpty()) {
				Node cur = pq.poll();
				
				if(dist[cur.vertex] < cur.dist) continue;
				
				for(Node next : graph.get(cur.vertex)) {
					int nextDist = cur.dist + next.dist;
					int nextLongestRoad = Math.max(cur.longestRoad, next.dist);
					
					if(nextDist < dist[next.vertex] || nextDist == dist[next.vertex] && minLongestRoad[next.vertex] > nextLongestRoad) {
						dist[next.vertex] = nextDist;
						
						// WARN : v 정점에 도착했을 떼, 가장 긴 도로의 최솟값만 보존하면 된다.
						minLongestRoad[next.vertex] = nextLongestRoad;
//						maxLongestRoad[next.vertex] = Math.max(maxLongestRoad[next.vertex], nextLongestRoad);
						pq.offer(new Node(next.vertex, nextDist, nextLongestRoad));
					}
				}
			}
			
//			System.out.printf("calc() : dist = %s\n", Arrays.toString(dist));
//			System.out.printf("calc() : minLongestRoad = %s\n", Arrays.toString(minLongestRoad));
//			System.out.printf("calc() : maxLongestRoad = %s\n", Arrays.toString(maxLongestRoad));
			
			if(dist[mCity] == Integer.MAX_VALUE) {
				return -1;
			}
			
			return minLongestRoad[mCity];
		}
		
		// 새로 추가할 엣지가 존재하면 일부에 대해서만 다익스트라 수행
		while(!pq.isEmpty()) {
			Node cur = pq.poll();
			
			if(dist[cur.vertex] < cur.dist) continue;
			
			for(Node next : graph.get(cur.vertex)) {
				int nextDist = cur.dist + next.dist;
				int nextLongestRoad = Math.max(cur.longestRoad, next.dist);
				
				if(nextDist < dist[next.vertex] || nextDist == dist[next.vertex] && minLongestRoad[next.vertex] > nextLongestRoad) {
					dist[next.vertex] = nextDist;
					minLongestRoad[next.vertex] = nextLongestRoad;
//					maxLongestRoad[next.vertex] = Math.max(maxLongestRoad[next.vertex], nextLongestRoad);
					pq.offer(new Node(next.vertex, nextDist, nextLongestRoad));
				}
			}
		}
		
//		System.out.printf("calc() : dist = %s\n", Arrays.toString(dist));
//		System.out.printf("calc() : minLongestRoad = %s\n", Arrays.toString(minLongestRoad));
//		System.out.printf("calc() : maxLongestRoad = %s\n", Arrays.toString(maxLongestRoad));
		
		if(dist[mCity] == Integer.MAX_VALUE) {
			return -1;
		}
		
		return minLongestRoad[mCity];
	}
}