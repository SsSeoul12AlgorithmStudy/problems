package solve.swea.day0824.swea_무선통신;

import java.util.*;

class UserSolution {
    /*
    * 26-8-23 10:37 ~ 11:03 (이해)
    * ~ 11:32 (시간초과)
    * ~ 13:00 (포기)
    *
    * 한 무선통신기가 mLimit의 파워 내에서 mCount개의 무선통신기를 연결할 때 필요한 파워의 최소값을 구하자
    *
    * ====
    * 공간해싱을 쓰는 문제다
    *
    * */

    final int GRID_SIZE = 100;
    final int[] dy = { -1, 0, 1, 0 };
    final int[] dx = { 0, 1, 0, -1 };

    int gridCount, powerLimit, gridLimit;
    List<Radio>[][] grids;

    Map<Integer, Radio> radioMap;
    /*
    * 맵의 크기와 사용할 수 있는 파워의 최대 한도를 입력받는 함수
    * - 최초에 한번 호출
    * */
	void init(int N, int mLimit)
	{
        gridCount = N / GRID_SIZE + 1;

        grids = new ArrayList[gridCount][gridCount];
        for(int y = 0; y < gridCount; y++){
            for(int x = 0; x < gridCount; x++){
                grids[y][x] = new ArrayList<>();
            }
        }

        powerLimit = mLimit;
        gridLimit = mLimit / (10 * GRID_SIZE) + 1;

        radioMap = new HashMap<>();
	}

    /*
    * 무선통신기를 K개 추가하는 함수
    *
    * - 설치되는 위치에는 무선통신기가 없음이 보장
    * - 최대 500번 호출
    * */
	void addRadio(int K, int mID[], int mFreq[], int mY[], int mX[])
	{
        for(int i = 0; i < K; i++){
            int yIndex = mY[i] / GRID_SIZE;
            int xIndex = mX[i] / GRID_SIZE;
            Radio r = new Radio(mID[i], mFreq[i], mY[i], mX[i]);
            grids[yIndex][xIndex].add(r);
            radioMap.put(mID[i], r);
        }
	}

    /*
    * id에 해당하는 무선통신기에서 mCount개의 다른 통신기를 연결할 때 필요한 최소 파워를 반환하는 함수
    * - 파워 제한 내에서 연결 못하는 경우는 없음
    * - 반환값은 10억을 넘지 못함 -> int 범위
    * - 최대 1000번 호출
    * ===
    *
    * 제약
    * - 테케마다 무선통신기는 최대 5만개
    * - 같은 파워로 연결되면 아이디가 작은 것이 우선 연결???
    * - mCount <= 100
    *
    * 조건
    * - 거리 * 10의 파워가 필요. 거리는 맨하탄 거리
    * - 고유주파수가 다르면 1000 파워가 추가로 필요
    * - ** 한 공유기와 연길 시 필요한 파워가 파워제한을 넘으면 연결 못함 ** !!!
    *
    * ===
    *
    * 어떤 알고리즘?
    *
    * 완탐이 가능할까?
    * - 모든 통신기와 연결하는데 필요한 파워를 구하고, 가장 작은 mCount개를 고른다
    * - 5만 * 1000 = 5천만-> 시간 초과 없을 듯!
    *
    * 최적화 방법은?
    * - 시간초과가 발생하더라!!!
    *   - 다른 두 함수는 시간복잡도를 줄일 수 없으니 여기서 시간복잡도를 줄여야 함
    *
    * - 같은 공유기에 대해서 여러 쿼리가 발생하고 있으니 중복 계산을 줄여야 할 것 같음
    *   - 처음에는 캐싱을 생각했는데 이것은 시간초과가 발생한다
    *       - 만약 캐시가 없는 상태에서 모든 라디오에 대해서 최소 파워를 계산하게 되면 5만 * 1000이라서 시간에 영향을 주는 것 같다
    *
    * - 제일 깔끔한 풀이는 공간해싱이라고 한다
    *   - 공간해싱 : 공간을 그리드로 쪼개서 탐색 범위를 좁히는 방법
    *   - 왜 사용 가능한가?
    *       - 이 문제에서 탐색할 거리의 범위가 주어진 것이나 다름없다
    *       - 거리 <= mLimit / 10 또는 mLimit / 10 - 1000이댜
    *       - 의문 : 통신기가 한 공간에 쏠리지는 않을까?
    *           - 다음 조건 덕분에 공간해싱의 평균적인 성능을 기대해볼 수 있다 : 각 테스트 케이스에서 무선통신기의 위치와 고유 주파수는 임의로 생성되는 것을 보장한다
    * */

    int getMinPower(int mID, int mCount) {
//        System.out.printf("get : ===== ( %d %d ) ====\n", mID, mCount);

        PriorityQueue<Integer> pq = new PriorityQueue<>((a, b) -> a - b);

        boolean[][] visited = new boolean[gridCount][gridCount];
        Queue<int []> qu = new LinkedList<>();

        Radio query = radioMap.get(mID);

        // query 주변에 있는 그리드를 탐색하며 top mCount를 계산
        int qyIndex = query.y / GRID_SIZE;
        int qxIndex = query.x / GRID_SIZE;

        findTopKPowers(pq, mCount, query, grids[qyIndex][qxIndex]);
//        System.out.println("get : pq = " + pq);

        qu.offer(new int[] { qyIndex, qxIndex });
        visited[qyIndex][qxIndex] = true;

        while(!qu.isEmpty()){
            int[] cur = qu.poll();

            for(int i = 0; i < 4; i++){
                int nyIndex = cur[0] + dy[i];
                int nxIndex = cur[1] + dx[i];

                if(outOfRange(nyIndex) || outOfRange(nxIndex)){
                    continue;
                }

                if(visited[nyIndex][nxIndex]){
                    continue;
                }

                if(Math.abs(qyIndex - nyIndex) + Math.abs(qxIndex - nxIndex) > gridLimit){
                    continue;
                }

                findTopKPowers(pq, mCount, query, grids[nyIndex][nxIndex]);

//                System.out.printf("get : bfs = ny nx %d %d \n", nyIndex, nxIndex);
//                System.out.println("get : pq = " + pq);

                qu.offer(new int[] { nyIndex, nxIndex });
                visited[nyIndex][nxIndex] = true;
            }
        }

        // top mCount개의 합을 반환
        int ret = 0;
        for(int i = 0; i < mCount; i++){
            ret += pq.poll();
        }
        return ret;
	}

    void findTopKPowers(PriorityQueue<Integer> pq, int topK, Radio query, List<Radio> grid){
        for(Radio r : grid){
            if(query != r){
                int dist = Math.abs(r.y - query.y) + Math.abs(r.x - query.x);
                int power = dist * 10 + ((r.freq != query.freq) ? 1000 : 0);
                if(power <= powerLimit){
//                    System.out.printf("add (%d) radio to pq\n", r.id);
                    pq.offer(power);
                }
            }
        }
    }

    private boolean outOfRange(int coord){
        return coord < 0 || coord >= gridCount;
    }

    static class Radio{
        int id;
        int freq;
        int y;
        int x;

        public Radio(int id, int freq, int y, int x){
            this.id = id;
            this.freq = freq;
            this.y = y;
            this.x = x;
        }
    }
}