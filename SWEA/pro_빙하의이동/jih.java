package solve.swea.day0901;

import java.util.*;

class UserSolution {
    /*
     * 26-9-1 8:48 ~ 9:01(문제이해) 9:09 ~ 9:59 (설계) ~ 11:25 13:20 ~ 16:10
     *
     * ===
     * 조건
     * - N <= 100
     * - oneYear() 호출 횟수 <= 100
     *
     * ===
     * 성능 제약
     * - 25개 테스트 케이스가 3초 내에 실행
     *
     * ===
     *
     *  oneYear는 어떤 알고리즘인가?
     *  - 100 x 100 크기의 빙하를 시뮬레이션한다면 그냥 구현을 해도 될 것 같다.
     *
     *  - 빙하는 클래스로 저장하자
     * 		- 위쪽과 아랫쪽, 왼쪽과 오른쪽이 연결되어 있으므로 하나의 빙하로 보아야 하므로 클래스가 더더욱 적절
     *  	- 클래스 설계
     *  		- 이동 방향
     *  		- 빙하 조각 이차원 배열
     *  			- 얼음의 높이 정보
     *  			- 얼음의 위치 정보 -> 빙하 조각의 왼쪽 맨 위를 저장한다!
     *
     *  - 각 단계별로 어떤 작업을 수행?
     *  	- 융해 : 바다와 인접한 것의 높이를 1씩 깎아야 한다
     *  		 -> 각 빙하 조각의 외곽을 1씩 깎는다
     *  	- 이동 : 빙하 조각을 맵의 범위와 이동방향에 맞추어서 +-1하면 된다.
     *  	- 병합 :
     *  		- 빙하가 충돌하거나 인접한지는 어떻게 체크?
     *  			- 2차원 배열에 각 빙하 조각을 배치해서 빙하를 병합한다
     *  			- 2차원 배열의 정보를 토대로 다음 빙하 정보를 만들어서 사용하면 될 것 같다
     *		- 시간복잡도는? N * N * k 정도로 보이므로 적절!
     *
     * 5번 테케에서 3번 이동에서 틀리고 있다
     * */
	
	/*
반례
	 
[0, 44, 17, 29, 0, 0, 0, 1, 0, 0, 0, 0, 7, 0, 0]
[0, 41, 33, 41, 0, 0, 1, 9, 0, 31, 0, 0, 31, 14, 0]
[36, 19, 15, 15, 18, 46, 0, 0, 0, 0, 40, 8, 0, 16, 25]
[0, 0, 0, 41, 39, 0, 3, 0, 0, 0, 42, 13, 0, 12, 14]
[0, 0, 0, 12, 0, 0, 0, 21, 0, 0, 0, 0, 0, 11, 0]
[0, 0, 0, 0, 0, 0, 0, 35, 47, 0, 0, 0, 0, 0, 0]
[0, 0, 2, 4, 0, 0, 0, 0, 24, 0, 0, 0, 0, 0, 0]
[0, 0, 45, 0, 0, 0, 47, 5, 23, 0, 0, 0, 0, 0, 0]
[0, 0, 0, 0, 0, 0, 0, 36, 29, 0, 0, 0, 0, 11, 0]
[0, 0, 33, 0, 0, 0, 0, 0, 43, 0, 0, 0, 0, 0, 11]
[0, 18, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0]
[43, 44, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 42, 9]
[0, 38, 33, 40, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 45]
[43, 27, 0, 27, 0, 0, 0, 0, 0, 0, 22, 0, 0, 0, 39]
[0, 0, 13, 18, 34, 0, 0, 8, 0, 0, 0, 34, 27, 0, 0]

0 44 17 29 0 0 0 1 0 0 40 0 7 0 0 
0 41 33 41 0 0 1 9 0 31 42 0 31 14 0 
36 19 15 15 18 46 0 0 0 0 0 8 0 16 25
0 0 0 41 39 0 3 0 0 0 0 13 0 12 14
0 0 0 12 0 0 0 21 0 0 0 0 0 11 0
0 0 0 0 0 0 0 35 47 0 0 0 0 0 0
0 0 2 4 0 0 0 0 24 0 0 0 0 0 0
0 0 45 0 0 0 47 5 23 0 0 0 0 0 0
0 0 0 0 0 0 0 36 29 0 0 0 0 11 0
 0 0 33 0 0 0 0 0 43 0 0 0 0 0 11
  0 18 0 0 0 0 0 0 0 0 0 0 0 0 0
  43 44 0 0 0 0 0 0 0 0 0 0 0 42 9
  0 38 33 40 0 0 0 0 0 0 0 0 0 0 45
  43 27 0 27 0 0 0 0 0 0 22 0 0 0 39
  0 0 13 18 34 0 0 8 0 0 0 34 27 0 0


왜 사리지는 것 같지?
new ice found 2 10
oneYearLater : prev = Iceburg [direction=2, topLeftX=9, topLeftY=1, iceMap=
[0, 0, 0]
[0, 40, 0]
[0, 42, 0]
[0, 0, 0]
]

0 0 14 18 35 0 0 9 0 32 0 35 28 0 0
 0 45 17 30 0 0 0 2 0 0 41 0 8 0 0
  0 42 33 42 0 0 2 10 0 0 43 0 32 15 0
  37 20 16 15 19 47 0 0 0 0 0 9 0 17 26
  0 0 0 42 40 0 4 0 0 0 0 14 0 13 15
 0 0 0 13 0 0 0 22 0 0 0 0 0 12 0
  0 3 5 0 0 0 0 36 48 0 0 0 0 0 0
  0 0 0 46 0 0 0 0 25 0 0 0 0 0 0
  0 0 0 0 0 0 48 6 24 0 0 0 0 0 12
  0 0 0 34 0 0 0 37 30 0 0 0 0 12 0
  0 0 19 0 0 0 0 0 44 0 0 0 0 0 0
  10 44 45 0 0 0 0 0 0 0 0 0 0 0 43 46 0 39 34 41 0 0 0 0 0 23 0 0 0 0 40 44 28 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 28 0 0 0 0 0 0 0 0 0 0 0



	 */

    private final static int MAX_N = 100;
    static final int[] dy = {-1, 0, 1, 0};
    static final int[] dx = {0, 1, 0, -1};

    class RESULT {
        int[][] heights;

        RESULT() {
            heights = new int[MAX_N][MAX_N];
        }
    }

    static class Iceberg {
        int direction;

        // 빙하맵의 가장 왼쪽과 오른쪽을 어떻게 구하지?
        // -> 빙하의 y좌표가 단절되어 있으면 아래에서 가장 작은 것이 topLeftY가 된다
        // -> 빙하의 x좌표가 단절되어 있으면 오른쪽에서 가장 작은 것이 topLeftX가 된다
        int topLeftX;
        int topLeftY;

        int[][] iceMap;
        int[][] prevIceMap;

        public Iceberg(int topLeftX, int topLeftY, int[][] iceMap) {
            super();
            this.topLeftX = topLeftX;
            this.topLeftY = topLeftY;
            this.iceMap = iceMap;
        }

        public void melt() {
            prevIceMap = new int[iceMap.length][iceMap[0].length];
            for (int i = 0; i < iceMap.length; i++) {
                prevIceMap[i] = Arrays.copyOf(iceMap[i], iceMap[0].length);
            }

            Queue<int[]> qu = new LinkedList<>();
            boolean[][] visited = new boolean[iceMap.length][iceMap[0].length];

            for (int y = 0; y < iceMap.length; y++) {
                for (int x = 0; x < iceMap[0].length; x++) {
                    if (iceMap[y][x] == 0) {
                        qu.offer(new int[]{y, x});
                        visited[y][x] = true;
                    }
                }
            }

            while (!qu.isEmpty()) {
                int[] cur = qu.poll();

                for (int i = 0; i < 4; i++) {
                    int ny = cur[0] + dy[i];
                    int nx = cur[1] + dx[i];

//					System.out.printf(&quot;ny nx = %d, %d\n&quot;, ny, nx);

                    if (outOfRange(ny, 0, iceMap.length - 1) || outOfRange(nx, 0, iceMap[0].length - 1)) {
//						System.out.printf(&quot;out of range : %d %d\n&quot;, iceMap.length, iceMap[0].length);
                        continue;
                    }
                    if (visited[ny][nx]) continue;

                    if (iceMap[ny][nx] > 0) {

                        iceMap[ny][nx] -= 1;
                        visited[ny][nx] = true;
                        continue;
                    }

                    qu.offer(new int[]{ny, nx});
                    visited[ny][nx] = true;
                }
            }
        }

        public List<Iceberg> split(){
            // TODO 쪼개진 빙하들을 새로운 빙하들로 분리
            List<Iceberg> ret = new ArrayList<>();

            int[][] visited = new int[iceMap.length][iceMap[0].length];
            for(int y = 0; y < iceMap.length; y++){
                for(int x = 0; x < iceMap[0].length; x++){
                    if(visited[y][x] == 0 && iceMap[y][x] > 0){
                        int[][] ccMap = findCcMap(y, x, iceMap, visited, iceMap.length, iceMap[0].length);

                        // TODO ccMap과 iceMap의 크기가 같게 해야 함
                        int[][] newIceMap = new int[iceMap.length][iceMap[0].length];
                        for(int yy = 1; yy <= iceMap.length - 2; yy++){
                            for(int xx = 1; xx <= iceMap[0].length - 2; xx++){
//                                System.out.printf("split : imHei = %d, imWid = %d, yy = %d, xx = %d\n", iceMap.length, iceMap[0].length, yy, xx);

                                if(ccMap[yy][xx] == 1){
                                    newIceMap[yy][xx] = iceMap[yy][xx];
                                }
                            }
                        }

                        Iceberg ice = new Iceberg(topLeftX, topLeftY, newIceMap);
                        ice.prevIceMap = prevIceMap;
                        ice.direction = this.direction;
                        ret.add(ice);
                    }
                }
            }

            return ret;
        }

        private boolean outOfRange(int c, int lb, int up) {
            return c < lb || c > up;
        }

        private void move() {
            topLeftX = (topLeftX + dx[direction] + N) % N;
            topLeftY = (topLeftY + dy[direction] + N) % N;
        }

        public void copyToMap(int[][] map) {
            for (int y = 1; y < iceMap.length - 1; y++) {
                for (int x = 1; x < iceMap[0].length - 1; x++) {
                    int oriY = (topLeftY + y + N) % N;
                    int oriX = (topLeftX + x + N) % N;

                    if(iceMap[y][x] > 0) {
                        // TODO 빙하 id는 최대높이의 것을 저장
                        if(map[oriY][oriX] < iceMap[y][x]) {
                            map[oriY][oriX] = iceMap[y][x];
                            // TODO 빙하가 겹칠 경우 이동방향에 기여할 빙하가 무엇인지 선택해야 한다
                        }
                    }
                }
            }
        }

        public boolean overlap(int[][] ccMap) {
            for (int y = 1; y < iceMap.length - 1; y++) {
                for (int x = 1; x < iceMap[0].length - 1; x++) {

                    if (iceMap[y][x] == 0) {
                        continue;
                    }

                    int oriY = (topLeftY + y + N) % N;
                    int oriX = (topLeftX + x + N) % N;

                    if (ccMap[oriY][oriX] == 1) {
                        return true;
                    }
                }
            }

            return false;
        }

        public boolean contains(int[][] ccMap) {
            for (int y = 1; y <= iceMap.length - 1; y++) {
                for (int x = 1; x <= iceMap[0].length - 1; x++) {
                    int oriY = (topLeftY - dy[direction] + y + N) % N;
                    int oriX = (topLeftX - dx[direction] + x + N) % N;
                    if (iceMap[y][x] > 0 && ccMap[oriY][oriX] == 1) {
                        return true;
                    }
                }
            }
            return false;
        }

        // TODO 이전의 볼륨과 영역, 좌표를 구해야 한다
        public int getVolume() {
            int ret = 0;
            for (int y = 1; y < iceMap.length - 1; y++) {
                for (int x = 1; x < iceMap[0].length - 1; x++) {
                    if(prevIceMap[y][x] > 0){
                        ret += prevIceMap[y][x];
                    }
                }
            }
            return ret;
        }

        public int getArea() {
            int ret = 0;
            for (int y = 1; y < iceMap.length - 1; y++) {
                for (int x = 1; x < iceMap[0].length - 1; x++) {
                    if (prevIceMap[y][x] > 0) {
                        ret += 1;
                    }
                }
            }
            return ret;
        }

        public int getMinY() {
            for (int y = 1; y < iceMap.length - 1; y++) {
                for (int x = 1; x < iceMap[0].length - 1; x++) {
                    if (prevIceMap[y][x] > 0) {
                        return (topLeftY + y + N) % N;
                    }
                }
            }
            return Integer.MAX_VALUE;
        }

        public int getMinX() {
            for (int x = 1; x < iceMap[0].length - 1; x++) {
                for (int y = 1; y < iceMap.length - 1; y++) {
                    if (prevIceMap[y][x] > 0) {
                        return (topLeftX + x + N) % N;
                    }
                }
            }
            return Integer.MAX_VALUE;
        }

        @Override
        public String toString() {
            String ret = "Iceburg [direction=" + direction + ", topLeftX= " + topLeftX + ", topLeftY=" + topLeftY + ", prevIceMap=\n";

            for (int i = 0; i < iceMap.length; i++)
                ret += Arrays.toString(iceMap[i]) + "\n";

            ret += "]";

            return ret;
        }
    }

    static int N;
    static List<Iceberg> icebergs;

    /*
     * 테케 초기에 빙하 정보를 입력받는 함수
     * */
    void init(int N, int M, int mIceBlock[][], int mIceGroup[][]) {
        this.N = N;

        icebergs = new ArrayList<>();

        for (int i = 0; i < M; i++) {
            int ix = mIceGroup[i][0];
            int iy = mIceGroup[i][1];
            int iDirection = mIceGroup[i][2];

            // 각 빙하의 연결 요소를 찾는다
            int[][] visited = new int[N][N];
            int[][] ccMap = findCcMap(iy, ix, mIceBlock, visited, N, N);

//    		System.out.printf(&quot;init : ccMap = \n &quot;);
//    		for(int t = 0; t < N; t++) {
//    			System.out.printf(&quot;%s\n&quot;, Arrays.toString(ccMap[t]));
//    		}

            // 빙하의 iceMap을 구한다
            Iceberg ice = iceburgFromMap(ccMap, mIceBlock);

            ice.direction = iDirection;

//    		System.out.printf(&quot;init : ice = %s\n&quot;, ice);

            icebergs.add(ice);
        }
    }

    // 현재 좌표에 해당하는 연결요소를 구한다.
    static int[][] findCcMap(int sy, int sx, int[][] map, int[][] visited, int H, int W) {

        int[][] ccMap = new int[H][W];

        Queue<int[]> qu = new LinkedList<>();

        qu.offer(new int[]{sy, sx});
        visited[sy][sx] = 1;
        ccMap[sy][sx] = 1;

        while (!qu.isEmpty()) {
            int[] cur = qu.poll();

            for (int i = 0; i < 4; i++) {
                int ny = (cur[0] + dy[i] + H) % H;
                int nx = (cur[1] + dx[i] + W) % W;

                if (visited[ny][nx] == 1) continue;
                if (map[ny][nx] == 0) continue;

                qu.offer(new int[]{ny, nx});
                visited[ny][nx] = 1;

                ccMap[ny][nx] = 1;
            }
        }

        return ccMap;
    }

    Iceberg iceburgFromMap(int[][] ccMap, int[][] map) {
        int[][] extendedMap = new int[N * 3][N * 3];

        // 배열을 9배로 확장
        for (int sy = 0; sy < N * 3; sy += N) {
            for (int sx = 0; sx < N * 3; sx += N) {
                for (int y = 0; y < N; y++) {
                    for (int x = 0; x < N; x++) {
                        extendedMap[sy + y][sx + x] = map[y][x];
                    }
                }
            }
        }

        // connected를 통해서 iceburg 정보를 결정
        for (int sy = N; sy < N * 2; sy++) {
            for (int sx = N; sx < N * 2; sx++) {
                if (ccMap[sy - N][sx - N] == 1) {

                    // bfs로 iceMap을 찾기

                    Queue<int[]> qu = new LinkedList<>();
                    boolean[][] visited = new boolean[N * 3][N * 3];

                    qu.offer(new int[]{sy, sx});
                    visited[sy][sx] = true;

                    int ltX = sx;
                    int ltY = sy;
                    int rbX = sx;
                    int rbY = sy;

                    while (!qu.isEmpty()) {
                        int[] cur = qu.poll();

                        for (int i = 0; i < 4; i++) {
                            //
                            int ny = (cur[0] + dy[i] + 3 * N) % (3 * N);
                            int nx = (cur[1] + dx[i] + 3 * N) % (3 * N);

                            if (extendedMap[ny][nx] == 0) continue;
                            if (visited[ny][nx]) continue;

                            qu.offer(new int[]{ny, nx});
                            visited[ny][nx] = true;

                            ltX = Math.min(ltX, nx);
                            ltY = Math.min(ltY, ny);
                            rbX = Math.max(rbX, nx);
                            rbY = Math.max(rbY, ny);
                        }
                    }

//    				System.out.printf(&quot;%d %d %d %d\n&quot;, ltX, ltY, rbX, rbY);

                    int[][] iceMap = new int[rbY - ltY + 1 + 2][rbX - ltX + 1 + 2];
                    for (int y = 0; y < rbY - ltY + 1; y++) {
                        for (int x = 0; x < rbX - ltX + 1; x++) {
                            int extendY = (ltY + y) % N;
                            int extendX = (ltX + x) % N;
                            iceMap[y + 1][x + 1] = ccMap[extendY][extendX] == 1 ? extendedMap[extendY][extendX] : 0;
                        }
                    }

                    // 좌표 범위를 0 ~ N -1로 변경
                    ltX = (ltX - 1 + N) % N;
                    ltY = (ltY - 1 + N) % N;

                    return new Iceberg(ltX, ltY, iceMap);
                }
            }
        }

        return null;
    }


    /*
     * 마지막 빙하 생태를 기준으로 1년 후의 빙하 상태를 반환하는 함수
     *
     * */
    RESULT oneYearLater() {
        RESULT res = new RESULT();

        System.out.println("oneYearLater : start ===== ");
        // 빙하를 깎는다
        List<Iceberg> tempIceburgs = new ArrayList<>();

        for (Iceberg ice : icebergs) {
            ice.melt();
//    		System.out.printf("oneYearLater : before split : ice = \n %s\n", ice);
            tempIceburgs.addAll(ice.split());
        }

        icebergs = tempIceburgs;

        // 빙하를 이동시킨다
        for (Iceberg ice : icebergs) {
            ice.move();
        }

        System.out.printf("oneYearLater : after split and move : iceburgs = %s\n\n", icebergs);

        // 빙하를 병합한다
        int[][] map = new int[N][N];
        for (int i = 0; i < icebergs.size(); i++) {
            icebergs.get(i).copyToMap(map);
        }

    	System.out.printf("oneYearLater : map = \n");
		for(int i = 0; i < N; i++) {
			System.out.printf("%s\n", Arrays.toString(map[i]));
		}

//        System.out.printf("oneYearLater : placedIceIndex = \n");
//        for(int i = 0; i < N; i++) {
//            System.out.printf("%s\n", Arrays.toString(placedIceIndex[i]));
//        }

        List<Iceberg> newIceburgs = new ArrayList<>();
        int[][] visited = new int[N][N];

        for (int y = 0; y < N; y++) {
            for (int x = 0; x < N; x++) {
                if (visited[y][x] == 0 && map[y][x] > 0) {
                    int[][] ccMap = findCcMap(y, x, map, visited, map.length, map[0].length);

//    	    		System.out.printf("oneYearLater : ccMap = \n ");
//    	    		for(int t = 0; t < N; t++) {
//    	    			System.out.printf("%s\n", Arrays.toString(ccMap[t]));
//    	    		}

//    				System.out.printf("oneYearLater : new ice found %d %d\n", y, x);

                    // 이전 빙하들을 수집
                    PriorityQueue<Iceberg> pq = new PriorityQueue<>((a, b) -> compareIceburg(a, b));

                    for (Iceberg prevIce : icebergs) {
                        if (prevIce.overlap(ccMap)) {
                            pq.offer(prevIce);
                        }
                    }

                    Iceberg ice = iceburgFromMap(ccMap, map);

                    // ice에 포함된 이전 빙하들 중에서 우선순위가 높은 것을 방향을 결정한다
                    Iceberg prev = pq.poll();

//    				System.out.printf("oneYearLater : prev = %s\n", prev);

                    ice.direction = prev.direction;

                    newIceburgs.add(ice);
                }
            }
        }

        icebergs = newIceburgs;

        res.heights = map;

        return res;
    }

    static int compareIceburg(Iceberg a, Iceberg b){
        int aV = a.getVolume();
        int bV = b.getVolume();
        if (aV != bV) {
            return bV - aV;
        }

        int aA = a.getArea();
        int bA = b.getArea();
        if (aA != bA) {
            return aA - bA;
        }

        int aY = a.getMinY();
        int bY = b.getMinY();
        if (aY != bY) {
            return aY - bY;
        }

        int aX = a.getMinX();
        int bX = b.getMinX();

        return aX - bX;
    }
}