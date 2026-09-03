import java.util.Arrays;

class Solution {
    /*
    26-9-2 17:00 ~ 17:20, 22:19 ~ 
    
    마지막에 파괴되지 않은 건물의 개수를 구하자
    
    ===
    
    조건
    
    N <= 1000, M <= 1000
    r, c <= 1000
    skill의 길이 <= 25만
    
    ===
    
    각 스킬을 보드에 바로 반영하는 것은 시간초과가 발생할 것임.
    
    업데이트를 최적화할 방법이 있을까?
    
    보드의 원소의 값 <= 1000이라는 조건은 왜 주어졌을까?
    
    일차원에서는 어떻게 할까?
    
    ===
    힌트를 보니 차분합? 누적합을 이용해야하더라
    
    방법 : 
    일차원 배열에서 [L, R] 구간에 +-D를 여러번 수행해야하면
    diff 배열을 이용해서 업데이트 구간과 증감값을 기록한 후에
    나중에 누적합 1번의 스캔으로 diff배열을 업데이트한다.
    이 diff 배열을 원본 배열에 적용하면 최종 연산 결과를 구할 수 있다.
    
    일차원에서는 단순하게 적용하면 되는데, 이 문제는 2차원이다.
    
    처음에 드는 생각 : 각 행을 1차원 배열로 보고 누적합을 적용할 수 있을까?
    -> 근데 N <= 1000이라서 시간초과의 가능성이 있다!
    
    그래서 행 축에 대한 업데이트도 유사한 논리로 diff 배열을 업데이트해야 한다.
    - 행에 대한 정보는 어떻게 저장하지?
        - 열에 대한 구간의 업데이트는 L에 +D, R + 1 위치에 -D를 하면 된다.
        - 행에 대한 구간의 업데이트는?
            - 행 방향에서 아래쪽에 -D 같은 게 필요하다
            - 지피티의 힌트 : 
                - (ltx, lty), (rbx + 1, lty), (lbx, rby + 1), (rbx + 1, rby) 4군데를 업데이트하면 될 것 같다.
                    - 왜? -> 행방향 업데이트만 고려한 다음에 열방향 업데이트를 계산한다.
            - 그렇다면 어떻게 누적합을 만드나?
                - 행방향 업데이트 : diff[x][y] = diff[x][y] + diff[x - 1][y]
                - 열방향 업데이트 : diff[x][y] = diff[x][y] + diff[x][y - 1]
                - 최종 업데이트는?
                    - diff[x][y] = diff[x][y] + diff[x - 1][y] + diff[x][y - 1]이라 생각했는데 아니다
                - 행 방향 업데이트와 열 방향 업데이트를 같은 배열에 저장하는 것이 문제같다
                    - 행방향 업데이트만 저장한 배열 diff1, 열방향 업데이트만 저장한 배열 diff2를 정의해서
                    - diff1으로 행방향 업데이트의 누적합을 구한 후에, 구한 diff1과 diff2로 열방향 업데이트를 적용하면 최종결과인 것 같다
                    
                    -> 사실은 두 배열로 나눌 필요가 없다! 
    */
    public int solution(int[][] board, int[][] skill) {
        int N = board.length;
        int M = board[0].length;
        
        // 가장자리 테두리에 0 패딩을 두어야 편하다
        int[][] diffR = new int[N + 2][M + 2];
        int[][] diffC = new int[N + 2][M + 2];
        
        for(int i = 0; i < skill.length; i++){
            int degree = skill[i][0] == 1 ? -skill[i][5] : skill[i][5];
            int r1 = skill[i][1] + 1;
            int c1 = skill[i][2] + 1;
            int r2 = skill[i][3] + 1;
            int c2 = skill[i][4] + 1;
            
            diffC[r1][c1] += degree;
            diffC[r1][c2 + 1] -= degree;
            diffR[r2 + 1][c1] -= degree;
            diffR[r2 + 1][c2 + 1] += degree; 
            
//            for(int y = 0; y <= N + 1; y++)
//                 System.out.printf("%s\n", Arrays.toString(diffC[y]));
//             System.out.println();

//               for(int y = 0; y <= N + 1; y++)
//                 System.out.printf("%s\n", Arrays.toString(diffR[y]));
//             System.out.println();
        }    
        
        int[][] diffResult = new int[N + 2][M + 2];
        
        // 행방향 업데이트 
        for(int r = 1; r <= N; r++){
            for(int c = 1; c <= M; c++){
                diffC[r][c] = diffC[r][c] + diffC[r][c - 1];
                diffR[r][c] = diffR[r][c] + diffR[r][c - 1];
            }
        }
        
//          for(int y = 0; y <= N + 1; y++)
//             System.out.printf("%s\n", Arrays.toString(diffC[y]));
//         System.out.println();
        
//           for(int y = 0; y <= N + 1; y++)
//             System.out.printf("%s\n", Arrays.toString(diffR[y]));
//         System.out.println();
        
        // 열방향 업데이트
        for(int c = 1; c <= M; c++){
            for(int r = 1; r <= N; r++){    
                // if(r == 3 && c == 1){
                //     System.out.printf("diffC sum = %d \n", diffC[r][c] + diffC[r - 1][c]); 
                // }
                diffC[r][c] = diffC[r][c] + diffC[r - 1][c] + diffR[r][c];
            }
        }
        
        // for(int i = 0; i <= N + 1; i++)
        //     System.out.printf("%s\n", Arrays.toString(diffC[i]));
        // System.out.println();
        
        // 최종 결과
        int answer = 0;
        
        for(int br = 0; br < N; br++){
            for(int bc = 0; bc < M; bc++){
                if(board[br][bc] + diffC[br + 1][bc + 1] > 0){
                    answer++;
                }
            }
        }
        
        return answer;
    }
}