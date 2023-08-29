package dev.authorises.cavelet.deepmarket;

public class DeepMarketUtils {

    public static String calculateUpDown(Double past, Double now){
        try {
            if (past == -1D) {
                return "<red>Server not online enough time";
            }
            if (past.equals(now)) {
                return "<aqua>Unchanged";
            }
            double percent = round((past - now) / past * 100, 2);
            if (percent > 0) {
                return "<green>▲" + percent + "%";
            } else {
                String s = (""+percent).replaceAll("-", "");
                return "<red>▼" + s + "%";
            }
        }catch (Exception e) {
            return "<red>Error";
        }
    }

    public static double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        long factor = (long) Math.pow(10, places);
        value = value * factor;
        long tmp = Math.round(value);
        return (double) tmp / factor;
    }
}
