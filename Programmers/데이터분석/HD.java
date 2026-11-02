/*

data에서 ext < val_ext 데이터만 뽑아 sort_by에 해당하는 기준으로 오름차순 정렬

data[i] = { 코드번호(code), 제조일(date), 최대수량(maximum), 현재수량(remain) }

data는 최대 500개

시간복잡도
- 500개 중 ext < val_ext 인 갯수 검사 - 500 
- 단순 풀이 -> O(N^2)
-> 충분히 가능할듯

*/

import java.util.ArrayDeque;
import java.util.Queue;

class Solution {
    public int[][] solution(int[][] data, String ext, int val_ext, String sort_by) {
        
        int extSt = strToInt(ext);
        int sortSt = strToInt(sort_by);
        
        Queue<int[]> q = new ArrayDeque<>();
        
        // ext < val_ext 
        for(int[] d : data) {
            if(d[extSt] >= val_ext) continue;
            q.offer(d);
        }
        
        int[][] result = new int[q.size()][4];
        int idx = 0;
        while (!q.isEmpty()) {
            result[idx++] = q.poll();
        }

        // 선택 정렬 - 오름차순
        for (int i = 0; i < result.length - 1; i++) {
            int minIdx = i;

            // 현재 위치부터 가장 작은 값 탐색
            for (int j = i + 1; j < result.length; j++) {
                if (result[j][sortSt] < result[minIdx][sortSt]) {
                    minIdx = j;
                }
            }

            // 현재 위치와 최솟값 교환
            int[] temp = result[i];
            result[i] = result[minIdx];
            result[minIdx] = temp;
        }
        
        return result;
    }
    
    private int strToInt(String ext) {
        
        if(ext.equals("code")) {
            return 0;
        } else if(ext.equals("date")) {
            return 1;
        } else if(ext.equals("maximum")) {
            return 2;
        }
        
        return 3;
    }
}
