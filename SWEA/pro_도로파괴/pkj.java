import java.util.*;

// 메인 아이디어는 다익스트라로 단방향 그래프의 최단경로를 찾으면서
// 배열에 해당 노드에 연결된 최소 간선의 아이디를 저장하고
// 도착지에서 역추적하면서 최단 경로 복구하고
// 최단 경로의 간선을 지우면서 최장 거리를 찾는 것
 
class UserSolution {
    private Map<Integer, List<RoadInfo>> graph;
    private Map<Integer, RoadInfo> idInfo;
    private int N;
     
    class RoadInfo {
        int id, start, end, time;
         
        public RoadInfo(int id, int start, int end, int time) {
            this.id= id;
            this.start = start;
            this.end = end;
            this.time = time;
        }
    }
     
    public void init(int N, int K, int mId[], int sCity[], int eCity[], int mTime[]) {
        this.N = N;
        graph = new HashMap<>(); //키: 출발지, 밸류: 간선정보 
        idInfo = new HashMap<>(); //키: 아이디, 밸류: 해당 아이디 간선의 정보 
         
        for (int i = 0; i < N+1; i++) {
            graph.put(i, new ArrayList<>());
        }
         
        for (int i = 0; i < K; i++) {
            add(mId[i], sCity[i], eCity[i], mTime[i]);
        }
         
        return;
    }
 
    public void add(int mId, int sCity, int eCity, int mTime) {
        RoadInfo road = new RoadInfo(mId, sCity, eCity, mTime);
        idInfo.put(mId, road);
        graph.get(sCity).add(road);
    }
 
    public void remove(int mId) {
        if (!idInfo.containsKey(mId)) return;
         
        RoadInfo road = idInfo.get(mId);
        idInfo.remove(mId);
         
        List<RoadInfo> list = graph.get(road.start);
        if (list != null) {
            list.removeIf(r -> r.id == mId); //해당되는 아이디의 간선 정보삭제 
        }
    }
 
    public int calculate(int sCity, int eCity) {
        // 최단거리 다익스트라 
        DijkstraResult result = dijkstra(sCity, eCity, -1);
        int minTime = result.distance;
 
        // 목적지에 도달할 수 없는 경우
        if (minTime == -1) return -1;

        //최단경로일 시 간선들 
        List<Integer> pathRoadIds = getShortesRoute(sCity, eCity, result.prevRoad);
         
        int maxTime = minTime;
 
        // 최단경로가 없어야 하기 때문에 최단경로에 있는 간선을 제거하면서 최장거리 찾기 
        for (int excludeId : pathRoadIds) {
            //적의 군대는 항상 최단거리로 움직이기 때문에 도로가 파괴돼도 최단거리로 이동한다
            DijkstraResult tmp = dijkstra(sCity, eCity, excludeId);
            int time = tmp.distance;
 
            if (time == -1) return -1; //여기서 왜 리턴인지 모르겠음... continue로 하면 테케 다 틀림
            maxTime = Math.max(maxTime, time);
        }
 
        return maxTime - minTime;
    }
     
    // 다익스트라 결과, 거리와 노드들
    private static class DijkstraResult {
        int distance;
        int[] prevRoad;
 
        public DijkstraResult(int distance, int[] prevRoad) {
            this.distance = distance;
            this.prevRoad = prevRoad;
        }
    }
 
    // 다익스트라
    private DijkstraResult dijkstra(int start, int end, int excludeRoadId) {
         //우선순위 큐 구현 구글링함...
        PriorityQueue<int[]> pq = new PriorityQueue<>(Comparator.comparingInt(a -> a[1]));

        //해당 노드에 접근하는 최소비용 
        int[] dist = new int[N + 1];
        Arrays.fill(dist, Integer.MAX_VALUE);
      
        //최단경로 복원을 위한 배열 
        //노드번호를 인덱스, 값을 간선아이디로 
        int[] prevRoad = new int[N + 1];
        Arrays.fill(prevRoad, -1);
 
        dist[start] = 0;
        pq.offer(new int[] {start, 0});
 
        while (!pq.isEmpty()) {
            int[] current = pq.poll();
 
            int currentCity = current[0];
            int currentDist = current[1];
 
            // 이미 더 짧은 경로를 발견했다면 다음 노드로 
            if (currentDist > dist[currentCity]) continue;
 
            // 목적지 도착
            if (currentCity == end) {
                return new DijkstraResult(currentDist,prevRoad);
            }
 
           // 현재 노드 에서 출발하는 간선들
            for (RoadInfo road : graph.getOrDefault(currentCity,Collections.emptyList())) {
 
                // 제외해야 하는 간선
                if (road.id == excludeRoadId) continue;
 
                int nextCity = road.end;
                int nextDist = currentDist + road.time;
 
                // 더 짧은 경로를 발견한 경우
                if (nextDist < dist[nextCity]) {
                    dist[nextCity] = nextDist;
 
                    // nextCity로 들어오는 최단경로 간선을 저장
                    prevRoad[nextCity] = road.id;
 
                    pq.offer(new int[] {nextCity,nextDist});
                   }
                }
            }
 
        // 목적지까지 도달하지 못함
        return new DijkstraResult(-1,prevRoad);
    }
 
 
    // 최단 경로에 포함된 간선 ID 추출
    private List<Integer> getShortesRoute(int start, int end, int[] prevRoad) {
        List<Integer> roadIds = new ArrayList<>();
        int currentCity = end;
 
        // 목적지에서 시작점까지 역추적
        while (currentCity != start) {
            //간선 아이디로 이전 노드에 연결된 간선 확인
            int roadId = prevRoad[currentCity];
 
            // 이전 간선이 없다면 경로가 없음
            if (roadId == -1) return new ArrayList<>();
 
            // 현재 도시로 들어온 간선 저장
            roadIds.add(roadId);
 
            // 해당 간선의 출발 도시로 이동
            RoadInfo road = idInfo.get(roadId);
 
            currentCity = road.start;
        }
 
        return roadIds;
    }
}
