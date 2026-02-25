package com.meteor.meteorenderbox.util;

import org.bukkit.entity.*;
import org.bukkit.permissions.*;
import com.meteor.meteorenderbox.*;
import java.util.*;

public class Util
{
    public static int getMaxLockBox(final Player player) {
        for (final PermissionAttachmentInfo perm : player.getEffectivePermissions()) {
            if (perm.getPermission().startsWith("meteor.ender.")) {
                return Integer.valueOf(perm.getPermission().substring(13));
            }
        }
        return MeteorEnderBox.Instance.getConfig().getInt("setting.default-maxlock");
    }
}
