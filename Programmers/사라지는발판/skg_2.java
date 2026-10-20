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

=== DFS 구현 ===
DFS 함수의 리턴 값 : (win or lose, 이동 횟수)

DFS 를 해서 리턴 값이 여러 개 올텐데 그 중에서
win이 있으면 win 중에 최솟값
lose만 있으면 lose 중 최댓값

다음 거에서 win이 오면 -> 내 입장에서는 내가 졌다는 뜻
다음 거에서 lose가 오면 -> 내 입장에서는 내가 이겼다는 뜻

*/

import java.util.*;

class Result {
    boolean win;
    int move;
    Result(boolean win, int move) {
        this.win = win;
        this.move = move;
    }
}

class Solution {
    final int[][] dir = {{1,0}, {-1,0}, {0,1}, {0,-1}};
    
    int[][] board;
    int n, m;
    
    public int solution(int[][] board, int[] aloc, int[] bloc) {
        this.board = board;
        this.n = board.length;
        this.m = board[0].length;
        
        Result res = DFS(0, aloc, bloc);
        
        return res.move;
    }

   Result DFS(int move, int[] p, int[] opp) {
       
       // 기저 조건
       // 갈 수 있는 위치 찾기
       List<int[]> cand = new ArrayList<>();
       
       for (int[] d : dir) {
           int nx = d[0] + p[0];
           int ny = d[1] + p[1];
           if (nx < 0 || ny < 0 || nx >= n || ny >= m)  continue;
           if (board[nx][ny] != 1)  continue;
           cand.add(new int[] {nx, ny});
       }
       
       // 1. 현재 플레이어가 갈 곳이 없을 때
       if (cand.isEmpty()) {
           return new Result(false, move);
       }
       // 2. 겹친 상태에서 현재 플레이가 갈 곳이 있을 때
       if (p[0] == opp[0] && p[1] == opp[1]) {
           return new Result(true, move + 1);
       }
       
       // 가능한 경로에 대해 DFS 진행
       List<Integer> win = new ArrayList<>();
       List<Integer> lose = new ArrayList<>();
       for (int[] next : cand) {
          
           board[p[0]][p[1]] = 0;
           Result res = DFS(move + 1, opp, next);
           if (res.win) {
               lose.add(res.move);
           } else {
               win.add(res.move);
           }
           board[p[0]][p[1]] = 1;
       } 
       
       // DFS 진행 결과 취합
       if (win.isEmpty()) {
           Collections.sort(lose);
           return new Result(false, lose.get(lose.size() - 1));
       } else {
           Collections.sort(win);
           return new Result(true, win.get(0));
       }
       
   }
}
