import java.io.*;
import java.util.*;

class Solution {
    static int m;
    static int n;
    static int h;
    static int w;
    static long[][] tmpC;
    static long[][] tmp;
    // drops의 길이가 500000x500000 이랬으니까 초과값, 비 안오는 지역 판별
    static final long MAX = 250000000001L;

    public int[] solution(int m, int n, int h, int w, int[][] drops) {
        this.m = m;
        this.n = n;
        this.h = h;
        this.w = w;

        
        long[][] map = new long[m][n];
        //우리에게 중요한건 선인장 구역의 좌상단
        //가로부터 w칸씩 묶어서 저장, 0-2열은 0, 1-3열은 1, ...
        tmpC = new long[m][n - w + 1];
        //세로도 h칸씩 묶어서 저장, 0-3행은 0, 1-4열은 1, ...
        tmp = new long[m - h + 1][n - w + 1];

        //비 안오는 구역이 0이면 판별이 어려우니 MAX값으로 채우고 시작
        for (long[] row : map) {
            Arrays.fill(row, MAX);
        }

        //map에 비 맞는 날짜 저장
        for (int i = 0; i < drops.length; i++) {
            int day = i + 1;
            int x = drops[i][0];
            int y = drops[i][1];
            map[x][y] = day;
        }
        
        //지도 압축
        sliding(map);
        
        return findAnswer();
    }

    //비를 가장 늦게 맞는 지역 찾기 메소드
    static int[] findAnswer() {
        long max = -1;
        int maxX = 0;
        int maxY = 0;

        for (int i = 0; i < tmp.length; i++) {
            for (int j = 0; j < tmp[0].length; j++) {
                if (tmp[i][j] == MAX) {
                    return new int[]{i, j};
                } else if (tmp[i][j] > max) {
                    max = tmp[i][j];
                    maxX = i;
                    maxY = j;
                }
            }
        }

        return new int[]{maxX, maxY};
    }

    //지도 압축하는 메소드
    static void sliding(long[][] map) {
        //최소값의 인덱스를 오름차순으로 유지하기 위함
        Deque<Integer> deque = new ArrayDeque<>();
        
        //일단 가로부터 압축
        for (int i = 0; i < m; i++) {
            deque.clear(); 
            
            for (int j = 0; j < n; j++) {
                //덱의 헤드가 윈도우 범위를 벗어나면 제거
                while (!deque.isEmpty() && deque.peekFirst() <= j - w) {
                    deque.pollFirst();
                }
                
                //새로 들어온 값보다 크거나 같은 기존 값들은 최소가 될 일이 없으니 테일 제거
                while (!deque.isEmpty() && map[i][deque.peekLast()] >= map[i][j]) {
                    deque.pollLast();
                }
                //현재 인덱스 삽입
                deque.offerLast(j);

                //윈도우의 크기 이상 탐색한 시점부터 덱의 헤드값이 윈도우의 최솟값이므로 배열에 저장
                if (j >= w - 1) {
                    tmpC[i][j - w + 1] = map[i][deque.peekFirst()];
                }
            }
        }

        Deque<Integer> dequeV = new ArrayDeque<>();
        
        for (int j = 0; j < n - w + 1; j++) {
            dequeV.clear();
            
            for (int i = 0; i < m; i++) {
                while (!dequeV.isEmpty() && dequeV.peekFirst() <= i - h) {
                    dequeV.pollFirst();
                }
                
                while (!dequeV.isEmpty() && tmpC[dequeV.peekLast()][j] >= tmpC[i][j]) {
                    dequeV.pollLast();
                }
                dequeV.offerLast(i);

                if (i >= h - 1) {
                    tmp[i - h + 1][j] = tmpC[dequeV.peekFirst()][j];
                }
            }
        }
    }
}
