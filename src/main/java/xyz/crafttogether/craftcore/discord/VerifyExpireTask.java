package xyz.crafttogether.craftcore.discord;

import xyz.crafttogether.craftcore.CraftCore;

import java.util.Map;
import java.util.TimerTask;

public class VerifyExpireTask extends TimerTask {
    @Override
    public void run() {
        for (Map.Entry<Long, VerifyCode> set : CraftCore.getVerificationCodes().entrySet()) {
            if (set.getValue().hasExpired()) {
                CraftCore.removeVerificationCode(set.getKey());
            }
        }
    }
}
