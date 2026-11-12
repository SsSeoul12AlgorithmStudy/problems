import java.util.*;

class Solution {
    /*
    26/11/12 21:29 ~ 21:51
    
    크기가 n인 삼각형의 첫행부터 마지막행을 합친 배열을 구하자
    
    삼각형 안에 작은 삼각형이 들어있는데 재귀적인 성질이 있는 것 같다
    - n = 5인 삼각형에는 n=3인 삼각형이 있다
    - 근데 특정 위치에 숫자 몇이 있을지는 모른다
    
    크기 1000 x 1000인 배열을 생성해서 달팽이 채우기를 수행해야하나?
    */
    private int[] dy = { 1, 0, -1 };
    private int[] dx = { 0, 1, -1 };
    private static int[][] triangle;
    
    public int[] solution(int n) {
        int length = n * (n + 1) / 2;
        int[] answer = new int[length];
        
        triangle = new int[n][n];
        
        int y = 0;
        int x = 0;
        int direct = 0;
        for(int i = 1; i <= length; i++){
            triangle[y][x] = i;
            
            int ny = y + dy[direct];
            int nx = x + dx[direct];
            
            if(!inRange(ny, nx, n) || triangle[ny][nx] > 0){
                direct = (direct + 1) % 3;
                ny = y + dy[direct];
                nx = x + dx[direct];
            }
            
            y = ny;
            x = nx;
        }
        
        int index = 0;
        for(int i = 0; i < n; i++){
            for(int j = 0; j <= i; j++){
                answer[index] = triangle[i][j];
                index++;
            }
        }
        
        return answer;
    }
    
    private boolean inRange(int ny, int nx, int n){
        return ny >= 0 && ny < n && nx >= 0 && nx < n;
    }
}