package com.ledungcobra.common;

import java.time.Instant;

public class AuditUtils {

    public static <T extends Audit> T updateAudit(T src, String auditName) {
        src.setUpdateBy(auditName);
        var now = Instant.now();
        src.setUpdateOn(now);
        if (src.createOn == null) {
            src.setCreateOn(now);
        }
        return src;
    }

    public static <T extends Audit> T createAudit(T src, String auditName) {
        var now = Instant.now();
        src.setCreateBy(auditName);
        src.setCreateOn(now);
        return updateAudit(src, auditName);
    }

}
