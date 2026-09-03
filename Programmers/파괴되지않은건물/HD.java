/*
재풀이 문제
- 1시간 초과 후에 풀이 궁금해서 찾아봄.

시간 복잡도
1. 단순 구현 : O(QNM)
2. 누적합    : O(Q + NM)

예시2)
skill 별 4칸 꼭짓점 진행 후 prefixSum[][]

       c0  c1  c2  c3  c4  c5

r0     -4  -1   0   0   1   4
r1      2   0  -2   0   0   0
r2     -2   0   0   0   2   0
r3      2   0   0   0  -2   0
r4      2   1   2   0  -1  -4

행 누적합 -> 열 누적합 or 반대로 누적합 진행하면 누적합 결과 prefixSum 완성

배열 크기만큼 board[x][y] += prefixSum[x][y] 


** 약 2500억 회 연산 vs 400만 회 연산 **

결론 - 그냥 이해하기로 함.
++ 직사각형 영역 전체에 어떤 값을 더하는 문제 -> 오늘 처럼
*/

class Solution {
    public int solution(int[][] board, int[][] skill) {
        
        int N = board.length, M = board[0].length;
        int[][] prefixSum = new int[N + 1][M + 1];
        
        for(int[] s : skill) {
            
            int type = s[0], r1 = s[1], c1 = s[2], r2 = s[3], c2 = s[4];
            int degree = type != 1 ? s[5] : -s[5];
            
            prefixSum[r1][c1] += degree;
            prefixSum[r1][c2 + 1] -= degree;
            prefixSum[r2 + 1][c1] -= degree;
            prefixSum[r2 + 1][c2 + 1] += degree;
        }
        
        // 행 누적
        for(int r = 0; r < N; r++) {
            for(int c = 1; c < M; c++) {
                prefixSum[r][c] += prefixSum[r][c - 1];    
            }
        }
        
        // 열 누적 
        for(int c = 0; c < M; c++) {
            for(int r = 1; r < N; r++) {
                prefixSum[r][c] += prefixSum[r - 1][c];    
            }
        }
        
        int result = 0;
        for(int r = 0; r < N; r++) {
            for(int c = 0; c < M; c++) {
                board[r][c] += prefixSum[r][c];
                if(board[r][c] > 0) result++;
            }
        }
        
        return result;
    }
}
