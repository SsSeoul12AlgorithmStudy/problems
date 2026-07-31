class Solution {
    boolean[] visit;
    int answer;
    
    public int solution(String begin, String target, String[] words) {
        answer = Integer.MAX_VALUE;
        visit = new boolean[words.length];
        
        dfs(begin, target, words, 0); 
        
        if(answer == Integer.MAX_VALUE) return 0;
        return answer;
    }
    
    public void dfs(String begin, String target, String[] words, int cnt){
        //현재 답이 더 작으면 리턴
        if(cnt >= answer) return;

        //찾는 단어이면 답 갱신
        if(begin.equals(target)){
            answer = cnt;
            return;
        }

        //배열 돌면서 하나씩 비교
        for(int i = 0; i < words.length; i++){
            //탐색했던 단어면 다음 단어로
            if(visit[i]){
                continue;
            }

          //탐색을 안했다면
            int c = 0;

            //같은 철자가 몇개 있는지 카운트
            for(int j = 0; j < begin.length(); j++){
                if(begin.charAt(j) == words[i].charAt(j)){
                    c++;
                }
            }

            //한 자리만 다르면
            if(c == begin.length() - 1){ 
                visit[i] = true;
                dfs(words[i], target, words, cnt+1);
                //백트래킹
                visit[i] = false; 
            }
        }
    }
}
