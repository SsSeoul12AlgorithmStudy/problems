// 250,000 * 1000 * 1000 + 1000 * 1000
// 250,000 + 1000 * 1000 으로 할 수 있는 방법?
// 세그먼트 트리?
/*
바로바로 보드를 업데이트하지 않고
변경사항만 저장..?
(r1, c1), (r2, c2), degree를 봤을 때 O(1)에 이 정보를 저장하는 방법?
또 (a, b) 의 변경 사항을 O(1)에 확인하는 방법?
bucket으로 나누기?
겹치면 -> 구간 나누기
아 뭐지 진짜? 화나네ㅔ
(r1, c1), (r2, c2)와 특정 좌표 (x, y)가 주어졌을 때 해당 좌표가 영향을 받는지 여부는 O(1)에 확인 가능.
250,000 * 1000 * 1000..
250,000번에 걸쳐서 정보를 입력받고 -> 1000 * 1000 각 좌표에 대해서 입력받은 정보로 값 계산.

좌표를 넣었을 때 그 값의 변경 정보가 O(1)혹은 O(logN)에 확인 가능하도록?
트리에 저장..?
2시간 노력했으니 풀이 봐야겠다..

누적합으로 풀어야 한다?

풀이 보니까 벽 느껴지네;;;
일차원 배열에서 누적합 : 시작 위치에 degree값을 넣고, 종료 위치 + 1에 -degree 값을 넣는다.
누적합 시에는 for (int i = 0; i < N-1; i++) arr[i+1] += arr[i];
3 0 0 0 -3 0 => 누적합 => 3 3 3 3 0 0

이걸 이차원 배열로 변환

3 0 0 0 -3 0
0 0 0 0 0 0
0 0 0 0 0 0
-3 0 0 0 3 0

row -> col이든 col->row이든 순서 상관없이 누적합 진행하고 나면
row 진행 후
3 3 3 3 0 0
0 0 0 0 0 0
0 0 0 0 0 0
-3 -3 -3 -3 0 0
col 진행 후
3 3 3 3 0 0
3 3 3 3 0 0
3 3 3 3 0 0
0 0 0 0 0 0

=> 근데 이러면 행/열이 하나씩 더 필요할 것 같은데?
*/
class Solution {
    int[][] cumsum;
    int N;
    int M;
    public int solution(int[][] board, int[][] skill) {
        int answer = 0;
        N = board.length;
        M = board[0].length;
        
        cumsum = new int[N+1][M+1];
        
        for (int[] s : skill) {
            int type = s[0];
            int r1 = s[1];
            int c1 = s[2];
            int r2 = s[3];
            int c2 = s[4];
            int degree = s[5];

            add(r1, c1, r2, c2, (type == 1 ? -1 : 1) * degree);
        }
        
        for (int i = 0; i <= N; i++) {
            for (int j = 1; j <= M; j++) {
                cumsum[i][j] += cumsum[i][j-1];
            }    
        }

        
        for (int j = 0; j <= M; j++) {
            for (int i = 1; i <= N; i++) {
                cumsum[i][j] += cumsum[i-1][j];
            }
        }

        
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < M; j++) {
                board[i][j] += cumsum[i][j];
                if (board[i][j] > 0)    answer++;
            }
        }

        
        return answer;
    }
    
    void printBoard(int[][] b) {
        System.out.println("=======");
        for (int i = 0; i < b.length; i++) {
            for (int j = 0; j < b[0].length; j++) {
                System.out.print(b[i][j] + " ");
            }
            System.out.println();
        }
    }
    
    void add(int r1, int c1, int r2, int c2, int degree) {
        cumsum[r1][c1] += degree;
        cumsum[r2 + 1][c1] -= degree;
        cumsum[r1][c2 + 1] -= degree;
        cumsum[r2+1][c2+1] += degree;

    }
}
