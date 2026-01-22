package com.minh.thread;
import java.lang.management.ManagementFactory;
import java.lang.management.ThreadMXBean;

public class WallCpuProbe {

    private static final ThreadMXBean THREAD_MX_BEAN =
            ManagementFactory.getThreadMXBean();

    static {
        if (THREAD_MX_BEAN.isThreadCpuTimeSupported()) {
            THREAD_MX_BEAN.setThreadCpuTimeEnabled(true);
        }
    }

    public static Probe start(String name) {
        return new Probe(name);
    }

    public static class Probe {
        private final String name;
        private final long wallStart;
        private final long cpuStart;

        private Probe(String name) {
            this.name = name;
            this.wallStart = System.nanoTime();
            this.cpuStart = getCpuTime();
        }

        public void end() {
            long wallEnd = System.nanoTime();
            long cpuEnd = getCpuTime();

            long wallNs = wallEnd - wallStart;
            long cpuNs  = cpuEnd - cpuStart;

            long waitNs = wallNs - cpuNs;

            System.out.println(
                    "[PROBE] " + name +
                            " | wall=" + wallNs + "ns" +
                            " | cpu="  + cpuNs  + "ns" +
                            " | wait=" + (wallNs - cpuNs) + "ns"
            );

        }

        private long getCpuTime() {
            long cpu = THREAD_MX_BEAN.getCurrentThreadCpuTime();
            return cpu < 0 ? 0 : cpu;
        }
    }
}

// use
//WallCpuProbe.Probe probe = WallCpuProbe.start("analyze");
//
//        try {
//                return apply(original);
//        } finally {
//                probe.end();
//        }
