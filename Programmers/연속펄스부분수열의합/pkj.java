class Solution {
    static long[] minusDP;
	  static long[] plusDP;
    
    public long solution(int[] sequence) {
        long answer = 0;
        minusDP = new long[sequence.length];
    	  plusDP = new long[sequence.length];
        
        answer = findPulse(sequence);
        return answer;
    }
    
    static long findPulse(int[] s) {
    	int minusFlag = -1;
    	int plusFlag = 1;
    	minusDP[0] = (long)(minusFlag * s[0]); //첫자리 -1 곱해서 시작하는 경우
    	plusDP[0] = (long)(plusFlag * s[0]); //첫자리 1 곱해서 시작하는 경우
    	long result = Math.max(minusDP[0], plusDP[0]);
    	 
    	minusFlag *= -1;
    	plusFlag *= -1;
    	for(int i = 1; i < s.length; i++) {
    		minusDP[i] = Math.max((minusDP[i-1] + minusFlag*s[i]), (minusFlag*s[i]));
    		plusDP[i] = Math.max((plusDP[i-1] + plusFlag*s[i]), plusFlag*s[i]);
    		
    		long tmp = Math.max(minusDP[i], plusDP[i]);
    		result = Math.max(result, tmp);
    		
    		minusFlag *= -1;
        plusFlag *= -1;
    	}
    	
    	return result;
    }
}
