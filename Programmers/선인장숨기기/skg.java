// 3시간 넘게 풀다 포기하고 힌트 보고 2시간동안 구현함..
package programmers;
// commit test
import java.util.*;

class VT {
    int v, t;
    VT(int v, int t) {
        this.v = v;
        this.t = t;
    }
}

// 덱을 써라...

// 1번 접근: 단순히 4중 for문 -> nxmxwxh로 시간 초과
// 2번 접근: 역순으로 각 물칸을 포함할 수 있는 좌상단 칸들의 물 내리는 시간 초기화해서 구하기 -> (nxm)xnxm으로 시간 초과
// 3번 접근: 1xw 블럭으로 생각하고 -> 1xw블록 위아래로 h개로 생각해서 풀기 -> 2*nxm

class Solution {
    static final int MX = 500000 * 500000;
    static int m, n, h, w;
    // 각 칸 별로 물이 떨어지는 초
    static int[][] board;
    static int ans_t = -1;
    public static int[] solution(int m, int n, int h, int w, int[][] drops) {
        Solution.m = m;
        Solution.n = n;
        Solution.h = h;
        Solution.w = w;
        
        int[] answer = {0, 0};
        // 이차원 배열에 물 떨어지는 시간 기입하기 (안 떨어지면 INF로 설정)
        board = new int[m][n];
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                board[i][j] = MX;
            }
        }
        for (int t = 1; t <= drops.length; t++) {
            board[drops[t-1][0]][drops[t-1][1]] = t;
        }
        
        Deque<VT> dq = null;
        
        // row 업데이트
        for (int i = 0; i < m; i++) {
            dq = new ArrayDeque<>();
            for (int j = 0; j < n; j++) {
                // 너무 멀리 떨어진 애들은 탈락
                while (!dq.isEmpty() && (j - dq.peekFirst().t >= w))  dq.removeFirst();
                // 들어올 값보다 크거나 같은 애들 탈락
                while(!dq.isEmpty() && dq.peekLast().v >= board[i][j])  dq.removeLast();
                // 넣기
                dq.addLast(new VT(board[i][j], j));
                // j-w+1번째 칸 업데이트
                if (!dq.isEmpty() && (j-w+1 >= 0))
                    board[i][j-w+1] = dq.peekFirst().v;
            }     
        }
        
        // column 업데이트
        for (int j = 0; j < n; j++) {
            dq = new ArrayDeque<>();
        	for (int i = 0; i < m; i++) {
        		// 너무 멀리 떨어진 애들은 탈락
                while(!dq.isEmpty() && i - dq.peekFirst().t >= h)  dq.removeFirst();
                // 들어올 값보다 크거나 같은 애들 탈락
                while(!dq.isEmpty() && dq.peekLast().v >= board[i][j])  dq.removeLast();
                // 넣기
                dq.addLast(new VT(board[i][j], i));
                // i-h+1번째 칸 업데이트
                if (!dq.isEmpty() && i-h+1 >= 0)
                    board[i-h+1][j] = dq.peekFirst().v;
        	}
        }

        answer = findSol();
            
        return answer;
    }
    
    // 좌상단으로 가능한 칸들 중에서 가장 board 값이 큰 좌표 구해서 반환하는 함수
    static int[] findSol() {
        int[] answer = new int[] {0, 0};
        for (int i = 0; i < m - h + 1; i++) {
        	for (int j = 0; j < n - w + 1; j++) {
        		if (board[i][j] > board[answer[0]][answer[1]]) {
        			answer[0] = i;
        			answer[1] = j;
        			// if (board[answer[0]][answer[1]] == MX) return answer;
        		}
        	}
        }
        
        return answer;
    }
    
    // 출력 확인용 함수
    static void printT() {
    	System.out.println("========");
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                System.out.printf(board[i][j] + " ");
            }
            System.out.println();
        }
    }
    
}
