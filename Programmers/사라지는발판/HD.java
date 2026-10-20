/*

1. 승리/패배 의 기준 플레이어를 지정함 -> 실패

A기준
- 승리시 resultW Math.min(round, resultW) 갱신
- 패배시 resultL Math.max(round, resultL) 갱신

왜 안되는지 완벽히 이해하지 못함.

정리해보면, 턴에 이동하는 플레이어 기준으로 승패여부에 따라서 결정해야한다는거 같음.
테케1번이 성립하려면?

근데 왜 dfs로 다음이동에 대해서 승패여부를 받아와야하는건가?
그냥 현재 기준으로 승패여부에 따른 갱신만 이뤄지면 안되나?

-> 안된다고 한다.. 18. 
"네가 놓친 핵심은 A가 선택한 이동 경로 이후에 B도 자신에게 유리한 선택을 한다는 사실을 반영하지 못했다는 것이야"

문제결론..
이 문제는 DFS로 모든 경우의 수를 탐색하되, 각 플레이어가 자신의 턴에서 최적의 선택을 해야 한다.
따라서 단순히 전체 탐색 결과의 최솟값과 최댓값을 전역 변수에 갱신하는 방식으로는 해결할 수 없다.
각 DFS 호출에서 승패와 이동 횟수를 계산하고, 그 결과를 부모 DFS에 전달해야 각 플레이어의 최적 선택을 반영할 수 있다.

minMax 알고리즘.. GG
*/

class Solution {
    
    int N, M, resultW, resultL;
    int[][] board;
    int[] dr = { -1, 0, 1, 0 }, dc = { 0, -1, 0, 1 };
    
    void init(int[][] board) {
        this.board = board;
        N = board.length;
        M = board[0].length;
        resultW = Integer.MAX_VALUE;
        resultL = -1;
    }
    
    public int solution(int[][] board, int[] aloc, int[] bloc) {
        
        init(board);
        
        dfs(0, aloc, bloc);
        
        if(resultW == Integer.MAX_VALUE) {
            return resultL;
        }
        
        return resultW;
    }
    
    class Result {
        boolean isWin;
        int round;
    }
    
    // 턴, a위치, b위치
    void dfs(int round, int[] a, int[] b) {
        
        // 종료 여부 체크 - 승리 패배 구분 resultW / resultL
        // 1. 현재 이동할 플레이어가 움직일 수 없다면 종료 - round
        // 2. 두 플레이어가 같은 공간에서 현재 플레이어가 이동한다면 종료 - round + 1
        int count = 0;
        if(round % 2 == 1) {
            
            // 현재 같은 위치여부 판단
            if(board[a[0]][a[1]] == 0) {
                resultL = Math.max(resultL, round - 1);
                return;
            }
            
            // 이동가능여부 판단
            for(int d = 0; d < 4; d++) {
                int nr = a[0] + dr[d];
                int nc = a[1] + dc[d];
                
                if(nr < 0 || nc < 0 || nr >= N || nc >= M) continue;
                
                if(board[nr][nc] == 0) continue;
                
                count++;
            }
            
            // A 이동시간에 A가 이동하지 못함 - lose
            if(count == 0) {
                resultL = Math.max(resultL, round);
                return;
            }
        } else {
            
            // 현재 같은 위치여부 판단
            if(board[b[0]][b[1]] == 0) {
                resultW = Math.min(resultW, round - 1);
                return;
            }
            
            for(int d = 0; d < 4; d++) {
                int nr = b[0] + dr[d];
                int nc = b[1] + dc[d];
                
                if(nr < 0 || nc < 0 || nr >= N || nc >= M) continue;
                
                if(board[nr][nc] == 0) continue;
                
                count++;
            }
            
            // B 이동시간에 B가 이동하지 못함
            if(count == 0) {
                resultW = Math.min(resultW, round);
                return;
            }
        }
        
        
        
        // 턴에 따른 이동 여부 체크
        // round 홀수는 A턴 짝수는 B턴
        
        // A턴
        if(round % 2 != 0) {
            int r = a[0];
            int c = a[1];
            
            for(int d = 0; d < 4; d++) {
                
                int nr = r + dr[d];
                int nc = c + dc[d];
                
                // 보드 밖
                if(nr < 0 || nc < 0 || nr >= N || nc >= M) continue;
                
                // 발판 x
                if(board[nr][nc] == 0) continue;
                
                board[r][c] = 0;
                dfs(round + 1, new int[]{ nr, nc }, b);
                board[r][c] = 1;
            }
        } else {
            int r = b[0];
            int c = b[1];
            
            for(int d = 0; d < 4; d++) {
                
                int nr = r + dr[d];
                int nc = c + dc[d];
                
                // 보드 밖
                if(nr < 0 || nc < 0 || nr >= N || nc >= M) continue;
                
                // 발판 x
                if(board[nr][nc] == 0) continue;
                
                board[r][c] = 0;
                dfs(round + 1, a, new int[]{ nr, nc });
                board[r][c] = 1;
            }
        }
    }
}
