import java.util.*;
 
class UserSolution {
    private final static int MAX_N = 100;
    private int N;
    private int[][] height; // 현재 얼음 높이
    private int[][] glacierId; // 현재 각 얼음칸이 속한 빙하 ID
    private ArrayList<Glacier> glaciers; // 현재 존재하는 빙하 정보
    private static int[] dx = {0, 1, 0, -1};
    private static int[] dy = {-1, 0, 1, 0};
 
    class RESULT {
        int[][] heights;
        RESULT() {
            heights = new int[MAX_N][MAX_N];
        }
    }
 
    class Glacier {
        int dir; // 이동 방향 
        int area; // 면적 
        int volume; // 부피 
        int minY; 
        int minX;
 
        Glacier(int dir) {
            this.dir = dir;
            this.area = 0;
            this.volume = 0;
            this.minY = Integer.MAX_VALUE;
            this.minX = Integer.MAX_VALUE;
        }
 
        void add(int y, int x, int h) {
            area++;
            volume += h;
 
            // 문제에 주어진 조건대로 빙하 위치 저장 
            if (y < minY || (y == minY && x < minX)) {
                minY = y;
                minX = x;
            }
        }
    }
 
    // 얼음덩어리 
    class Ice {
        int y;
        int x;
        int h;
        int glacierId;
 
        Ice(int y, int x, int h, int glacierId) {
            this.y = y;
            this.x = x;
            this.h = h;
            this.glacierId = glacierId;
        }
    }
     
    public void init(int N, int M, int[][] mIceBlock, int[][] mIceGroup) {
        this.N = N;
        height = new int[N][N];
        glacierId = new int[N][N];
 
        for (int y = 0; y < N; y++) {
            for (int x = 0; x < N; x++) {
                height[y][x] = mIceBlock[y][x];
                glacierId[y][x] = -1;
            }
        }
 
        glaciers = new ArrayList<>();
 
        for (int i = 0; i < M; i++) {
            int x = mIceGroup[i][0];
            int y = mIceGroup[i][1];
            int dir = mIceGroup[i][2];
            Glacier g = new Glacier(dir);
 
            // 같은 빙하의 얼음덩어리들 
            bfsInitial(y, x, i, g);
            glaciers.add(g);
        }
    }

    // 같은 빙하의 얼음들 묶기 
    private void bfsInitial(int sy,int sx,int id,Glacier glacier) {
        if (height[sy][sx] == 0) {
            return;
        }
 
        ArrayDeque<Integer> queue = new ArrayDeque<>();
        glacierId[sy][sx] = id;
        queue.offer(sy * N + sx);
 
        while (!queue.isEmpty()) {
            int cur = queue.poll();
            int y = cur / N;
            int x = cur % N;
 
            glacier.add(y, x, height[y][x]);
 
            for (int d = 0; d < 4; d++) {
                int ny = wrapY(y + dy[d]);
                int nx = wrapX(x + dx[d]);
 
                if (height[ny][nx] == 0) {
                    continue;
                }
 
                if (glacierId[ny][nx] != -1) {
                    continue;
                }
 
                glacierId[ny][nx] = id;
                queue.offer(ny * N + nx);
            }
        }
    }
 
    public RESULT oneYearLater() {
 
        // 1) 융해 
        boolean[][] melt = new boolean[N][N]; //녹일 칸 저장
 
        for (int y = 0; y < N; y++) {
            for (int x = 0; x < N; x++) {
                if (height[y][x] == 0) {
                    continue;
                }
                 
                // 상하좌우 중 바다와 인접한 경우 녹는다. 
                for (int d = 0; d < 4; d++) {
                    int ny = wrapY(y + dy[d]);
                    int nx = wrapX(x + dx[d]);
 
                    if (height[ny][nx] == 0) {
                        melt[y][x] = true;
                        break;
                    }
                }
            }
        }
 
        // 녹이기 
        for (int y = 0; y < N; y++) {
            for (int x = 0; x < N; x++) {
                if (melt[y][x]) {
                    height[y][x]--;
                     
                    if (height[y][x] <= 0) {
                        height[y][x] = 0;
                        glacierId[y][x] = -1;
                    }
                }
            }
        }
 
 
        // 쪼개진 빙하 있는지 확인 
        int[][] newGlacierId = new int[N][N];
        for (int y = 0; y < N; y++) {
            Arrays.fill(newGlacierId[y], -1);
        }
 
        ArrayList<Glacier> beforeMove = new ArrayList<>(); // 새로운 빙하 리스트 
        for (int y = 0; y < N; y++) {
            for (int x = 0; x < N; x++) {
                if (height[y][x] == 0) {
                    continue;
                }
 
                if (newGlacierId[y][x] != -1) {
                    continue;
                }
                 
                int oldId = glacierId[y][x];
                int dir = glaciers.get(oldId).dir;
                Glacier g = new Glacier(dir);
                int newId = beforeMove.size();
 
                bfsAfterMelt(y,x,oldId,newId,newGlacierId,g);
                beforeMove.add(g);
            }
        }
 
 
        // 2) 빙하 이동 
        ArrayList<Ice> movedCells = new ArrayList<>();
 
        for (int y = 0; y < N; y++) {
            for (int x = 0; x < N; x++) {
                if (height[y][x] == 0) {
                    continue;
                }
 
                int id = newGlacierId[y][x];
                Glacier g = beforeMove.get(id);
                int ny = wrapY(y + dy[g.dir]);
                int nx = wrapX(x + dx[g.dir]);
 
                movedCells.add(new Ice(ny,nx,height[y][x],id));
            }
        }
 
 
         
        // 3) 병합 
        int glacierCount = beforeMove.size();
        DSU dsu = new DSU(glacierCount);
        int[][] firstGlacier = new int[N][N]; // 각 좌표에 어떤 빙하가 있는지 기록한다.
        for (int y = 0; y < N; y++) {
            Arrays.fill(firstGlacier[y], -1);
        }
         
        for (Ice c : movedCells) {
            if (firstGlacier[c.y][c.x] == -1) {
                firstGlacier[c.y][c.x] = c.glacierId;
            } 
            else {
                dsu.union(c.glacierId,firstGlacier[c.y][c.x]);
            }
        }
         
        // 상하좌우 인접 확인 
        for (Ice c : movedCells) {
            for (int d = 0; d < 4; d++) {
                int ny = wrapY(c.y + dy[d]);
                int nx = wrapX(c.x + dx[d]);
                int other = firstGlacier[ny][nx];

                // 같은 빙하로 묶어주기 
                if (other != -1) {
                    dsu.union(c.glacierId,other);
                }
            }
        }
 
 
        // 높이 계산 
        int[][] newHeight = new int[N][N];
        for (Ice c : movedCells) {
            if (c.h > newHeight[c.y][c.x]) newHeight[c.y][c.x] = c.h;
        }
 
 
        // 빙하의 이동 방향 
        Glacier[] winner = new Glacier[glacierCount];
         
        for (int i = 0; i < glacierCount; i++) {
            int root = dsu.find(i);
 
            if (winner[root] == null) {
                winner[root] = beforeMove.get(i);
            } 
            else {
                Glacier current = beforeMove.get(i);
 
                if (isBetter(current, winner[root])) winner[root] = current;
            }
        }
 
        // 빙하 저장 
        int[] rootToNewId = new int[glacierCount];
        Arrays.fill(rootToNewId, -1);
        ArrayList<Glacier> afterMove = new ArrayList<>();
 
        for (int i = 0; i < glacierCount; i++) {
            int root = dsu.find(i);
            if (rootToNewId[root] != -1) {
                continue;
            }
 
            Glacier win = winner[root];
            Glacier merged = new Glacier(win.dir);
            rootToNewId[root] = afterMove.size();
            afterMove.add(merged);
        }
 
 
        // 면적과 부피 
        int[][] newGlacierIdFinal = new int[N][N];
        for (int y = 0; y < N; y++) {
            Arrays.fill(newGlacierIdFinal[y], -1);
        }
         
        for (Ice c : movedCells) {
            if (c.h != newHeight[c.y][c.x]) {
                continue;
            }
 
            int root = dsu.find(c.glacierId);
            int newId = rootToNewId[root];
 
            if (newGlacierIdFinal[c.y][c.x] == -1) {
                newGlacierIdFinal[c.y][c.x] = newId;
                afterMove.get(newId).add(c.y,c.x,c.h);
            }
        }
         
        height = newHeight;
        glacierId = newGlacierIdFinal;
        glaciers = afterMove;
 
        RESULT result = new RESULT();
        for (int y = 0; y < N; y++) {
            for (int x = 0; x < N; x++) {
                result.heights[y][x] = height[y][x];
            }
        }
 
        return result;
    }
 
    private void bfsAfterMelt(int sy,int sx,int oldId,int newId,int[][] newGlacierId,Glacier glacier) {
        ArrayDeque<Integer> queue = new ArrayDeque<>();
        newGlacierId[sy][sx] = newId;
        queue.offer(sy * N + sx);
 
        while (!queue.isEmpty()) {
            int cur = queue.poll();
            int y = cur / N;
            int x = cur % N;
 
            glacier.add(y, x, height[y][x]);
 
            for (int d = 0; d < 4; d++) {
                int ny = wrapY(y + dy[d]);
                int nx = wrapX(x + dx[d]);
 
                if (height[ny][nx] == 0) continue;
                if (newGlacierId[ny][nx] != -1) continue;
                if (glacierId[ny][nx] != oldId) continue;
 
                newGlacierId[ny][nx] = newId;
                queue.offer(ny * N + nx);
            }
        }
    }
 
    private boolean isBetter(Glacier a,Glacier b) {
         // 부피
        if (a.volume != b.volume) return a.volume > b.volume;
         
        // 면적
        if (a.area != b.area) return a.area < b.area;
 
        // Y좌표 
        if (a.minY != b.minY) return a.minY < b.minY;
 
        // X좌표 
        return a.minX < b.minX;
    }
 
    // 바다 말기 
    private int wrapY(int y) {
 
        if (y < 0) return N - 1;
        if (y >= N) return 0;
        return y;
    }
 
    //바다 말기 
    private int wrapX(int x) {
 
        if (x < 0) return N - 1;
        if (x >= N) return 0;
        return x;
    }
 
    // 지피티의 유니온파인드, 병합 과정  
    class DSU {
        int[] parent; // 각 빙하의 부모 번호 저장 
        int[] rank; // 빙하 트리의 높이 
 
        DSU(int n) {
            this.parent = new int[n];
            this.rank = new int[n];
 
            for (int i = 0; i < n; i++) {
                parent[i] = i;
            }
        }

        // 해당 빙하의 부모 번호 반환 
        int find(int x) {
            if (parent[x] == x) return x;

            // 조상까지 탐색 
            return parent[x] = find(parent[x]);
        }

        // 빙하 병합 
        void union(int a, int b) {
            a = find(a);
            b = find(b);
 
            if (a == b) return;
 
            if (rank[a] < rank[b]) {
                parent[a] = b;
 
            } 
            else if (rank[a] > rank[b]) {
                parent[b] = a;
            } 
            else {
                parent[b] = a;
                rank[a]++;
            }
        }
    }
}
