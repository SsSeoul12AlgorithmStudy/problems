class Solution {
    static int[][] info;
    static int n, m, sum;
    static int[][] visited;

    public int solution(int[][] info, int n, int m) {
        this.info = info;
        this.n = n;
        this.m = m;
        this.sum = Integer.MAX_VALUE;
        visited = new int[info.length + 1][n]; // A의 흔적에 따른 B의 최소 흔적
        
        for (int i = 0; i <= info.length; i++) {
            java.util.Arrays.fill(visited[i], Integer.MAX_VALUE);
        }

        makeSet(0, 0, 0);

        return sum == Integer.MAX_VALUE ? -1 : sum;
    }

    private static void makeSet(int idx, int sumA, int sumB) {
        
        // 최소 흔적 이상이거나 이전 기록보다 크면 탐색 불필요
        if (sumA >= n || sumB >= m || sumA >= sum) return;
        
        // A의 흔적이 sumA일 당시에 sumB의 흔적의 최소값보다 크면 탐색 불필요
        if (visited[idx][sumA] <= sumB) return;
        
        // 마지막 물건까지 훔쳤다면 최소인지 확인 후 갱신
        if (idx == info.length) {
            sum = Math.min(sum, sumA);
            return;
        }
        
        visited[idx][sumA] = sumB;

        makeSet(idx + 1, sumA, sumB + info[idx][1]);
        makeSet(idx + 1, sumA + info[idx][0], sumB);
    }
}
