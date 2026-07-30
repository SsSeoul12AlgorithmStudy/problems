import java.util.*;

class SV {
    String s;
    int cost;
    SV(String s, int cost) {
        this.s = s;
        this.cost = cost;
    }
}

class Solution {
    public int solution(String begin, String target, String[] words) {
        int answer = 0;
        
        // 어떤 단어를 방문했는지 여부를 확인하는 배열
        boolean[] vis = new boolean[words.length];
        
        Queue<SV> Q = new ArrayDeque<>();
        Q.add(new SV(begin, 0));
        
        while (!Q.isEmpty()) {
            SV cur = Q.poll();
            if (cur.s.equals(target)) {
                answer = cur.cost;
                break;
            }
            for (int i = 0; i < words.length; i++) {
                if (vis[i]) continue;
                if (diffChar(cur.s, words[i]) != 1)   continue;
                Q.add(new SV(words[i], cur.cost + 1));
                vis[i] = true;
            }
        }
        
        return answer;
    }
    
    // 두 문자열 간 다른 문자의 개수
    int diffChar(String s1, String s2) {
        char[] s1c = s1.toCharArray();
        char[] s2c = s2.toCharArray();
        
        int res = 0;
        for (int i = 0; i < s1c.length; i++) {
            if (s1c[i] != s2c[i])   res++;
        }
        
        return res;
    }
}
