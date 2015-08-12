
package com.baidu.oped.apm.common.util;

/**
 * class JvmVersion 
 *
 * @author meidongxu@baidu.com
 */
public enum JvmVersion {
    JAVA_5(1.5, 49),
    JAVA_6(1.6, 50),
    JAVA_7(1.7, 51),
    JAVA_8(1.8, 52),
    UNSUPPORTED(-1, -1);

    private final double version;
    private final int classVersion;

    private JvmVersion(double version, int classVersion) {
        this.version = version;
        this.classVersion = classVersion;
    }

    public boolean onOrAfter(JvmVersion other) {
        if (this == UNSUPPORTED || other == UNSUPPORTED) {
            return false;
        }
        return this == other || this.version > other.version;
    }

    public static JvmVersion getFromVersion(String javaVersion) {
        try {
            double version = Double.parseDouble(javaVersion);
            return getFromVersion(version);
        } catch (NumberFormatException e) {
            return UNSUPPORTED;
        }
    }

    public static JvmVersion getFromVersion(double javaVersion) {
        for (JvmVersion version : JvmVersion.values()) {
            if (Double.compare(version.version, javaVersion) == 0) {
                return version;
            }
        }
        return JvmVersion.UNSUPPORTED;
    }

    public static JvmVersion getFromClassVersion(int classVersion) {
        for (JvmVersion version : JvmVersion.values()) {
            if (version.classVersion == classVersion) {
                return version;
            }
        }
        return JvmVersion.UNSUPPORTED;
    }
}
