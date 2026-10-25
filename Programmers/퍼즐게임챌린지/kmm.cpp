//현재 퍼즐 diff, 현재 퍼즐 소요시간 time_cur
//이전 퍼즐 time_prev, 숙련도 level
//diff <= level --> time_cur 소요 해결
//diff > level 시 (diff-level) * (time_cur + time_prev) + time_cur
//다 틀리면 time_cur 만큼의 시간 사용 퍼즐 해결
//제한 시간 limit이 존재
//모두 해결하기 위한 숙련도 최솟값

#include <vector>
#include <algorithm>
using namespace std;

int solution(vector<int> diffs, vector<int> times, long long limit) {
    // 숙련도는 최소 1, 최대 난이도까지 탐색
    // 최대 난이도만큼 숙련도가 있으면 모든 퍼즐을 바로 해결
    int left = 1;
    int right = *max_element(diffs.begin(), diffs.end());

    while (left < right) {
        // 해결 가능한지 확인할 숙련도
        int level = left + (right - left) / 2;

        // 총시간은 int 범위를 넘을 수 있어 long long을 사용
        long long total = times[0];

        for (int i = 1; i < diffs.size(); i++) {
            int diff = diffs[i];
            int time_cur = times[i];
            int time_prev = times[i - 1];

            if (diff <= level) {
                // 숙련도가 충분하면 현재 퍼즐 시간만 더하기
                total += time_cur;
            } else { //아닐시
                int fail_count = diff - level;

                // 실패할 때마다 현재 퍼즐과 바로 이전 퍼즐의 시간 연산
                // (diff-level) * (time_cur + time_prev) + time_cur
                // 1LL을 곱해 곱셈부터 long long으로 계산진행
                total += 1LL * fail_count * (time_cur + time_prev) + time_cur;
            }

            // 이미 제한 시간을 넘었다면 break
            if (total > limit) break;
        }

        if (total <= limit) {
            // 현재 level이 최소 정답일 수 있으므로 후보에 저장
            right = level;
        } else {
            left = level + 1;
        }
    }

    // left == right가 되면 return
    return left;
}
