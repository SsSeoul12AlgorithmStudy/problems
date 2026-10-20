/*
먼소리여~
BFS를 두 번?

게임이 종료되는 상황은 두가지
1. 갈 곳이 없을 때
2. 자기 밑 발판이 사라질 때

근데 BFS를 두 번 하게 되면 두 번째 예시와 같은 경우는 못 잡아냄.
그럼 DFS를 해야 하나?
DFS를 해서 움직인 횟수의 합이 최대가 되게 하는..? X

최적의 플레이라는 건 뭘까..
이길 수 있는 플레이어는 움직임을 최소화, 지는 플레이어는 움직임을 최대화

이길 수 있는 플레이어가 누군지 어떻게 알 수 있는거지..?
DFS를 하다보면 A가 이길 때도 있고 B가 이길 때도 있을텐데..

dfs가 아닌가?

=== 현도형 정리 ===
오케이 누가 이길지를 모른다고 치고, 각 플레이어는 모두 당장에 최적의 선택을 하는 BFS를 한다고 하자.
최적의 선택 : 타 플레이어가 없는 발판이 있다면 거기로 가고, 선택지가 타 플레이어가 있는 발판밖에 없다면 어쩔 수 없이 거기로

BFS가 저장해야 하는 상태 : A위치, B위치, 보드의 상태, 누구의 턴인지, 몇번째 이동인지
보드의 상태는.. 현도 형 말대로 비트마스킹으로 저장..?

--- 틀림 ---
min max + DFS로 풀어야 한다고 함.
쥐쥐

*/

import java.util.*;

class State {
    int[] A;
    int[] B;
    int board_bit;
    char turn;
    int move;
    State(int[] A, int[] B, int board_bit, char turn, int move) {
        this.A = A;
        this.B = B;
        this.board_bit = board_bit;
        this.turn = turn;
        this.move = move;
    }
}

class Solution {
    int max_move = -1;
    int[][] board;
    int n, m;
    
    final int[][] dir = {{1,0}, {0,1}, {-1,0}, {0,-1}};
    
    public int solution(int[][] board, int[] aloc, int[] bloc) {
        this.board = board;
        this.n = board.length;
        this.m = board[0].length;
        
        int bit = boardToBit(board);
        
        State first = new State(aloc, bloc, bit, 'A', 0);
        Queue<State> Q = new ArrayDeque<>();
        Q.add(first);
        
        while (!Q.isEmpty()) {
            State cur = Q.poll();
            
            // 누구 턴인지 확인
            char turn = cur.turn;
            int[] p;
            int[] op;
            if (turn == 'A') {
                p = cur.A;
                op = cur.B;
            } else {
                p = cur.B;
                op = cur.A;
            }
            int board_bit = cur.board_bit;
            int move = cur.move;
            
            // 갈 수 있는 방향 확인하기(우선순위에 따라서 진행 - 상대가 없는 칸이 있으면 거기로 진행)
            List<int[]> normal = new ArrayList<>();
            List<int[]> abnormal = new ArrayList<>();
            
            for (int[] d : dir) {
                int nx = d[0] + p[0];
                int ny = d[1] + p[1];
                if (nx < 0 || ny < 0 || nx >= n || ny >= m) continue;
                if (bitToLoc(nx, ny, board_bit) == 0)   continue;
                if (nx == op[0] && ny == op[1]) {
                    abnormal.add(new int[] {nx, ny});
                } else {
                    normal.add(new int[] {nx, ny});
                }
            }
            
            // 갈 수 있는 방향이 없으면 종료
            if (normal.isEmpty() && abnormal.isEmpty())   return move;
            
            // 혹은 겹쳐있을 때 이동해버리면 종료
            if (p[0] == op[0] && p[1] == op[1])    return move + 1;
            
            for (int[] nloc : (normal.isEmpty() ? abnormal : normal)) {
                int nx = nloc[0];
                int ny = nloc[1];
                int new_bit = removeLoc(p[0], p[1], board_bit);
                
                if (turn == 'A') {
                    Q.add(new State(new int[] {nx, ny}, cur.B, new_bit, 'B', move+1));
                } else {
                    Q.add(new State(cur.A, new int[] {nx, ny}, new_bit, 'A', move+1));
                }
            }
        }
        
        
        return -1;
    }

    
    int boardToBit(int[][] board) {
        int result = 0;

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                result = result << 1;
                result += board[i][j];
            }
        }
        
        return result;
    }
    
    // 1 1 1 1 1 1 1 1 1
    // (2, 0) -> 6 -> 3
    // (1, 1) -> 4 -> 5
    // 비트로부터 (x,y) 위치의 보드 정보를 반환하는 함수
    int bitToLoc(int x, int y, int bit) {
        int k = m * x  + y;
        bit = bit >> (n*m - k - 1);
        return bit % 2;
    }

    // 특정 위치의 발판을 사라지게 하는 함수
    int removeLoc(int x, int y, int bit) {
        int k = m * x  + y;
        int temp = 1 << (n*m - k - 1); // 00000100 -> 반전..?
        temp = ~temp;
        bit = bit & temp;
        
        return bit;
    }
}
