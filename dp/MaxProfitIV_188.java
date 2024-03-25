package dp;

/**
 * 188. 买卖股票的最佳时机 IV (困难)
 * 给定一个整数数组 prices ，它的第 i 个元素 prices[i] 是一支给定的股票在第 i 天的价格。
 * 设计一个算法来计算你所能获取的最大利润。你最多可以完成 k 笔交易。
 * 注意：你不能同时参与多笔交易（你必须在再次购买前出售掉之前的股票）。
 *
 * 输入：k = 2, prices = [2,4,1]
 * 输出：2
 * 解释：在第 1 天 (股票价格 = 2) 的时候买入，在第 2 天 (股票价格 = 4) 的时候卖出，这笔交易所能获得利润 = 4-2 = 2。
 *
 * 输入：k = 2, prices = [3,2,6,5,0,3]
 * 输出：7
 * 解释：在第 2 天 (股票价格 = 2) 的时候买入，在第 3 天 (股票价格 = 6) 的时候卖出, 这笔交易所能获得利润 = 6-2 = 4 。
 *      随后，在第 5 天 (股票价格 = 0) 的时候买入，在第 6 天 (股票价格 = 3) 的时候卖出, 这笔交易所能获得利润 = 3-0 = 3 。
 */
public class MaxProfitIV_188 {
    public static void main(String[] args) {

    }

    /**
     * 动态规划；T(O())  S(O())
     *  和121,122,123的区别就是，至多可以买k次，
     *                 五部曲：①确定dp含义
     *                           此时一天就有五个状态：
     *                              0：没有操作；1：第一次买入；2：第一次卖出；3：第二次买入；4：第二次卖出.......
     *                             dp[i][j]中 i表示第i天，j为 [0 - k] 五个状态，dp[i][j]表示第i天状态j所剩最大现金。
     *
     *                            大家应该发现规律了吧 ，除了0以外，偶数就是卖出，奇数就是买入。题目要求是至多有K笔交易，那么j的范围就定义为 2 * k + 1 就可以了
     *
     *                       ②确定递推公式
     *                           需要注意：dp[i][1]，表示的是第i天，买入股票的状态，并不是说一定要第i天买入股票，这是很多同学容易陷入的误区。
     *                           达到dp[i][1]状态，有两个具体操作：
     *                              操作一：第i天买入股票了，那么dp[i][1] = dp[i-1][0] - prices[i]
     *                              操作二：第i天没有操作，而是沿用前一天买入的状态，即：dp[i][1] = dp[i - 1][1]
     *                              取操作中最大的；dp[i][1] = max(dp[i-1][0] - prices[i], dp[i - 1][1]);
     *
     *                           同理dp[i][2]也有两个操作：
     *                              操作一：第i天卖出股票了，那么dp[i][2] = dp[i - 1][1] + prices[i]
     *                              操作二：第i天没有操作，沿用前一天卖出股票的状态，即：dp[i][2] = dp[i - 1][2]
     *                              所以dp[i][2] = max(dp[i - 1][1] + prices[i], dp[i - 1][2])
     *
     *                           同理可推出剩下状态部分：
     *                              dp[i][3] = max(dp[i - 1][3], dp[i - 1][2] - prices[i]);
     *                              dp[i][4] = max(dp[i - 1][4], dp[i - 1][3] + prices[i]);
     *                              ....
     *                           通用代码就是：
     *                                  for (int j = 0; j < 2 * k - 1; j += 2) {
     *                                      dp[i][j + 1] = max(dp[i - 1][j + 1], dp[i - 1][j] - prices[i]);
     *                                      dp[i][j + 2] = max(dp[i - 1][j + 2], dp[i - 1][j + 1] + prices[i]);
     *                                   }
     *
     *                       ③dp数组如何初始化
     *                              第0天没有操作，这个最容易想到，就是0，即：dp[0][0] = 0;
     *                              第0天做第一次买入的操作，dp[0][1] = -prices[0];
     *                              第0天做第一次卖出的操作，这个初始值dp[0][2]应该是多少呢？
     *                                  首先卖出的操作一定是收获利润，整个股票买卖最差情况也就是没有盈利即全程无操作现金为0，
     *                                  从递推公式中可以看出每次是取最大值，那么既然是收获利润如果比0还小了就没有必要收获这个利润了。
     *                                  所以dp[0][2] = 0;
     *                              第0天第二次买入操作，初始值应该是多少呢？应该不少同学疑惑，第一次还没买入呢，怎么初始化第二次买入呢？
     *                                  第二次买入依赖于第一次卖出的状态，其实相当于第0天第一次买入了，第一次卖出了，然后在买入一次
     *                                  （第二次买入），那么现在手头上没有现金，只要买入，现金就做相应的减少。dp[0][3] = -prices[0];
     *                              所以同理可以推出dp[0][j]当j为奇数的时候都初始化为 -prices[0]
     *                                  for (int j = 1; j < 2 * k; j += 2) {
     *                                      dp[0][j] = -prices[0];
     *                                   }
     *
     *                       ④确定遍历顺序
     *                              从递推公式可以看出dp[i]都是有dp[i - 1]推导出来的，那么一定是从前向后遍历。
     *
     *
     *                       ⑤举例推导dp数组
     *                                  输入[1,2,3,4,5] k=2为例
     *
     *                                  最后一次卖出，一定是利润最大的，dp[prices.size() - 1][2 * k]即红色部分就是最后求解。
     *
     * @param k
     * @param prices
     * @return
     */
    public static int maxProfit(int k, int[] prices) {
        if (prices.length == 0) {
            return 0;
        }

        // [天数][股票状态]
        // 股票状态: 奇数表示第 k 次交易持有/买入, 偶数表示第 k 次交易不持有/卖出, 0 表示没有操作
        int[][] dp = new int[prices.length][k * 2 + 1];

        //dp数组初始化
        for (int i = 1; i < k * 2; i += 2) {
            dp[0][i] = -prices[0];
        }

        for (int i = 1; i < prices.length; i++) {
            for (int j = 0; j < k * 2-1; j += 2) {
                dp[i][j + 1] = Math.max(dp[i - 1][j + 1], dp[i - 1][j] - prices[i]);//奇数：i天买入
                dp[i][j + 2] = Math.max(dp[i - 1][j + 2], dp[i - 1][j+1] + prices[i]);//偶数：i天卖出
            }
        }
        return dp[prices.length - 1][2 * k];
    }
}
