package xyz.crafttogether.craftcore.discord;

import xyz.crafttogether.craftcore.CraftCore;

import java.util.Map;
import java.util.TimerTask;

/**
 * TimerTask which checks whether the verification codes have expired, and remove those who have expired
 */
public class VerifyExpireTask extends TimerTask {

    /**
     * Runs the TimerTask
     */
    @Override
    public void run() {
        for (Map.Entry<Long, VerifyCode> set : CraftCore.getVerificationCodes().entrySet()) {
            if (set.getValue().hasExpired()) {
                CraftCore.removeVerificationCode(set.getKey());
            }
        }
    }
}
