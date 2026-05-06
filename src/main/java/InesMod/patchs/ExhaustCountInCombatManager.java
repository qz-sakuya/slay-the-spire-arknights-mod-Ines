package InesMod.patchs;


import InesMod.helpers.LogHelper;

/**
 * 维护 战斗中消耗卡牌数 的计数器
 * 增加由 MoveToExhaustPilePatc 处理
 * 清空由 AtBattleStartPreDrawPatch 处理
 */
public class ExhaustCountInCombatManager {
    public static int exhaustCountInCombat = 0;

    public static void add(int amount) {
        exhaustCountInCombat += amount;

        LogHelper.info("===ExhaustCountInCombatManager：add：当前值{}===", exhaustCountInCombat);
    }


    public static void set(int value) {
        exhaustCountInCombat = value;

        LogHelper.info("===ExhaustCountInCombatManager：set：当前值{}===", exhaustCountInCombat);
    }
}
