import java.util.*;

class Solution {
    /*
    26-10-20 9:34 ~ 11:30, 23:00 ~ 24:30
    
    양 플레이어가 최적의 플레이를 수행할 때 두 캐릭터가 움직인 횟수의 합을 구하자
    
    ===
    
    지는 사람은 최대한 늦게 지고 이기는 사람은 최대한 빨리 이겨야 한다
    -> A는 자기 턴에서 자기가 최대한 빨리 이기거나 최대한 늦게 이기는 수를 선택해야한다
    B도 마찬가지.
    
    완전탐색이 가능할까?
    - 최악의 경우 (WH)!의 시간이 걸릴 것이다
    
    ===
    조건
    - board의 크기 <= 5 * 5
    
    시간 제한 
    - 10초
    
    */
    private int[] dy = { -1, 0, 1, 0 };
    private int[] dx = { 0, 1, 0, -1 };
    private int W, H;
    
    public int solution(int[][] board, int[] aloc, int[] bloc) {
        H = board.length;
        W = board[0].length;
        
        int[] ret = solve(0, aloc[0], aloc[1], bloc[0], bloc[1], board);
        
        return ret[1];
    }
    
    private int[] solve(int depth, int ay, int ax, int by, int bx, int[][] board){
        // A턴인 경우 내가 빨리 이기거나 늦게 지는 수를 선택
        // B턴인 경우에도 마찬가지
        if(depth % 2 == 0){
            if(!inRange(by, bx)){
                return new int[] { 0, -1 };
            }
            
            if(notOnPad(by, bx, board)){
                return new int[] { 0, -1 };
            }
            
            if(notOnPad(ay, ax, board)){
                return new int[] { 1, 0 };
            }
            
            PriorityQueue<int []> pq = new PriorityQueue<>((a, b) -> {
                // 내가 이기는 수를 우선
                if(a[0] != b[0]){
                    return Integer.compare(a[0], b[0]);
                }

                // 내가 빨리 이기는 수를 우선
                if(a[0] == 0){
                    return Integer.compare(a[1], b[1]);
                }

                // 내가 질 수 밖에 없다면 늦게지는 수를 우선
                return -Integer.compare(a[1], b[1]);
            });
            
            for(int i = 0; i < 4; i++){
                int ny = ay + dy[i];
                int nx = ax + dx[i];

                board[ay][ax] = 0;

                int[] result = solve(depth + 1, ny, nx, by, bx, board);
                pq.offer(result);
                
                board[ay][ax] = 1;
            }
            
            int[] ret = pq.poll();
            if(ret == null){
                System.out.printf("%d %d %d %d", ay, ax, by, bx);
                for(int i = 0; i < H; i++)
                    System.out.printf("%s\n", Arrays.toString(board[i]));
                System.out.println(pq);
                return new int[] { 12, 12 };
            }
            ret[1] += 1;
            return ret;
        }
        
        if(!inRange(ay, ax)){
            return new int[] { 1, -1 };
        }
        
        if(notOnPad(ay, ax, board)){
            return new int[] { 1, -1 };
        }
        
        if(notOnPad(by, bx, board)){
            return new int[] { 0, 0 };
        }
        
        PriorityQueue<int[]> pq = new PriorityQueue<>((a, b) -> {               
            if(a[0] != b[0]){
                return -Integer.compare(a[0], b[0]);
            }

            // 내가 빨리 이기는 수를 우선
            if(a[0] == 1){
                return Integer.compare(a[1], b[1]);
            }

            // 내가 질 수 밖에 없다면 늦게지는 수를 우선
            return -Integer.compare(a[1], b[1]);
        });

        for(int i = 0; i < 4; i++){
            int ny = by + dy[i];
            int nx = bx + dx[i];

            if(!inRange(ny, nx)) continue;

            board[by][bx] = 0;
            
            int[] result = solve(depth + 1, ay, ax, ny, nx, board);
            pq.offer(result);
            
            board[by][bx] = 1;
        }
            
        int[] ret = pq.poll();
        ret[1] += 1;
        return ret;
    }
    
    private boolean notOnPad(int y, int x, int[][] board){
        return board[y][x] == 0;
    }
    
    private boolean inRange(int ny, int nx){
        return ny >= 0 && ny < H && nx >= 0 && nx < W;
    }
}