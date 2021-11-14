package fr.raphoulfifou.cyan.util;

import java.util.HashMap;
import java.util.Map;

public enum OpLevels
{
    NOTOP(0),
	OP1(1),
	OP2(2),
	OP3(3),
    OPMAX(4);

    private int value;
    private static Map<Integer, OpLevels> map = new HashMap<>();

    private OpLevels(int value) {
        this.value = value;
    }

    static {
        for (OpLevels opLevels : OpLevels.values()) {
            map.put(opLevels.value, opLevels);
        }
    }

    public static OpLevels valueOf(int opLevels) {
        return (OpLevels) map.get(opLevels);
    }

    public int getValue() {
        return value;
    }
}
