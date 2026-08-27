import java.util.*;

/*
 * BFS로 풀 수 있는건가?
 * BFS로 풀려면 환승 횟수가 기준이므로 vis 배열값을 환승한 횟수로 둬야할듯.
 * 아니면 vis 배열을 각 열차별로 따로 두는 게 나을지도? -> 환승 횟수로 두는 건 유지해야 함.
 * Q에 추가할 때 현재 열차의 모든 역을 Q에 추가.
 * Q에서 peek를 살펴볼 때 그 역에서 환승할 수 있고 아직 방문하지 않은 열차이면 방문
 *
 * 그전에 일단 시간 복잡도 괜찮으려나?
 * 
 * 자료구조는 어떻게 짜지?
 * 열차 역 별로 해당 역에서 내릴 수 있는 train id를 List 형태로 저장 => Q에서 peek를 살펴볼 때 필요함.
 * train id 별로 시작 역/종착 역/정차 역 간격 저장 => Q에 추가할 때 train id만 있으면 모든 역을 Q에 추가할 수 있음.
 * 
 * 특정 열차에서 도달 가능한 모든 역을 큐에 추가하니까 너무 오래 걸린다.
 * 큐에는 열차 아이디만 넣고 환승 가능한 열차들 간에 엣지를 추가한다..?
 * 
 * 근데 엣지를 어떻게 추가하지? => 이게 좀 어렵네.. => 환승 가능하면 edge 연결되어있음.
 * 
 * 각 역마다 도달 가능한 열차를 기록하고 이걸로 엣지 연결?
 * 추가되는 열차는 사실상 init(50) + add(150)으로 200개밖에 없음.
 * int[][] edge : mId가 최대 10억이라.. index로? 
 * 
 * 
 * Q에는 t_id를 추가하고,
 * poll 시에는 해당 t_id의 열차에서 eId 역을 가지는지 확인
 * 아니라면 BFS 진행 -> 그 t_id에서 환승가능한 열차들 중 방문하지 않은 열차 id 큐에 넣기
 * 
 * 아니면 union-find로 같은 그룹인지 확인? => union-find는 그룹이잖아.. 얘는 edge
 * 
 * 특정 열차가 특정 역을 가지는지는 O(1)에 알 수 있음.
 * 특정 열차가 다른 열차와 연결되었는지는..? 해당 열차의 모든 station에 대해서 다른 열차가 그 역을 가졌는지 확인 -> O(100,000 / 3)
 * 
 * 근데 BFS로 하면 add랑 remove가 또 문제가 된다..
 * 
 * 결국 GEMINI 찬스로 해결
 * 1. BFS 시 vis 배열이 아닌 map을 사용해서 vis 처리 <= 이건 생각 못 했다..
 * 		vis.put(?, 1);
 * 		vis.get(?) == null?
 * 		일반적으로 일반 배열이 훨씬 빠르지만 이 경우에는 sparse한 케이스이므로 Map 사용.
 * 2. trains_per_station 없앰.
 * 		쓸모가 초기 Q 설정할 때밖에 없는데 열차의 최대 수가 200밖에 안되므로 그냥 일일이 살펴보는 게 더 효율적
 * 3. 근데 init에서 N은 어디다 쓰는거지..?
 * 4. remove 시 train_map에서 edge 처리를 안 하고 그냥 제거해버림 -> edge 접근 시 주의
 */

/*
 * <로직 정리>
 * 열차 기준으로 한 BFS 구현
 * edge 추가의 경우에는 naive하게 구현
 * BFS 방문 처리 구현 시 sparse한 id 때문에 배열 대신 Map 사용.
 * <문제 조건>
 * 최대 열차 수가 200으로 매우 적음
 * 역 수도 100,000으로 적음.
 * id의 범위는 십억까지로 매우 큼 => id를 index로 한 배열은 절대 안됨.
 */

class Train {
	int st, end, itvl;
	Train(int st, int end, int itvl) {
		this.st = st;
		this.end = end;
		this.itvl = itvl;
	}
	
	// 이 열차가 station에서 정차하는 지 여부를 반환 O(1)
	boolean hasStation(int station) {
		return (st <= station && station <= end) && ((station - st) % itvl == 0);
	}
}

class UserSolution {
	// train id -> 열차 정보
	Map<Integer, Train> train_map;
	// edge 정보, train id 열차에서 연결된 다른 열차
	Map<Integer, Set<Integer>> edge;
	
	public void init(int N, int K, int mId[], int sId[], int eId[], int mInterval[]) {
		train_map = new HashMap<>();
		edge = new HashMap<>();

		for (int i = 0; i < K; i++) {
			train_map.put(mId[i], new Train(sId[i], eId[i], mInterval[i]));
			edge.put(mId[i], new HashSet<>());
		}
		
		List<Integer> keys = new ArrayList<>(train_map.keySet());
		for (int i = 0; i < keys.size(); i++) {
			for (int j = i + 1; j < keys.size(); j++) {
				int id1 = keys.get(i);
				int id2 = keys.get(j);
				if (id1 == id2)	continue;
				if (isConnected2(id1, id2)) {
					edge.get(id1).add(id2);
					edge.get(id2).add(id1);
				}
			}
		}
		
		return;
	}

	public void add(int mId, int sId, int eId, int mInterval) {
		edge.put(mId, new HashSet<>());
		train_map.put(mId, new Train(sId, eId, mInterval));
		
		for (int id : train_map.keySet()) {
			if (id == mId)	continue;
			if (isConnected2(mId, id)) {
				edge.get(mId).add(id);
				edge.get(id).add(mId);
			}
		}
		
		return;
	}

	public void remove(int mId) {
		train_map.remove(mId);
		return;
	}

	public int calculate(int sId, int eId) {
		Queue<Integer> Q = new ArrayDeque<>();
		// int[] vis = new int[MX_ID]; => 너무 메모리 차지가 커서 Map으로 바꿈.
		// 열차 id - 환승 횟수
		Map<Integer, Integer> vis = new HashMap<>();
		
		// sId 역을 갖고 있는 모든 열차에 대해서 Q에 추가/vis 부여
		for (Map.Entry<Integer, Train> entry : train_map.entrySet()) {
			int id = entry.getKey();
			Train t = entry.getValue();
			if (!t.hasStation(sId))	continue;
			vis.put(id, 0);
			Q.add(id);
		}
		int ans = -1;
		// BFS 진행
		while (!Q.isEmpty()) {
			int cur = Q.poll();
			Train t = train_map.get(cur);
			
			// 현재 열차가 목표 역을 가지고 있는 열차라면 종료
			if (t.hasStation(eId)) {
				ans = vis.get(cur);
				break;
			}
			
			// train id = cur인 열차와 연결된 열차들에 대해서 탐색 진행
			for (int t_id : edge.get(cur)) {
				// 이미 방문했다면 지나치기
				if (vis.get(t_id) != null)	continue;
				// 삭제된 열차라면 지나치기
				if (train_map.get(t_id) == null) continue;
				vis.put(t_id, vis.get(cur) + 1);
				Q.add(t_id);
			}
		}
		
		return ans;
	}
	
	// 시간복잡도  O(100,000 / 3)
	boolean isConnected(int t_id1, int t_id2) {
		Train t1 = train_map.get(t_id1);
		Train t2 = train_map.get(t_id2);
		for (int s = t1.st; s <= t1.end; s += t1.itvl) {
			if (t2.hasStation(s))	return true;
		}
		
		return false;
	}
	
	// 실행시간 80ms 감소했지만 유의미하진 않은 것 같다.
	boolean isConnected2(int t_id1, int t_id2) {
		Train t1 = train_map.get(t_id1);
		Train t2 = train_map.get(t_id2);
		int start = Math.max(t1.st, t2.st);
		int end = Math.min(t1.end, t2.end);
		if (start > end)	return false;
		int remain = (start  - t1.st) % t1.itvl;
		int s = start + (remain > 0 ? t1.itvl - remain : 0);
		for (; s <= end; s += t1.itvl) {
			if (t2.hasStation(s))	return true;
		}
		
		return false;
	}
}
