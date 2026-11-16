/*

흠.. 재귀?
아씨 어려운데..
직접 채운다?
n이 1000까지인데 이게 되나?
1
2-9
3-10-8
4-5-6-7

뭐야 되네.. 쩝..

*/

class Solution {
    private int[][] arr;
    private final int[][] DIRS = {{1, 0}, {0, 1}, {-1, -1}};
    
    public int[] solution(int n) {
        int[] answer = {};
        
        arr = new int[n][n];
        
        int r = 0;
        int c = 0;
        int num = 1;
        int dir = 0;
        while (true) {
            arr[r][c] = num++;
            int nr = r + DIRS[dir][0];
            int nc = c + DIRS[dir][1];
            if (OOB(nr, nc, n) || arr[nr][nc] != 0) {
                dir = (dir + 1) % 3;
                nr = r + DIRS[dir][0];
                nc = c + DIRS[dir][1];
                if (OOB(nr, nc, n) || arr[nr][nc] != 0) {
                    break;
                }
            }
            r = nr;
            c = nc;
        }
        
        answer = new int[num - 1];
        int k = 0;
        for (int i = 0; i < n; i++) {
            for (int j = 0; j <= i; j++) {
                answer[k++] = arr[i][j];
            }
        }
        
        return answer;
    }
    
    private boolean OOB(int r, int c, int n) {
        return (r < 0 || c < 0 || r >= n || c >= n);
    }
}
