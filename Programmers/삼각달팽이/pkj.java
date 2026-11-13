class Solution {
    public int[] solution(int n) {
        int[] answer = makeArr(n);
        
        return answer;
    }
    
    private int[] makeArr(int n){
        int[][] arr = new int[n][n];
        int[] dx = {1, 0, -1};
        int[] dy = {0, 1, -1};

        int x = 0;
        int y = 0;
        int dir = 0;
        int idx = 1;

        arr[x][y] = idx++;
        
         while (true) {
            int nx = x + dx[dir];
            int ny = y + dy[dir];

             // 벽이나 이미 채워진 칸을 만났을 때, 방향 전환 
            if (nx < 0 || nx >= n || ny < 0 || ny >= n || arr[nx][ny] != 0) {
                dir = (dir + 1) % 3; 
                nx = x + dx[dir];
                ny = y + dy[dir];

                //방향을 돌렸는데 더 채울 필요가 없다면 탐색 종료 
                if (nx < 0 || nx >= n || ny < 0 || ny >= n || arr[nx][ny] != 0) {
                    break;
                }
            }

            x = nx;
            y = ny;
            arr[x][y] = idx++;
        }
        
        int[] result = new int[idx-1];
        int k = 0;

        for (int i = 0; i < n; i++) {
            for (int j = 0; j <= i; j++) {
                result[k++] = arr[i][j];
            }
        }
        
        return result;
    }
}
