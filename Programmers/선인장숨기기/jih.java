import java.util.*;

class jih {
    /*
    덱을 이용하면 특정 범위 내에 있는 최솟값을 구할 수 있다.
    덱을 단조증가 덱으로 관리하면 덱의 맨 앞에 최솟값이 저장된다.
    */
    public int[] solution(int m, int n, int h, int w, int[][] drops) { 
        int[][] map = new int[m][n];
        for(int i = 0; i < m; i++) Arrays.fill(map[i], Integer.MAX_VALUE);
        
        for(int i = 0; i < drops.length; i++){
            int[] drop = drops[i];
            map[drop[0]][drop[1]] = i + 1;
        }
        
        int[][] rowMins = new int[m][n];
        
        // 각 행마다 w 사이즈의 윈도우 내에서의 최솟값을 구한다
       
        for(int r = 0; r < m; r++){
            Deque<int []> dq = new ArrayDeque<>();
            
            for(int c = 0; c < w; c++){
                while(!dq.isEmpty() && dq.peekLast()[0] > map[r][c]){
                    dq.removeLast();
                }
                dq.addLast(new int[] { map[r][c], c });
            }
            
            for(int c = 0; c < w; c++){
                rowMins[r][c] = dq.peekFirst()[0]; 
            }
            
            for(int c = w; c < n; c++){
                while(!dq.isEmpty() && dq.peekFirst()[1] < c - (w - 1)){
                    dq.removeFirst();
                }
                while(!dq.isEmpty() && dq.peekLast()[0] > map[r][c]){
                    dq.removeLast();
                }
                dq.addLast(new int[] { map[r][c], c });
                rowMins[r][c - (w - 1)] = dq.peekFirst()[0];
            }
        }
       
        
        // for(int i = 0; i < m; i++)
        //     System.out.println(Arrays.toString(rowMins[i]));
        
        int[][] colMins = new int[m][n];
        
        // 각 열에서 h 사이즈의 윈도우에서 최솟값을 구하자
        for(int c = 0; c < n; c++){
            Deque<int []> dq = new ArrayDeque<>();
            
            for(int r = 0; r < h; r++){
                 while(!dq.isEmpty() && dq.peekLast()[0] > rowMins[r][c]){
                    dq.removeLast();
                }
                dq.addLast(new int[] { rowMins[r][c], r });
            }
            
            for(int r = 0; r < h; r++){
                colMins[r][c] = dq.peekFirst()[0];
            }
           
            for(int r = h; r < m; r++){
                while(!dq.isEmpty() && dq.peekFirst()[1] < r - (h - 1)){
                    dq.removeFirst();
                }
                while(!dq.isEmpty() && dq.peekLast()[0] > rowMins[r][c]){
                    dq.removeLast();
                }
                dq.addLast(new int[] { rowMins[r][c], r });
                colMins[r - (h - 1)][c] = dq.peekFirst()[0];
            }
        }
        
        // System.out.println("colMins");
        // for(int i = 0; i < m; i++)
        //     System.out.println(Arrays.toString(colMins[i]));
        
        int[] answer = { 0, 0 };
        int maxValue = 0;
        for(int r = 0; r < m - (h - 1); r++){
            for(int c = 0; c < n - (w - 1); c++){
                if(maxValue < colMins[r][c]){
                    maxValue = colMins[r][c];
                    answer = new int[] { r, c };
                }
            }
        }
        
        return answer;
    }
}