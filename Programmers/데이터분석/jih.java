import java.util.*;

class Solution {
    
    /*
    26-10-30 23:17 ~ 23:32
    */
    public int[][] solution(int[][] data, String ext, int val_ext, String sort_by) {
        Map<String, Integer> colIndex = Map.of("code", 0, "date", 1, "maximum", 2, "remain", 3);

        List<int[]> answer = new ArrayList<>();
        
        for(int[] row : data){
            if(row[colIndex.get(ext)] < val_ext){
                answer.add(row);
            }
        }
        
        int i = colIndex.get(sort_by);
        answer.sort((a, b) -> Integer.compare(a[i], b[i]));
        
        return answer.toArray(new int[0][0]);
    }
}