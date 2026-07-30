class Solution {
    static int result;
    static boolean[] visited;
    public int solution(String begin, String target, String[] words) {
        
        result = 51;
        visited = new boolean[words.length];
        
        dfs(begin, target, words, 0);
        
        if(result == 51) return 0;
        return result;
    }
    
    private void dfs(String now, String target, String[] words, int depth) {
        
        if(depth > words.length) return;
        
        if(now.equals(target)) {
            if(result > depth) {
                result = depth;
            }
            
            return;
        }
        
        for(int i = 0; i < words.length; i++) {
            if(visited[i]) continue;
            
            int cnt = 0;
            for(int j = 0; j < now.length(); j++) {
                if(now.charAt(j) != words[i].charAt(j)) cnt++;
            }
            
            if(cnt == 1) {
                visited[i] = true;
                dfs(words[i], target, words, depth + 1);
                visited[i] = false;
            }
        }
    }
}