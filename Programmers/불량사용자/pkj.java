import java.util.*;

class Solution {
    //모든 조합 저장, 중복 불가
    static Set<Set<String>> resultSets = new HashSet<>();
    
    public int solution(String[] user_id, String[] banned_id) {
		int answer = 0;
		ArrayList<ArrayList<String>> ubList = new ArrayList<>(); //ban-user 매핑 
		
		//정규표현식으로 변경
		//정규표현식 .은 모든 문자를 커버해줌
		for (int b = 0; b < banned_id.length; b++) {
            StringBuilder nBan = new StringBuilder();
            for (int i = 0; i < banned_id[b].length(); i++) {
                char c = banned_id[b].charAt(i);
                if (c == '*') {
                    nBan.append('.');
                } else {
                    nBan.append(c);
                }
            }
            banned_id[b] = nBan.toString();
        }
		
		//불량 사용자로 고려 가능한 아이디를 ban id와 매핑
        for (String ban : banned_id) {
            ArrayList<String> matched = new ArrayList<>();
            for (String user : user_id) {
                // 글자 수가 같고 정규식 패턴에 맞으며, 아직 포함되지 않았다면
                if (user.length() == ban.length() && user.matches(ban)) {
                    matched.add(user);
                }
            }
            ubList.add(matched);
        }
        
        makeComb(0, ubList, new HashSet<>()); //조합 만들기
		
		answer = resultSets.size();
		return answer;
    }
    
    //조합 만들기: ban의 인덱스, user와 ban 매핑 리스트, 현재 조합
    public static void makeComb(int depth, ArrayList<ArrayList<String>> ubList, Set<String> currentComb) {
        
        //모든 banned_id에 대해 매칭 아이디를 하나씩 골랐을 때
        if (depth == ubList.size()) {
          // 현재 조합을 결과 Set에 추가, 중복 조합 불허
            resultSets.add(new HashSet<>(currentComb)); 
            return;
        }

        //현재 불량 사용자에 매칭되는 후보 아이디들 탐색 
        for (String userId : ubList.get(depth)) {
            //이미 다른 불량 사용자 매칭에 사용된 아이디라면 중복 선택 불가
            if (!currentComb.contains(userId)) {
                currentComb.add(userId);
                makeComb(depth + 1, ubList, currentComb);
                currentComb.remove(userId); //백트래킹
            }
        }
    }
}
