class Solution {
    public int solution(int n, int[] stations, int w) {
        int answer = 0;
        boolean[] con = new boolean[n];
        
        for (int sn : stations) {
            sn--;
            for (int i = -w; i <= w; i++) {
                if (sn + i < 0 || sn + i >= n)  continue;
                con[sn + i] = true;
            }
        }
        
        for (int i = 0; i < n; i++) {
            if (!con[i]) {
                // System.out.println(i + " is not connected!");
                // 해당 위치가 i일 때, i + w에 기지국 설치
                // 기지국 설치 후 con 배열 업데이트
                for (int j = -w; j <= w; j++) {
                    if (i+w+j < 0 || i+w+j >= n)    continue;
                    con[i+w+j] = true;
                    // System.out.println(i+w+j + " is connected!");
                } 
                i = i+w+w;
                answer++;
            }
        }

        return answer;
    }
    
}
