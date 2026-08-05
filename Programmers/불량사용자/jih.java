import java.util.*;

class Solution {
    /*
    26-8-6 0:40 ~ 
    
    ===
    
    제재대상 리스트의 경우의 수를 구하자
    아이디 목록에서 순서는 상관이 없다
    
    내가 선택한 아이디들이 이전에 본 것인지 알 수 있게
    방문 여부를 메모이제이션하자
    
    */
    int answer = 0;
    String [] UserId;
    String[] BannedId;
    int[] selected;
    boolean[] seen;
        
    public int solution(String[] user_id, String[] banned_id) {
        UserId = user_id;
        BannedId = banned_id;
        
        selected = new int[BannedId.length];
        
        seen = new boolean[1<<8];
       
        makeBlackList(0, 0, 0);
        
        return answer;
    }
    
    private void makeBlackList(int depth, int banCount, int state){
        if(depth >= UserId.length){
            if(!seen[state] && banCount == BannedId.length){
                System.out.println(Arrays.toString(selected));
                seen[state] = true;
                answer += 1;
            }
            return;
        }
         
        for(int j = 0; j < BannedId.length; j++){
            if(selected[j] > 0){
                continue;
            }
            if(isMatched(UserId[depth], BannedId[j])){
                selected[j] = depth + 1;
                int nextState = state | (1 << depth);
                makeBlackList(depth + 1, banCount + 1, nextState);
                selected[j] = 0;
            }
        }
        
        makeBlackList(depth + 1, banCount, state);
    }
    
    private boolean isMatched(String user, String ban){
        if(user.length() != ban.length()){
            return false;
        }
        for(int i = 0; i < user.length(); i++){
            if(user.charAt(i) != ban.charAt(i) && ban.charAt(i) != '*'){
                return false;
            }
        }
        return true;
    }
}