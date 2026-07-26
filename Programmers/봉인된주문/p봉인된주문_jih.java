import java.util.*;

class p봉인된주문_jih {

    public String solution(long n, String[] bans) {
        int removed = 0;

        Set<Long> set = new HashSet<>();
        for(String b : bans){
            long i = getIndex(b);
            set.add(i);
            if(i <= n){
                removed++;
            }
        }

        for(int i = 0; i < removed; i++){
            n++;
            while(set.contains(n)){
                n++;
            }
        }

        return getSpell(n);
    }

    private long getIndex(String spell){
        long index = 1;
        for(int i = 0; i < spell.length(); i++){
            if(i == spell.length() - 1){
                index += spell.charAt(i) - 'a';
            }
            else{
                index += Math.pow(26, spell.length() - i - 1) * (spell.charAt(i) - 'a' + 1);
            }
        }
        return index;
    }

    private String getSpell(long index){
        StringBuilder sb = new StringBuilder();
        while(index > 0){
            index -= 1;
            sb.append((char) ('a' + index % 26));
            index = index / 26;
        }
        sb.reverse();
        return sb.toString();
    }
}