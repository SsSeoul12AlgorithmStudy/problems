class Solution {
    /*
    26/10/27 17:08 ~ 18:00 20:08 ~ 20:36
    
    numbers의 각 정수가 이진트리로 표현될 수 있는지 판정하는 프로그램을 구현
    
    7의 이진수 = 111
    42의 이진수 = 0101010
    
    ---
    
    이진수가 포화이진트리가 되기 위한 조건은?
    - 이진수 왼쪽에 0을 채울 수 있음
    - 이진수의 길이는 2의 등비수열의 합이어야 함
    - 루트는 무조건 1
    - 왼쪽 자식이 0이면 그 아래에 0만 있어야 함, 오른쪽 자식도 마찬가지
    
    완전탐색이 가능한가?
    - 십진수를 이진수로 변환하고 위의 규칙을 만족하는지 판정
        - 최악의 경우 이진수의 길이 * 2를 확인
        - 0을 추가하는 경우를 고려
    - 시간복잡도 : numbers의 길이 * 이진수의 길이 * 2
        <= 1만 * log 10^15 * 2 = 1만 * 50 * 2
        
    */
    public int[] solution(long[] numbers) {
        int[] answer = new int[numbers.length];
        
        for(int i = 0; i < numbers.length; i++){
            if(canConvert(numbers[i])){
                answer[i] = 1;
            }
            else{
                answer[i] = 0;
            }
        }
                
        return answer;
    }
    
    private boolean canConvert(long number){
        // 이진수로 바꾼다
        String binary = getBinary(number);
        
        // 포화이진트리가 될 수 있는 길이로 만들어서 조건을 만족하는지 검사
        StringBuilder sb = new StringBuilder();
        
        for(int i = 0; i < binary.length(); i++){
            String padding = sb.toString();
            String target = padding + binary;
            
            // System.out.println(target);
            
            if(!isPossibleLength(target.length())){
                sb.append("0");  
                continue;
            }
            
            if(target.charAt(target.length() / 2) == '0'){
                sb.append("0");  
                continue;
            }
            
            // System.out.println(target);
            
            if(!isFullBinaryTree(target, 0, target.length() - 1)){
                sb.append("0");  
                continue;
            }
            
            return true; 
        }
    
        return false;
    }
    
    private String getBinary(long number){
        StringBuilder sb = new StringBuilder();
        
        while(number > 0){
            sb.append(number % 2);
            number = number / 2;
        }
        return sb.reverse().toString();
    }
               
    private boolean isPossibleLength(int length){
        for(int i = 2; i - 1 <= length; i = i * 2){
            if(i - 1 == length){
                return true;
            }
        }
        return false;
    }
    
    private boolean isFullBinaryTree(String binary, int left, int right){
        if(left == right){
            return true;
        }
        
        int mid = (left + right) / 2;
        
        if(binary.charAt(mid) == '0'){
            boolean ret = true;
            
            for(int i = left; i <= right; i++){
                if(binary.charAt(i) == '1'){
                    ret = false;
                    break;
                }
            }
            
            return ret;
        }
        
        boolean ret = true;
        
        ret &= isFullBinaryTree(binary, left, mid - 1);
        ret &= isFullBinaryTree(binary, mid + 1, right);

        return ret;
    }
}